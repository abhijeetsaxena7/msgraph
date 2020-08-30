package com.microsoft.azure.msalwebsample.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.msalwebsample.config.BasicConfiguration;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private BasicConfiguration basicConfig;
	
	@GetMapping("/redirectUri")
	public void sendRedirectResponse(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			response.sendRedirect(basicConfig.getRedirectResponseUri());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
//		return request.getHeader(HttpHeaders.COOKIE);
		
		
	}
	
	public String getSessionId(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.COOKIE);
	}

}
