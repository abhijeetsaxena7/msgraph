//package com.microsoft.azure.msalwebsample;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonPrimitive;
//import com.microsoft.aad.msal4j.IAuthenticationResult;
//import com.microsoft.azure.msalwebsample.config.AuthHelper;
//import com.microsoft.azure.msalwebsample.config.AuthScope;
//import com.microsoft.azure.msalwebsample.config.HelperMethods;
//import com.microsoft.graph.authentication.IAuthenticationProvider;
//import com.microsoft.graph.http.IHttpRequest;
//import com.microsoft.graph.models.extensions.AppRole;
//import com.microsoft.graph.models.extensions.AppRoleAssignment;
//import com.microsoft.graph.models.extensions.DirectoryObject;
//import com.microsoft.graph.models.extensions.Group;
//import com.microsoft.graph.models.extensions.IGraphServiceClient;
//import com.microsoft.graph.models.extensions.MeetingParticipantInfo;
//import com.microsoft.graph.models.extensions.MeetingParticipants;
//import com.microsoft.graph.models.extensions.OnlineMeeting;
//import com.microsoft.graph.models.extensions.PasswordProfile;
//import com.microsoft.graph.models.extensions.Team;
//import com.microsoft.graph.models.extensions.TeamFunSettings;
//import com.microsoft.graph.models.extensions.TeamMemberSettings;
//import com.microsoft.graph.models.extensions.TeamMessagingSettings;
//import com.microsoft.graph.models.extensions.User;
//import com.microsoft.graph.models.generated.GiphyRatingType;
//import com.microsoft.graph.models.generated.GroupType;
//import com.microsoft.graph.options.HeaderOption;
//import com.microsoft.graph.requests.extensions.GraphServiceClient;
//import com.microsoft.graph.requests.extensions.IDirectoryObjectCollectionReferenceRequest;
//import com.microsoft.graph.requests.extensions.IGroupCollectionRequest;
//import com.microsoft.graph.requests.extensions.IGroupRequest;
//import com.microsoft.graph.requests.extensions.IOnlineMeetingCollectionRequest;
//import com.microsoft.graph.requests.extensions.ITeamCollectionRequest;
//import com.microsoft.graph.requests.extensions.ITeamRequest;
//import com.microsoft.graph.requests.extensions.IUserCollectionRequest;
//import com.microsoft.graph.requests.extensions.IUserRequest;
//import com.microsoft.graph.requests.extensions.IUserRequestBuilder;
//
//@RestController
//public class TestController implements IAuthenticationProvider {
//
//	@Autowired
//	private AuthHelper authHelper;
//
//	@GetMapping("/test")
//	public String test(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/OnlineMeeting.ReadWrite");
//
//			IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
////			IUserRequest req = graphClient.me().buildRequest();
//
//			OnlineMeeting onlineMeeting = new OnlineMeeting();
//			
//			Calendar start = Calendar.getInstance();
//			start.set(2020, Calendar.AUGUST, 24, 12, 00);
//			onlineMeeting.startDateTime = start;
//			
//			Calendar end = Calendar.getInstance();
//			end.set(2020, Calendar.AUGUST, 24, 15, 00);
//			onlineMeeting.endDateTime = end;
//			
//			onlineMeeting.subject = "Test meeting 2";
//			MeetingParticipants mp = new MeetingParticipants();
//			
////			IUserRequestBuilder req = graphClient.me();
////			req.onlineMeetings().req
//			
//			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
//			IOnlineMeetingCollectionRequest req = graphClient.me().onlineMeetings().buildRequest(Arrays.asList(option));
//			
////			req.addHeader("Authorization", "Bearer " + result.accessToken());
//			
//			OnlineMeeting res = req.post(onlineMeeting);
//
////			User user = req.get();
//			return res.joinWebUrl;
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//
//		}
//
//	}
//	
//	@GetMapping("/group")
//	public Group createGroup(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/Group.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All","https://graph.microsoft.com/Directory.AccessAsUser.All");
//			IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
//			
//			Group group = new Group();
//			group.description = "Test group 2 for classroom";
//			group.displayName = "Class T2";
//			List<String> groupTypeList = new ArrayList<String>();
//			groupTypeList.add("unified");
//			group.groupTypes = groupTypeList;
//			group.mailEnabled = true;
//			group.mailNickname = "classT2";
//			group.securityEnabled = false;
//			
//			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
//			
//			IGroupCollectionRequest groupReq = graphClient.groups().buildRequest(Arrays.asList(option));
//			
//			Group res = groupReq.post(group);
//			
//			return res;
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} 
//	}
//	
//	@GetMapping("/user")
//	public User createUser(HttpServletRequest request, HttpServletResponse response) {
//		try {			
//			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/User.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All","https://graph.microsoft.com/Directory.AccessAsUser.All");
//			IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
//			
//			User user = new User();
//			user.accountEnabled = true;
//			user.displayName = "CustomUser";
//			user.mailNickname = "mailNickName";
//			user.userPrincipalName = "customuser@libsys366.onmicrosoft.com";
//			PasswordProfile passwordProfile = new PasswordProfile();
//			passwordProfile.forceChangePasswordNextSignIn = false;
//			passwordProfile.password = "Libsys@1234";
//			user.passwordProfile = passwordProfile;
//			AppRoleAssignment role = new AppRoleAssignment();
////			role.appRoleId = AppRole 
////			user.appRoleAssignments = appRoleAssignments;
//					
//			
//			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
//			
//			IUserCollectionRequest userReq = graphClient.users().buildRequest(Arrays.asList(option));
//			
//			User res = userReq.post(user);
//			
//			return res;
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} 
//	}
//
//	@GetMapping("/owner")
//	public JsonObject addOwnerToGroup(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/Group.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All","https://graph.microsoft.com/Directory.AccessAsUser.All");
//			IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
//			
//			DirectoryObject directoryObject = new DirectoryObject();
//			directoryObject.id ="33eff43d-d581-45ac-9ad1-d0bb5ec2eff5";
//			
//			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
//			IDirectoryObjectCollectionReferenceRequest res = graphClient.groups("902e46a0-471f-4e41-9c33-77abae63245d").owners().references().buildRequest(Arrays.asList(option));
//			return res.post(directoryObject).getRawObject();
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//	
//	@GetMapping("/member")
//	public JsonObject addMemberToGroup(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/GroupMember.ReadWrite.All","https://graph.microsoft.com/Group.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All","https://graph.microsoft.com/Directory.AccessAsUser.All");
//			IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
//			
//			Group group = new Group();
//			/*
//			 * List<String> members = new ArrayList<String>(); members.add("[\]");
//			 */
//			
//			JsonArray members = new JsonArray();
//			members.add("https://graph.microsoft.com/v1.0/directoryObjects/3188014a-237a-4956-9c19-cf44bb4ea15a");
//			group.additionalDataManager().put("members@odata.bind", members);
//			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
//			IGroupRequest res= graphClient.groups("902e46a0-471f-4e41-9c33-77abae63245d").buildRequest(Arrays.asList(option));
//			return res.patch(group).getRawObject();
//		} catch (Throwable e) {			
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//		
//	}
//	
//	@GetMapping("/team")
//	public String createTeamFromGroup(HttpServletRequest request, HttpServletResponse response) {
//		try {
//		IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/Group.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All");
//		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
//		
//		Team team = new Team();
//		
//		TeamMemberSettings memberSettings = new TeamMemberSettings();
//		memberSettings.allowCreateUpdateChannels = false;
//		team.memberSettings = memberSettings;
//		
//		TeamMessagingSettings messageSettings = new TeamMessagingSettings();
//		messageSettings.allowOwnerDeleteMessages = true;
//		messageSettings.allowUserEditMessages = true;
//		messageSettings.allowTeamMentions = true;
//		team.messagingSettings = messageSettings;
//		
//		TeamFunSettings funSettings = new TeamFunSettings();
//		funSettings.allowCustomMemes = true;
//		funSettings.allowGiphy = true;
//		funSettings.allowStickersAndMemes = true;
//		funSettings.giphyContentRating = GiphyRatingType.MODERATE;
//		team.funSettings =  funSettings;
//		
//		Map<String,JsonElement> additionalData = new HashMap<String, JsonElement>();
//		additionalData.put("group@odata.bind", new JsonPrimitive("https://graph.microsoft.com/v1.0/groups/902e46a0-471f-4e41-9c33-77abae63245d"));
//		additionalData.put("template@odata.bind", new JsonPrimitive("https://graph.microsoft.com/1.0/teamsTemplates/standard"));
//		team.additionalDataManager().putAll(additionalData);
//		graphClient.groups("").team().buildRequest().
//		
//		HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
////		ITeamRequest req = graphClient.groups("902e46a0-471f-4e41-9c33-77abae63245d").team().buildRequest(Arrays.asList(option));
////		req.setMaxRetries(3);
////		req.post(team);
////		ITeamCollectionRequest res = graphClient.teams().buildRequest(Arrays.asList(option));
//		return null; //res.post(team).getRawObject().toString();
//		}catch (Throwable e) {			
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//	
//	public String createTeamFromGroupBeta(HttpServletRequest request, HttpServletResponse response) throws Throwable {
//		IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response,"https://graph.microsoft.com/Group.ReadWrite.All","https://graph.microsoft.com/Directory.ReadWrite.All");
//		
//		URL url = new URL(authHelper.getMsGraphEndpointHost() + "/beta/groups/902e46a0-471f-4e41-9c33-77abae63245d/team");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        // Set the appropriate header fields in the request header.
//        conn.setRequestProperty("Authorization", "Bearer " + result.accessToken());
//        conn.setRequestProperty("Accept", "application/json");
//        
//        OutputStream os = conn.getOutputStream();
////        os.write("");
//        os.flush();
//        os.close();
//        
//        conn.setRequestMethod(HttpMethod.PUT.toString());
//        conn.setDoOutput(true);
//        String responseString = HttpClientHelper.getResponseStringFromConn(conn);
//        
//        int responseCode = conn.getResponseCode();
//        if(responseCode != HttpURLConnection.HTTP_OK) {
//            throw new IOException(responseString);
//        }
//
//        JSONObject responseObject = HttpClientHelper.processResponse(responseCode, responseString);
//        return responseObject.toString();
//	}
//
//	@Override
//	public void authenticateRequest(IHttpRequest request) {
//		// NOP
//
//	}
//
//}
