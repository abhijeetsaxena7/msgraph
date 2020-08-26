// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.azure.msalwebsample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.auth.confidentialClient.AuthorizationCodeProvider;
import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.microsoft.azure.msalwebsample.SessionManagementHelper.FAILED_TO_VALIDATE_MESSAGE;

/**
 * Helpers for acquiring authorization codes and tokens from AAD
 */
@Component
class AuthHelper {

	static final String PRINCIPAL_SESSION_NAME = "principal";
	static final String TOKEN_CACHE_SESSION_ATTRIBUTE = "token_cache";

	private String clientId;
	private String clientSecret;
	private String authority;
	private String redirectUriSignIn;
	private String redirectUriGraph;
	private String msGraphEndpointHost;
	private IGraphServiceClient graphServiceClient;
	@Autowired
	BasicConfiguration configuration;

	@PostConstruct
	public void init() {
		clientId = configuration.getClientId();
		authority = configuration.getAuthority();
		clientSecret = configuration.getSecretKey();
		redirectUriSignIn = configuration.getRedirectUriSignin();
		redirectUriGraph = configuration.getRedirectUriGraph();
		msGraphEndpointHost = configuration.getMsGraphEndpointHost();
	}

	void processAuthenticationCodeRedirect(HttpServletRequest httpRequest, String currentUri, String fullUrl)
			throws Throwable {

		Map<String, List<String>> params = new HashMap<>();
		for (String key : httpRequest.getParameterMap().keySet()) {
			params.put(key, Collections.singletonList(httpRequest.getParameterMap().get(key)[0]));
		}
		// validate that state in response equals to state in request
		StateData stateData = SessionManagementHelper.validateState(httpRequest.getSession(),
				params.get(SessionManagementHelper.STATE).get(0));

		AuthenticationResponse authResponse = AuthenticationResponseParser.parse(new URI(fullUrl), params);
		if (AuthHelper.isAuthenticationSuccessful(authResponse)) {
			AuthenticationSuccessResponse oidcResponse = (AuthenticationSuccessResponse) authResponse;
			// validate that OIDC Auth Response matches Code Flow (contains only requested
			// artifacts)
			validateAuthRespMatchesAuthCodeFlow(oidcResponse);

			IAuthenticationResult result = getAuthResultByAuthCode(httpRequest, oidcResponse.getAuthorizationCode(),
					currentUri);

			// validate nonce to prevent reply attacks (code maybe substituted to one with
			// broader access)
			validateNonce(stateData, getNonceClaimValueFromIdToken(result.idToken()));

			SessionManagementHelper.setSessionPrincipal(httpRequest, result);
		} else {
			AuthenticationErrorResponse oidcResponse = (AuthenticationErrorResponse) authResponse;
			throw new Exception(String.format("Request for auth code failed: %s - %s",
					oidcResponse.getErrorObject().getCode(), oidcResponse.getErrorObject().getDescription()));
		}
	}

	IAuthenticationResult getAuthResultBySilentFlow(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String...scopes)
			throws Throwable {

		IAuthenticationResult result = SessionManagementHelper.getAuthSessionObject(httpRequest);

		IConfidentialClientApplication app = createClientApplication();

		Object tokenCache = httpRequest.getSession().getAttribute("token_cache");
		if (tokenCache != null) {
			app.tokenCache().deserialize(tokenCache.toString());
		}
		
		Set<String> scopeSet = new HashSet<>();
		if(scopes.length>0) {
			for(String scope:scopes) {
				scopeSet.add(scope);
			}
		}
		SilentParameters parameters = SilentParameters.builder(scopeSet, result.account())
				.build();
		CompletableFuture<IAuthenticationResult> future = app.acquireTokenSilently(parameters);
		IAuthenticationResult updatedResult = future.get();

		// update session with latest token cache
		SessionManagementHelper.storeTokenCacheInSession(httpRequest, app.tokenCache().serialize());

		return updatedResult;
	}

	private void validateNonce(StateData stateData, String nonce) throws Exception {
		if (StringUtils.isEmpty(nonce) || !nonce.equals(stateData.getNonce())) {
			throw new Exception(FAILED_TO_VALIDATE_MESSAGE + "could not validate nonce");
		}
	}

	private String getNonceClaimValueFromIdToken(String idToken) throws ParseException {
		return (String) JWTParser.parse(idToken).getJWTClaimsSet().getClaim("nonce");
	}

	private void validateAuthRespMatchesAuthCodeFlow(AuthenticationSuccessResponse oidcResponse) throws Exception {
		if (oidcResponse.getIDToken() != null || oidcResponse.getAccessToken() != null
				|| oidcResponse.getAuthorizationCode() == null) {
			throw new Exception(FAILED_TO_VALIDATE_MESSAGE + "unexpected set of artifacts received");
		}
	}

	void sendAuthRedirect(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String scope,
			String redirectURL) throws IOException {

		// state parameter to validate response from Authorization server and nonce
		// parameter to validate idToken
		String state = UUID.randomUUID().toString();
		String nonce = UUID.randomUUID().toString();

		SessionManagementHelper.storeStateAndNonceInSession(httpRequest.getSession(), state, nonce);

		httpResponse.setStatus(302);
		String authorizationCodeUrl = getAuthorizationCodeUrl(httpRequest.getParameter("claims"), scope, redirectURL,
				state, nonce);
		httpResponse.sendRedirect(authorizationCodeUrl);
	}

	String getAuthorizationCodeUrl(String claims, String scope, String registeredRedirectURL, String state,
			String nonce) throws UnsupportedEncodingException {

		scope = "onlinemeetings.readwrite";
		String urlEncodedScopes = scope == null ? "" : URLEncoder.encode(scope, "UTF-8");
		
		Set<String> scopes = new HashSet<>();
//		scopes.add("user.read");
//		scopes.add("onlinemeeting.readwrite");
		scopes.add("group.readwrite.all");
		scopes.add("directory.readwrite.all");
		scopes.add("directory.accessasuser.all");
		scopes.add("user.readwrite.all");
		scopes.add("groupMember.readwrite.all");
		
		AuthorizationRequestUrlParameters parameters = AuthorizationRequestUrlParameters
				.builder(registeredRedirectURL, /*Collections.singleton(urlEncodedScopes)*/ scopes)
				.responseMode(ResponseMode.QUERY).prompt(Prompt.SELECT_ACCOUNT).state(state).nonce(nonce)
				.claimsChallenge(claims).build();

//		PublicClientApplication pca = PublicClientApplication.builder(clientId).build();
		
		
		ConfidentialClientApplication cca = null;
		try {
			cca = createClientApplication();
			return cca.getAuthorizationRequestUrl(parameters).toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private IAuthenticationResult getAuthResultByAuthCode(HttpServletRequest httpServletRequest,
			AuthorizationCode authorizationCode, String currentUri) throws Throwable {

		IAuthenticationResult result;
		ConfidentialClientApplication app;
		try {
			app = createClientApplication();

			String authCode = authorizationCode.getValue();
			AuthorizationCodeParameters parameters = AuthorizationCodeParameters.builder(authCode, new URI(currentUri))
					.build();

			Future<IAuthenticationResult> future = app.acquireToken(parameters);

			result = future.get();
		} catch (ExecutionException e) {
			throw e.getCause();
		}

		if (result == null) {
			throw new ServiceUnavailableException("authentication result was null");
		}

		SessionManagementHelper.storeTokenCacheInSession(httpServletRequest, app.tokenCache().serialize());

		return result;
	}

	private ConfidentialClientApplication createClientApplication() throws MalformedURLException {
		return ConfidentialClientApplication.builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret))
				.authority(authority).build();
	}

	private static boolean isAuthenticationSuccessful(AuthenticationResponse authResponse) {
		return authResponse instanceof AuthenticationSuccessResponse;
	}

	String getRedirectUriSignIn() {
		return redirectUriSignIn;
	}

	String getRedirectUriGraph() {
		return redirectUriGraph;
	}

	String getMsGraphEndpointHost() {
		return msGraphEndpointHost;
	}

}
