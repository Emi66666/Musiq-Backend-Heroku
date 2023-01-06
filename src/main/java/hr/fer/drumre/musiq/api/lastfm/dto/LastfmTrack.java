package hr.fer.drumre.musiq.api.lastfm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LastfmTrack {

	private String name;
	private String mbid;
	private String url;
	private long duration;
	public int listeners;
	public int playcount;
	
	private LastfmArtist artist;
}
