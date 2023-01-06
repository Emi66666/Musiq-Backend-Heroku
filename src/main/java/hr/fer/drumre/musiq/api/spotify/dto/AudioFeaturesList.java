package hr.fer.drumre.musiq.api.spotify.dto;

import java.util.List;

import hr.fer.drumre.musiq.db.mongo.tracks.AudioFeatures;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioFeaturesList {

	private List<AudioFeatures> audio_features;
	
}
