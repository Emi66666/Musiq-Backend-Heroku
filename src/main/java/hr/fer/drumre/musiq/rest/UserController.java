package hr.fer.drumre.musiq.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.login.Authenticator;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	UserService service;
	
	@Autowired
	Authenticator auth;
    
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") String id) {
    	User user = service.findUserById(id);
		if (user == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		return ResponseEntity.ok(user);
    }
    
    @GetMapping("/user/verify")
    public boolean verifyUser(@RequestParam String id, @RequestParam String token, @RequestParam String secret) {
    	return auth.authenticate(id, token, secret)!=null;
    }

    @Data
    @NoArgsConstructor 
    static class UserIdAndToken {
    	private String id;
    	private String token;
    }
    
}