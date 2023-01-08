package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrack;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrack;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Document("tracks")
public class Track implements Comparable<Track> {

	@Id
	public String id;
	
	public String lastfm_url;
	public String spotify_id;
	public String mbid;
	
	public String name;
	public List<Artist> artists;
	public Album album;
	
	public String uri;
	public String preview_url;
	public int disc_number;
	public int track_number;
	public long duration;
	public boolean explicit;
	
	public int spotifyPopularity;
	public int lastfmListeners;
	public int lastfmPlaycount;
	
	public int popularity;
	
	public AudioFeatures audio_features;
	
	private int likeNumber;
	
	public boolean isComplete() {
		return spotify_id != null && mbid != null && name != null;
	}

	
	public void calculatePopularity() {
		this.popularity = spotifyPopularity + lastfmListeners/100000 + lastfmPlaycount/1000000;
	}
	
	public double similarityTo(Track other) {
		if (!this.isComplete() || other.isComplete()) return 0;
		double total = 0;
		AudioFeatures af1 = this.getAudio_features();
		AudioFeatures af2 = other.getAudio_features();
		total += 1-Math.abs(af1.getAcousticness()-af2.getAcousticness());
		total += 1-Math.abs(af1.getDanceability()-af2.getDanceability());
		total += 1-Math.abs(af1.getEnergy()-af2.getEnergy());
		total += 1-Math.abs(af1.getLiveness()-af2.getLiveness());
		total += 1-Math.abs(af1.getLoudness()-af2.getLoudness());
		total += 1-Math.abs(af1.getTempo()-af2.getTempo());
		total += 1-Math.abs(af1.getValence()-af2.getValence());
		total += 1-Math.abs(af1.getInstrumentalness()-af2.getInstrumentalness());
		total += 1-Math.abs(af1.getSpeechiness()-af2.getSpeechiness());
		total += 0.25*(4-Math.abs(af1.getTime_signature()-af2.getTime_signature()));
		
		for (Artist a1 : this.getArtists()) {
			if (other.getArtists().contains(a1)) {
				total += 2.5;
			}
		}
		
		if (this.getAlbum().equals((other.getAlbum()))) {
			total += 1.2;
		}
			
		return total;
	}
	
	public void addData(SpotifyTrack sTrack) {
		this.setName(sTrack.getName());
		this.setArtists(sTrack.getArtists());
		this.setAlbum(sTrack.getAlbum());
		
		this.setUri(sTrack.getUri());
		this.setPreview_url(sTrack.getPreviewUrl());
		this.setDisc_number(sTrack.getDiscNumber());
		this.setTrack_number(sTrack.getTrackNumber());
		this.setDuration(sTrack.getDuration());
		this.setExplicit(sTrack.isExplicit());
		this.setSpotifyPopularity(sTrack.getPopularity());
		
		if (this.spotify_id == null || this.spotify_id.equals("")) this.setLastfm_url(sTrack.getId());
	}
	
	public void addData(AudioFeatures audioFeatures) {
		this.setAudio_features(audioFeatures);
	}
	
	public void addData(LastfmTrack lTrack) {
		this.setLastfmListeners(lTrack.getListeners());
		this.setLastfmPlaycount(lTrack.getPlaycount());
		if (this.lastfm_url == null || this.lastfm_url.equals("")) this.setLastfm_url(lTrack.getUrl());
	}
	
	public void increaseLikeNumber() {
		likeNumber++;
	}
	
	public void decreaseLikeNumber() {
		if (likeNumber > 0)
			likeNumber--;
	}

	@Override
	public int compareTo(Track o) {
		return this.getPopularity() - o.getPopularity();
	}

}
