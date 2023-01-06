package hr.fer.drumre.musiq.login.facebook;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.fer.drumre.musiq.MusiqException;
import hr.fer.drumre.musiq.db.mongo.users.User;

@Service
public class FacebookTokenVerifier {

	private static final String URL = "https://graph.facebook.com";
	private RestTemplate rest;
	private HttpHeaders headers;
	
	public FacebookTokenVerifier() {
	    this.rest = new RestTemplate();
	    this.headers = new HttpHeaders();
	    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	    headers.set("User-Agent", "Musiq app v0.1");
	}
	
	public User getUser(String id, String accessToken) {
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		
		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL + "/" + id)
				.queryParam("fields", "id,name,email")
				.queryParam("access_token", accessToken)
				.encode()
		        .toUriString();

		ResponseEntity<User> responseEntity = rest.exchange(
				urlComplete,
				HttpMethod.GET,
				requestEntity,
				User.class);
	
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new MusiqException("Invalid access token!");
		}
		
		return responseEntity.getBody();
	}
	
	public boolean verifyUser(String id, String accessToken) {
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		
		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL + "/" + id)
				.queryParam("fields", "id,name,email")
				.queryParam("access_token", accessToken)
				.encode()
		        .toUriString();

		ResponseEntity<User> responseEntity = rest.exchange(
				urlComplete,
				HttpMethod.GET,
				requestEntity,
				User.class);
	
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			return false;
		} else {
			return true;
		}
	}
	
}
