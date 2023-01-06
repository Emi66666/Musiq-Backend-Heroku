package hr.fer.drumre.musiq.api.spotify;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.fer.drumre.musiq.MusiqException;
import hr.fer.drumre.musiq.Util;
import hr.fer.drumre.musiq.api.spotify.auth.SpotifyAuthenticatorService;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifySearchResponse;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrack;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrackList;
import hr.fer.drumre.musiq.api.spotify.dto.AudioFeaturesList;
import hr.fer.drumre.musiq.db.mongo.tracks.AudioFeatures;

@Component
public class SpotifyRestClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyRestClient.class.getName()); 
	
	private static final String URL = "https://api.spotify.com/v1";
	private RestTemplate rest;
	private HttpHeaders headers;

	@Autowired
	SpotifyAuthenticatorService spotifyAuthenticatorService;
	
	public SpotifyRestClient() {
	    this.rest = new RestTemplate();
	    this.headers = new HttpHeaders();
	    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	    headers.set("User-Agent", "Musiq app v0.1");
	}
	
	public void reset() {
		this.rest = new RestTemplate();
	}
	
	
	private <T> T makeRequest(String url, HttpMethod method, Class<T> responseType) {
		//LOGGER.info("Sending request to {}", url);
	
		headers.add("Authorization", spotifyAuthenticatorService.requestToken());
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		
		int count = 0;
		ResponseEntity<T> responseEntity = null;
		while(true) {
			if (count >= 5) {
				LOGGER.info("Request failed after 5 tries");
				return null;
			}

			count++;
			try {
				responseEntity = rest.exchange(url, method, requestEntity, responseType);
			} catch (Exception ex) {
				LOGGER.info("Received 502 Bad Gateway, will try again in 10s");
				requestEntity = new HttpEntity<String>("", headers);
				Util.sleep(10000);
				continue;
			}
			HttpStatus statusCode = responseEntity.getStatusCode();
			if (statusCode.equals(HttpStatus.TOO_MANY_REQUESTS)) {
				LOGGER.info("Received 429 Too Many Requests, will try again soon");
				try {
					Util.sleep(Integer.parseInt(responseEntity.getHeaders().get("Retry-After").get(0)));
				} catch (Exception ex) {
					Util.sleep(2000);
				}
			} else if (statusCode.equals(HttpStatus.BAD_GATEWAY)) {
				LOGGER.info("Received 502 Bad Gateway, will try again in 10s");
				requestEntity = new HttpEntity<String>("", headers);
				Util.sleep(10000);
				continue;
			} else {
				break;
			}
		}
		
		if (responseEntity != null) {
			return responseEntity.getBody();
		} else {
			return null;
		}
		
	}
	
	public void printRequest(String href) {
		LOGGER.info("Received: {}", makeRequest(href, HttpMethod.GET, String.class));		
	}
	
	public SpotifyTrack getTrack(String spotifyId) {	
		return makeRequest(URL + "/tracks/" + spotifyId, HttpMethod.GET, SpotifyTrack.class);
	}
	
	public List<SpotifyTrack> getTracks(List<String> trackIds) {
		if (trackIds.size() > 50) throw new MusiqException("Cannot get more than 50 at once");

		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL + "/tracks")
				.queryParam("ids", trackIds.stream().collect(Collectors.joining(",")))
				.encode()
		        .toUriString();

		SpotifyTrackList tracks = makeRequest(urlComplete, HttpMethod.GET, SpotifyTrackList.class);
		if (tracks == null) return null;
		return tracks.getTracks();
	}
	
	public AudioFeatures getAudioFeatures(String trackId) {
		return makeRequest(URL + "/audio-features/" + trackId, HttpMethod.GET, AudioFeatures.class);
	}
	
	public List<AudioFeatures> getMultipleAudioFeatures(List<String> trackIds) {
		if (trackIds.size() > 50) throw new MusiqException("Cannot get more than 100 at once");

		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL + "/audio-features")
				.queryParam("ids", trackIds.stream().collect(Collectors.joining(",")))
				.encode()
		        .toUriString();
		
		AudioFeaturesList response = makeRequest(urlComplete, HttpMethod.GET, AudioFeaturesList.class);
		
		if (response==null) return null;
		return response.getAudio_features();
	}
	
	public List<SpotifyTrack> search(String query, int limit) {
		
		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL + "/search")
					.queryParam("q", query)
					.queryParam("limit", limit)
					.queryParam("offset", 0)
					.queryParam("type", "track") 
					.encode()
			        .toUriString();
		
		SpotifySearchResponse searchResponse = makeRequest(urlComplete, HttpMethod.GET, SpotifySearchResponse.class);
		
		if (searchResponse == null) return null;

		return searchResponse.getTracks().getItems();
	}
	
}
