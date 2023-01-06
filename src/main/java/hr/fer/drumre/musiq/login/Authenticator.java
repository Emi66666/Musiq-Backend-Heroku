package hr.fer.drumre.musiq.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.login.facebook.FacebookTokenVerifier;
import hr.fer.drumre.musiq.login.twitter.TwitterTokenVerifier;

@Service
public class Authenticator {

	@Autowired
	UserService userService;
	
	@Autowired
	FacebookTokenVerifier fbTokenVerifier;

	@Autowired
	TwitterTokenVerifier twTokenVerifier;
	
	public User authenticate(String id, String token, String secret) {
		User user = userService.findUserById(id);
		
		if (user == null) return null;
		
		switch (user.getSocialNetwork()) {
		case FACEBOOK:
			if (!fbTokenVerifier.verifyUser(id, token))
				return null;
			break;
		case TWITTER:
			if (!twTokenVerifier.verifyUser(id, token, secret))
				return null;
			break;
		}
		
		return user;
	}
	
}
