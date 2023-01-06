package hr.fer.drumre.musiq.api.lastfm.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LastfmTracksWrapper {

	private TrackList tracks;

	@Data
	@NoArgsConstructor
	public static class TrackList {
		List<LastfmTrack> track;
	}
}
