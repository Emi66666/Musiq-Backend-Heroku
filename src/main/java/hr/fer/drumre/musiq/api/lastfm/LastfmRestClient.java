package hr.fer.drumre.musiq.api.lastfm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hr.fer.drumre.musiq.Util;
import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrack;
import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrackWrapper;
import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTracksWrapper;

@Component
public class LastfmRestClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(LastfmRestClient.class.getName());
	
	private static final String URL = "http://ws.audioscrobbler.com/2.0";
	private RestTemplate rest;
	private HttpHeaders headers;
	
	@Value("${lastfm.client.key}")
	private String key;
	
	public LastfmRestClient() {
	    this.rest = new RestTemplate();
	    this.headers = new HttpHeaders();
	    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	    headers.set("User-Agent", "Musiq app v0.1");
	}
	
	
	private <T> T makeRequest(String url, HttpMethod method, Class<T> responseType) {
		//LOGGER.info("Sending request to {}", url);
	
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
		
		return responseEntity.getBody();
	}

	public void printRequest(String href) {
		LOGGER.info("Received: {}", makeRequest(href, HttpMethod.GET, String.class));		
	}

	public LastfmTrack getTrack(String mbid) {
		String urlComplete = UriComponentsBuilder.fromHttpUrl(URL)
				.queryParam("api_key", key)
				.queryParam("format", "json")
				.queryParam("method", "track.getInfo")
				.queryParam("mbid", mbid)
				.encode()
		        .toUriString();
		
		LastfmTrackWrapper result = makeRequest(urlComplete, HttpMethod.GET, LastfmTrackWrapper.class); // sometimes null for some reason? TODO research
		if (result != null) {
			return result.getTrack();
		} else {
			return null;
		}
		
	}
	
	public List<LastfmTrack> getTopTracks() {
		
		List<LastfmTrack> tracks = new ArrayList<>();
		boolean stop = false;
		int page = 1;
	
		while (!stop || page >= 10) {
			String urlComplete = UriComponentsBuilder.fromHttpUrl(URL)
					.queryParam("api_key", key)
					.queryParam("format", "json")
					.queryParam("method", "chart.gettoptracks")
					.queryParam("page", page)
					.encode()
			        .toUriString();
			
			LastfmTracksWrapper result = makeRequest(urlComplete, HttpMethod.GET, LastfmTracksWrapper.class); 
			if (result != null && result.getTracks() != null && result.getTracks().getTrack().size() > 0) {
				tracks.addAll(result.getTracks().getTrack());
			} else {
				stop = true;
			}
			
			page++;
		}

		return tracks;

	}
	
	
}
