package hr.fer.drumre.musiq.api.spotify.dto;

import java.util.List;

import hr.fer.drumre.musiq.db.mongo.tracks.Album;
import hr.fer.drumre.musiq.db.mongo.tracks.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifySearchResponse {
	
	private SearchResponseAlbums albums;
	private SearchResponseArtists artists;
	private SearchResponseTracks tracks;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchResponseAlbums {
		private List<Album> items;
		private String next;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchResponseArtists {
		private List<Artist> items;
		private String next;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchResponseTracks {
		private List<SpotifyTrack> items;
		private String next;
	}
	
}
