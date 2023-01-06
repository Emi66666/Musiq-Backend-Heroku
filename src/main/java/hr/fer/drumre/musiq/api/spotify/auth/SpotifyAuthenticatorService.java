package hr.fer.drumre.musiq.api.spotify.auth;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import hr.fer.drumre.musiq.MusiqException;

@Component
public class SpotifyAuthenticatorService {
	// https://developer.spotify.com/documentation/general/guides/authorization/client-credentials/
	
	private static final String AUTH_URL = "https://accounts.spotify.com/api/token";
	
	private RestTemplate rest;
	private HttpEntity<MultiValueMap<String, String>> requestEntity;
	
	private long expires = 0;
	private String token;
	
	public SpotifyAuthenticatorService(@Value("${spotify.client.id}") String clientId, @Value("${spotify.client.secret}") String clientSecret) {
	    this.rest = new RestTemplate();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    
	    String authString = "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
	    headers.add("Authorization", authString);
	    
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "client_credentials");
	    
	    requestEntity = new HttpEntity<>(body, headers);	    
	}
	
	public String requestToken() {
		
		if (System.currentTimeMillis() > expires) {
			ResponseEntity<SpotifyTokenResponse> responseEntity = rest.exchange(AUTH_URL, HttpMethod.POST, requestEntity, SpotifyTokenResponse.class);
			
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new MusiqException("Request for token failed");
			}
			
			SpotifyTokenResponse tokenResponse = responseEntity.getBody();
			expires = System.currentTimeMillis() + (tokenResponse.getExpiry()-10) * 1000; // subtracted 10 seconds so we have some leeway with lag and whatnot
			token = tokenResponse.getTokenType() + " " + tokenResponse.getToken();
		}
		
		return token;
	}
	
}
