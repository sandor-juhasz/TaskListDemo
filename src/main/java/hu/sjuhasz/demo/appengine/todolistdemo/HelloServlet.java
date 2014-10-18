package hu.sjuhasz.demo.appengine.todolistdemo;

import hu.sjuhasz.lib.appengine.OAuthContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OAuthContext context = OAuthContext.getContext();
		
		//System.out.format("Access token: %s, time left: %d, Refresh token: %s\n", context.getCredential().getAccessToken(), context.getCredential().getExpiresInSeconds(), context.getCredential().getRefreshToken());
		try {
			resp.setContentType("text/html");
			PrintWriter writer = resp.getWriter();
			writer.println("<html><head><title>Task lists</title></head><body><h1>Task lists</h1>");
			writer.println("Owner: "+req.getUserPrincipal().getName()+"<br>");
			writer.format("Debug info: <br/>access token: %s,<br/> time left: %d,<br/> Refresh token: %s<br/>\n", context.getCredential().getAccessToken(), context.getCredential().getExpiresInSeconds(), context.getCredential().getRefreshToken());
			List<TaskList> taskLists = getTaskLists();
			writer.println("Number of task lists: "+taskLists.size());
			writer.println("<ul>");
			for (TaskList taskList : taskLists) {
				writer.format("<li>%s</li>", taskList.getTitle());
			}
			writer.println("</body></html>");
		} catch (GoogleJsonResponseException e) {
			// No refresh token was found.
			if (e.getStatusCode() == 401) {
				context.getCredential().setAccessToken(null);
			}
			throw e;
		} catch (TokenResponseException e) {
			// Refresh token is invalid. Re-authorizing...
			throw e;
		} catch (Exception e) {
			PrintWriter w = resp.getWriter();
			w.format("Access token: %s, time left: %d, Refresh token: %s\n", context.getCredential().getAccessToken(), context.getCredential().getExpiresInSeconds(), context.getCredential().getRefreshToken());
			e.printStackTrace(w);
		}
	}
	
	private List<TaskList> getTaskLists() throws Exception {
		OAuthContext context = OAuthContext.getContext();
		Tasks service = new Tasks.Builder(
				context.getTransport(),
				context.getJsonFactory(),
				context.getCredential())
			.setApplicationName("DemoProject")
			.build();
		return service.tasklists().list().execute().getItems();
	}
	
}
