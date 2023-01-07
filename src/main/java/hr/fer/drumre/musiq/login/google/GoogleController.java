package hr.fer.drumre.musiq.login.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.rest.TrackController.UserIdAndTokenAndSecret;

@RestController
@CrossOrigin
public class GoogleController {

	@Autowired
	UserService userService;
	
	@Autowired
	GoogleTokenVerifier googleTokenVerifier;
	
	@PostMapping("/user/googleLogin")
	public boolean googleLogin(@RequestBody UserIdAndTokenAndSecret idAndTokenAndSecret) {
		String userId = idAndTokenAndSecret.id;
		String token = idAndTokenAndSecret.token;
		
		return googleTokenVerifier.saveUser(userId, token);
	}
	
}
