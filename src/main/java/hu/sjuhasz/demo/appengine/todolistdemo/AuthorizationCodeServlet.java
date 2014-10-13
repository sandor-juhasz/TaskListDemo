package hu.sjuhasz.demo.appengine.todolistdemo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

public class AuthorizationCodeServlet extends AbstractAppEngineAuthorizationCodeServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().println("Hello, "+req.getUserPrincipal().getName());
		Tasks service = Utils.getTaskClient();
		List<TaskList> lists = service.tasklists().list().execute().getItems();
		for (TaskList list : lists) {
			resp.getWriter().println(list.getTitle());
		}
	}
	
	  @Override
	  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
	    return Utils.getRedirectUri(req);
	  }

	  @Override
	  protected AuthorizationCodeFlow initializeFlow() throws IOException {
	    return Utils.newFlow();
	  }
}
