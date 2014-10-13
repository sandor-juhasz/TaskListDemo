package hu.sjuhasz.demo.appengine.todolistdemo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;

public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

		GoogleClientSecrets secrets;
		
		
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("<html><head><title>Task lists</title></head><body><h1>Task lists</h1>");
		List<String> taskLists = new ArrayList<String>();
		taskLists.add("Hello1");
		taskLists.add("Hello2");
		writer.println("<ul>");
		for (String taskList : taskLists) {
			writer.format("<li>%s</li>", taskList);
		}
		writer.println("</body></html>");
	}
	
}
