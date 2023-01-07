package hr.fer.drumre.musiq.login.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import hr.fer.drumre.musiq.db.mongo.users.SocialNetwork;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;

@Service
public class GoogleTokenVerifier {

	@Autowired
	UserService userService;
	
	public boolean verifyUser(String userId, String token) {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
			    .setAudience(Collections.singletonList("918397890491-m0j7bn0r8nglebao1qel1shnebfv4isc.apps.googleusercontent.com"))
			    .build();
		
		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		Payload payload = idToken.getPayload();
		String id = payload.getSubject();
		
		return (userId.equals(id));
	}
	
	public boolean saveUser(String userId, String token) {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
			    .setAudience(Collections.singletonList("918397890491-m0j7bn0r8nglebao1qel1shnebfv4isc.apps.googleusercontent.com"))
			    .build();
		
		GoogleIdToken idToken;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		Payload payload = idToken.getPayload();
		String id = payload.getSubject();
		
		if (userService.findUserById(id) != null) {
			return true;
		}
		
		if (!userId.equals(id)) {
			return false;
		}
		
		User user = new User();
		user.setId(id);
		user.setEmail(payload.getEmail());
		user.setName((String) payload.get("name"));
		user.setSocialNetwork(SocialNetwork.GOOGLE);
		userService.saveUser(user);
		
		return true;
	}
	
}
