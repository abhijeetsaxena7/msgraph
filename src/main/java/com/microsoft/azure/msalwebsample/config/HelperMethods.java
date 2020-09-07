package com.microsoft.azure.msalwebsample.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.microsoft.azure.msalwebsample.config.MicrosoftScopes.BaseScope;

@Component
public class HelperMethods {	
	
	public Set<String> getAuthScopes(BaseScope[] scopes) {
		Set<String> scopeSet = new HashSet<>();
		for(BaseScope scope:scopes) {
			scopeSet.add(scope.getAuthValue());
		}
		return scopeSet;
	}
	
	public Set<String> getReqScopes(BaseScope[] scopes) {
		Set<String> scopeSet = new HashSet<>();
		for(BaseScope scope:scopes) {
			scopeSet.add(scope.getReqValue());
		}
		return scopeSet;
	}
}
