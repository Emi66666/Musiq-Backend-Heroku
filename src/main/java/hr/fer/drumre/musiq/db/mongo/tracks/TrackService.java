package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.api.spotify.SpotifyRestClient;

@Service
public class TrackService {
	
	@Autowired
	TrackRepository repo;
	
	@Autowired
	SpotifyRestClient spotifyRestClient;

	public long totalTracks() {
		return repo.count();
	}

	public List<Track> getPopular(int count, int page) {
		return repo.findAll(PageRequest.of(page, count, Sort.by(Sort.Order.desc("popularity")))).toList();
	}
	
	public List<Track> getHot(int count, int page) {
		return repo.findAll(PageRequest.of(page, count, Sort.by(Sort.Order.desc("spotifyPopularity")))).toList();
	}
	
	public Track getTrack(String id) {
		Optional<Track> track = repo.findById(id);
		if (track.isEmpty())
			return null;
		return track.get();
	}
	
	public List<Track> search(String query) {
		return repo.searchName(query);
	}
	
	public void updateTrack(Track track) {
		repo.save(track);
	}
	
	
	
}
