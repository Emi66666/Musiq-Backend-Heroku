package hr.fer.drumre.musiq.api.lastfm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LastfmArtist {

	private String name;
	private String mbid;
	private String url;
}
