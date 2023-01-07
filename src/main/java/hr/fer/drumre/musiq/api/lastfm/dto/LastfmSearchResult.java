package hr.fer.drumre.musiq.api.lastfm.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LastfmSearchResult {

	private Trackmatches results;

	
	
	@Data
	@NoArgsConstructor
	public static class Trackmatches {
		private TrackList trackmatches;
	}
	
	@Data
	@NoArgsConstructor
	public static class TrackList {
		private List<Mbid> track;
	}
	
	@Data
	@NoArgsConstructor
	public static class Mbid {
		String mbid;
	}
}

