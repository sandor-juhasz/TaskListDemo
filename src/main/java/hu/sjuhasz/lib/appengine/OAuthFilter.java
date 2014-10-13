package hu.sjuhasz.lib.appengine;

import hu.sjuhasz.demo.appengine.todolistdemo.Utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class OAuthFilter extends AbstractAppEngineOAuthFilter {

	private AuthorizationCodeFlow flow;

	/*
	 * Flow-related methods
	 */
	@Override
	protected AuthorizationCodeFlow getFlow() throws IOException {
		if (flow == null) {
			flow = Utils.newFlow();
		}
		return flow;
	}

	/*
	 * User info methods
	 */
	

	/*
	 * Authorization callback-related methods
	 */
	
	@Override
	protected boolean isAuthorizationCodeCallbackRequest(HttpServletRequest req) {
		return req.getPathInfo() != null && req.getPathInfo().equals("/authCode");
	}

	@Override
	protected String getAuthorizationCodeCallbackUri(HttpServletRequest req) {
		return "http://localhost:8888/authCode";
	}

	@Override
	protected void onSuccessfulAuthorization(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		resp.sendRedirect("/");
	}
	
}