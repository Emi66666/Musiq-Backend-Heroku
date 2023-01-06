package hr.fer.drumre.musiq.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.drumre.musiq.MusiqException;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.login.facebook.FacebookTokenVerifier;
import hr.fer.drumre.musiq.login.twitter.TwitterTokenVerifier;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserService service;
	
	@Autowired
	FacebookTokenVerifier fbTokenVerifier;
	
	@Autowired
	TwitterTokenVerifier twTokenVerifier;
	
    @PostMapping("/user/login")
    public ResponseEntity<User> user(@RequestBody UserIdAndToken idAndToken) {
    	try {
    		User user = fbTokenVerifier.getUser(idAndToken.getId(), idAndToken.getToken());
    		service.saveUser(user);
    		return ResponseEntity.ok(user);
    	} catch (MusiqException ex) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") String id) {
    	User user = service.findUserById(id);
		if (user == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		return ResponseEntity.ok(user);
    }
    
    @GetMapping("/user/verify")
    public boolean verifyUser(@RequestParam String id, @RequestParam String token, @RequestParam String secret) {
    	User user = service.findUserById(id);
    	if (user == null)
    		return false;
    	
		switch (user.getSocialNetwork()) {
		case FACEBOOK:
			if (!fbTokenVerifier.verifyUser(id, token))
				return false;
			break;
		case TWITTER:
			if (!twTokenVerifier.verifyUser(id, token, secret))
				return false;
			break;
		}
		
		return true;
    }

    @Data
    @NoArgsConstructor 
    static class UserIdAndToken {
    	private String id;
    	private String token;
    }
    
}