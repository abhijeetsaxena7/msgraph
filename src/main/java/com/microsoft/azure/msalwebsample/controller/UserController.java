package com.microsoft.azure.msalwebsample.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.azure.msalwebsample.config.AuthHelper;
import com.microsoft.azure.msalwebsample.config.AuthScope;
import com.microsoft.azure.msalwebsample.config.GraphServiceClientWrapper;
import com.microsoft.azure.msalwebsample.config.HelperMethods;
import com.microsoft.azure.msalwebsample.model.UserModel;
import com.microsoft.graph.models.extensions.PasswordProfile;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.requests.extensions.IUserCollectionRequest;

public class UserController {
	@Autowired
	private AuthHelper authHelper;
	@Autowired
	private HelperMethods helper;
	@Autowired
	private GraphServiceClientWrapper graphClientWrapper;
	
	public ResponseEntity createUser(HttpServletRequest request, HttpServletResponse response, @RequestBody UserModel userModel) {
		ResponseEntity resEntity;
		try {
			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response, helper.getReqScopes(AuthScope.User.Create.values()));
			User user = new User();
			user.accountEnabled = true;
			user.displayName = userModel.getDisplayName();
			user.mailNickname = userModel.getMailNickname();
			user.userPrincipalName = userModel.getUserPrincipalName();
			PasswordProfile passwordProfile = new PasswordProfile();
			passwordProfile.forceChangePasswordNextSignIn = true;
			passwordProfile.password = userModel.getDefaultPassword();
			user.passwordProfile = passwordProfile;
			user.department = userModel.getDepartment();						
			user.birthday= userModel.getDob();
			
			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());			
			IUserCollectionRequest userReq = graphClientWrapper.getGraphServiceClient().users().buildRequest(Arrays.asList(option));
			
			User res = userReq.post(user);

			resEntity = new ResponseEntity<User>(res,HttpStatus.CREATED);
		} catch (Throwable e) {
			e.printStackTrace();
			resEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resEntity;
		
	}
	
	
	
	public ResponseEntity<String> deleteUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId){
		ResponseEntity<String> resEntity;
		try {
			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response, helper.getReqScopes(AuthScope.User.Delete.values()));			
			
			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());			
			graphClientWrapper.getGraphServiceClient().users(userId).buildRequest(Arrays.asList(option)).delete();
			resEntity = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch (Throwable e) {
			e.printStackTrace();
			resEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return resEntity;
	}
}
