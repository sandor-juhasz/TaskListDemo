package hu.sjuhasz.demo.appengine.todolistdemo;

import hu.sjuhasz.lib.appengine.OAuthContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.appengine.api.users.UserServiceFactory;

public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OAuthContext context = OAuthContext.getContext();
		
		//System.out.format("Access token: %s, time left: %d, Refresh token: %s\n", context.getCredential().getAccessToken(), context.getCredential().getExpiresInSeconds(), context.getCredential().getRefreshToken());
		try {
			Tasks service = new Tasks.Builder(
					context.getTransport(),
					context.getJsonFactory(),
					context.getCredential())
				.setApplicationName("DemoProject")
				.build();
			List<TaskList> taskLists = service.tasklists().list().execute().getItems();
			resp.setContentType("text/html");
			PrintWriter writer = resp.getWriter();
			writer.println("<html><head><title>Task lists</title></head><body><h1>Task lists</h1>");
			writer.println("Owner: "+req.getUserPrincipal().getName());
			writer.println("Number of task lists: "+taskLists.size());
			writer.println("<ul>");
			for (TaskList taskList : taskLists) {
				writer.format("<li>%s</li>", taskList.getTitle());
			}
			writer.println("</body></html>");
		} catch (Exception e) {
			e.printStackTrace(resp.getWriter());
		}
	}
	
}
