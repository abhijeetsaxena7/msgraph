package com.microsoft.azure.msalwebsample.config;

import org.springframework.stereotype.Component;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

@Component
public class GraphServiceClientWrapper implements IAuthenticationProvider{
	private IGraphServiceClient graphServiceClient;
	public GraphServiceClientWrapper() {
		this.graphServiceClient = GraphServiceClient.builder().authenticationProvider(this).buildClient();
	}
	
	public IGraphServiceClient getGraphServiceClient() {
		return graphServiceClient;
	}

	@Override
	public void authenticateRequest(IHttpRequest request) {
		//NOP
	}

}
