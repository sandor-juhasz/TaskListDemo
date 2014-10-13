package hu.sjuhasz.lib.appengine;

import com.google.api.client.auth.oauth2.Credential;

public class OAuthContext {

	private static final ThreadLocal<OAuthContext> context = new ThreadLocal<OAuthContext>();
	 
    public static void createContext(String userId, Credential credential) {
    	context.set(new OAuthContext(userId, credential));
    }
 
    public static OAuthContext getContext() {
        return context.get();
    }
 
    public static void cleanup() {
        context.remove();
    }
	    
	private final String userId;
	private final Credential credential;
	
	public OAuthContext(final String userId, final Credential credential) {
		this.userId = userId;
		this.credential = credential;
	}
	public String getUserId() {
		return userId;
	}
	
	public Credential getCredential() {
		return credential;
	}
	
}
