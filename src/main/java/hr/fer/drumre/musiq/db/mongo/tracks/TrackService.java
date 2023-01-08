package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.api.lastfm.LastfmRestClient;
import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrack;
import hr.fer.drumre.musiq.api.spotify.SpotifyRestClient;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrack;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;

@Service
public class TrackService {
	
	@Autowired
	TrackRepository repo;
	
	@Autowired
	SpotifyRestClient spotify;
	
	@Autowired
	LastfmRestClient lastfm;
	
	@Autowired
	UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackService.class.getName());

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
		List<Track> result = repo.searchName(query);
		if (result.size() == 0) {
			LOGGER.info("Searching Spotify for {}", query);
			List<SpotifyTrack> sTracks = spotify.search(query, 10);
			LOGGER.info("Found {} potential new tracks", sTracks.size());
			for (SpotifyTrack sTrack : sTracks) {
				List<LastfmTrack> tracks = lastfm.search(sTrack.getName(), 1);
				if (tracks == null || tracks.size() == 0) continue;
				LastfmTrack lTrack = tracks.get(0);
				AudioFeatures af = spotify.getAudioFeatures(sTrack.getId());
				
				Track t = new Track();
				t.addData(lTrack);
				t.addData(sTrack);
				t.addData(af);
				repo.save(t);
				result.add(t);
				
				LOGGER.info("Adding {} to our database", t.getName());
			}
		}
		if (result.size()>60) return result.subList(0, 60);
		else return result;
	}
	
	public void updateTrack(Track track) {
		repo.save(track);
	}
	
	public List<Track> getLiked(int count, int page, String userId) {
		List<Track> likedTracks = new LinkedList<>();
		
		User user = userService.findUserById(userId);
		if (user == null)
			return likedTracks;
		
		List<String> allLikedTracksIds = user.getLikedTrackIds();
		
		int fromIndex = count * page;
		if (fromIndex < 0 || fromIndex >= allLikedTracksIds.size())
			return likedTracks;
		
		int toIndex = fromIndex + count;
		if (toIndex > allLikedTracksIds.size())
			toIndex = allLikedTracksIds.size();
		
		for (String trackId : user.getLikedTrackIds().subList(fromIndex, toIndex)) {
			Optional<Track> track = repo.findById(trackId);
			if (!track.isEmpty())
				likedTracks.add(track.get());
		}
		
		return likedTracks;
	}
	
}
