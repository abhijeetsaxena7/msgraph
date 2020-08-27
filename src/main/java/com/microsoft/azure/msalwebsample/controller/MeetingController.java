package com.microsoft.azure.msalwebsample.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.azure.msalwebsample.config.AuthHelper;
import com.microsoft.azure.msalwebsample.config.AuthScope;
import com.microsoft.azure.msalwebsample.config.GraphServiceClientWrapper;
import com.microsoft.azure.msalwebsample.config.HelperMethods;
import com.microsoft.azure.msalwebsample.model.OnlineMeetingModel;
import com.microsoft.graph.models.extensions.OnlineMeeting;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.requests.extensions.IOnlineMeetingCollectionRequest;

@RestController
@RequestMapping("/meeting")
public class MeetingController {
	@Autowired
	private AuthHelper authHelper;
	@Autowired
	private HelperMethods helper;
	@Autowired
	private GraphServiceClientWrapper graphClientWrapper;
	
	@PostMapping("")
	public ResponseEntity createMeeting(HttpServletRequest request, HttpServletResponse response, @RequestBody OnlineMeetingModel onlineMeetingModel){
		ResponseEntity resEntity;
		try {
			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response, helper.getReqScopes(AuthScope.Meeting.Create.values()));
			
			OnlineMeeting meeting = new OnlineMeeting();
			meeting.subject = onlineMeetingModel.getSubject();
			meeting.startDateTime = onlineMeetingModel.getStartDatetime();
			meeting.endDateTime = onlineMeetingModel.getEndDatetime();
			
			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
			
			IOnlineMeetingCollectionRequest req = graphClientWrapper.getGraphServiceClient().me().onlineMeetings().buildRequest(Arrays.asList(option));
			OnlineMeeting res = req.post(meeting);			
			resEntity = new ResponseEntity<OnlineMeeting>(res,HttpStatus.CREATED);
		} catch (Throwable e) {
			e.printStackTrace();
			resEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return resEntity;
	}
	
	@DeleteMapping("")
	public ResponseEntity deleteMeeting(HttpServletRequest request, HttpServletResponse response, @RequestParam String meetingId){
		ResponseEntity resEntity;
		try {
			IAuthenticationResult result = authHelper.getAuthResultBySilentFlow(request, response, helper.getReqScopes(AuthScope.Meeting.Delete.values()));
						
			HeaderOption option = new HeaderOption("Authorization", "Bearer " + result.accessToken());
			
			graphClientWrapper.getGraphServiceClient().me().onlineMeetings(meetingId).buildRequest(Arrays.asList(option)).delete();			
			resEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Throwable e) {
			e.printStackTrace();
			resEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return resEntity;
	}

}
