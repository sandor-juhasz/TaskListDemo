package hu.sjuhasz.lib.appengine;

import hu.sjuhasz.demo.appengine.todolistdemo.Utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class OAuthFilter implements Filter {

	private AuthorizationCodeFlow flow;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * TODO: get auth code path from filter config.
	 * Make the Flow available as ThreadLocal.
	 */
	public void doFilter(
			final ServletRequest request, 
			final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		System.out.println("[OAuthFilter] processing "+req.getRequestURI());		
		if (req.getPathInfo() != null && req.getPathInfo().equals("/authCode")) {
			System.out.println("[OAuthFilter] authCode path was detected.");
			
		    StringBuffer buf = req.getRequestURL();
		    if (req.getQueryString() != null) {
		      buf.append('?').append(req.getQueryString());
		    }
		    AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
		    String code = responseUrl.getCode();
		    if (responseUrl.getError() != null) {
		      resp.getWriter().println(responseUrl.getError());
		    } else if (code == null) {
		      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		      resp.getWriter().print("Missing authorization code");
		    } else {
		        if (flow == null) {
		          flow = Utils.newFlow();
		        }
		        String redirectUri = "http://localhost:8888/authCode";
		        TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
		        User user = UserServiceFactory.getUserService().getCurrentUser();
		        if (user == null) {
		        	resp.getWriter().println("Cannot identify user while processing authorization code.");
		        } else {
		        	String userId = user.getUserId();
			        Credential credential = flow.createAndStoreCredential(tokenResponse, userId);
			        resp.sendRedirect("/");
		        }
		    }						
		} else {
			User user = UserServiceFactory.getUserService().getCurrentUser();
			if (user != null) {
				System.out.println("[OAuthFilter] User "+user.getUserId()+" was detected with email "+user.getEmail()+".");
				if (flow == null)				
					flow = Utils.newFlow();
				Credential credential = flow.loadCredential(user.getUserId());
				if (credential == null || credential.getAccessToken() == null) {
					System.out.println("[OAuthFilter] no credential was found or access token is null. Reauthorizing.");
				    AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
				    authorizationUrl.setRedirectUri("http://localhost:8888/authCode");
				    String location = authorizationUrl.build();
				    System.out.println("[OAuthFilter] Redirecting to "+location);
				    resp.sendRedirect(location);
				} else {
					System.out.println("[OAuthFilter] existing credential for user was found.");
					System.out.println("[OAuthFilterFilter] calling protected resource. "+req.getRequestURI());
					try {
						chain.doFilter(request, response);
					} catch (Exception e) {
						if (credential.getAccessToken() == null) {
							System.out.println("[OAuthFilter] null access token was detected after processing protected resource. Reauthorizing...");
						    AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
						    authorizationUrl.setRedirectUri("http://localhost:8888/");
						    String location = authorizationUrl.build();
						    System.out.println("[OAuthFilter] Redirecting to "+location);
						    resp.sendRedirect(location);							
						}
					}
				}
			} else {
				if (req.getRequestURI().startsWith("/_ah/")) {
					System.out.println("[OAuthFilter] Processing admin request.");
					chain.doFilter(request, response);
				}
				System.out.println("[OAuthFilter] User is not logged in. Please configure user login first.");
			}
		} 
	}

	public void destroy() {
	}

}
