package hu.sjuhasz.demo.appengine.todolistdemo;

import java.util.Arrays;

import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class AuthCodeFlowTest {

	@Test
	public void testme() {

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(),
				new JacksonFactory(),
				"287728078906-25n0cdmib9t6h59fs8l6f5vn5h95p9s0.apps.googleusercontent.com",
				"-CKTFDbQX6ZacNjASmN9l-7x",
				Arrays.asList("https://www.googleapis.com/auth/tasks"))
		.build();
		
		GoogleAuthorizationCodeRequestUrl requestUrl = flow.newAuthorizationUrl();
		String url = requestUrl.setRedirectUri("urn:ietf:wg:oauth:2.0:oob").build();
		System.out.println(url);
		
		GoogleAuthorizationCodeFlow flow2 = new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(),
				new JacksonFactory(),
				"287728078906-25n0cdmib9t6h59fs8l6f5vn5h95p9s0.apps.googleusercontent.com",
				"-CKTFDbQX6ZacNjASmN9l-7x",
				Arrays.asList("https://www.googleapis.com/auth/tasks"))
		.build();
	}
}
