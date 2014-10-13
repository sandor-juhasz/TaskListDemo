package hu.sjuhasz.demo.appengine.todolistdemo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthorizationCodeCallbackServlet extends AbstractAppEngineAuthorizationCodeCallbackServlet  {

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,
			Credential credential) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    resp.sendRedirect("/");
	}
	
	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp,
			AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	    String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
	    resp.getWriter().print("<h3>" + nickname + ", why don't you want to play with me?</h1>");
	    resp.setStatus(200);
	    resp.addHeader("Content-Type", "text/html");
	}
	
	@Override
	protected String getRedirectUri(HttpServletRequest arg0)
			throws ServletException, IOException {
		return Utils.getRedirectUri(arg0);
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		return Utils.newFlow();
	}

}
