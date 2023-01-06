package hr.fer.drumre.musiq.db.mongo.tracks;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioFeatures {
	// https://developer.spotify.com/documentation/web-api/reference/#/operations/get-several-audio-features

	public String id;
	
	public double acousticness;
	public double danceability;
	public double energy;
	public double liveness;
	public double loudness;
	public double tempo;
	public double valence;
	
	public double instrumentalness;
	public double speechiness;
	
	public int key;
	public int mode;
	public int time_signature;
	
}
