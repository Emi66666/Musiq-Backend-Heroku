package hr.fer.drumre.musiq.api.spotify.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpotifyTrackList {
	private List<SpotifyTrack> tracks;
}
