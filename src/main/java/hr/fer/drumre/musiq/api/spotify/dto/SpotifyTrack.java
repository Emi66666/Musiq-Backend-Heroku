package hr.fer.drumre.musiq.api.spotify.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import hr.fer.drumre.musiq.db.mongo.tracks.Album;
import hr.fer.drumre.musiq.db.mongo.tracks.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpotifyTrack {
	// https://developer.spotify.com/documentation/web-api/reference/#/operations/get-track
	
	private String id;
	private String name;
	private String href;
	private Album album;
	private List<Artist> artists;
	
	private String uri;
	
	@JsonProperty("preview_url")
	private String previewUrl;
	
	@JsonProperty("disc_number")
	private int discNumber;
	
	@JsonProperty("track_number")
	private int trackNumber;
	
	@JsonProperty("duration_ms")
	private long duration;
	
	private boolean explicit;
	
	private int popularity;
	
	//@JsonProperty("available_markets")
	//private List<String> availableMarkets;
	
	
	
	
	
	
	
}
