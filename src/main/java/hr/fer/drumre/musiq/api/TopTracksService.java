package hr.fer.drumre.musiq.api;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.api.lastfm.LastfmRestClient;
import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrack;
import hr.fer.drumre.musiq.api.spotify.SpotifyRestClient;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrack;
import hr.fer.drumre.musiq.db.mongo.tracks.AudioFeatures;
import hr.fer.drumre.musiq.db.mongo.tracks.Track;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackRepository;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackService;

@Service
public class TopTracksService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopTracksService.class.getName());
	
	@Autowired
	TrackRepository trackRepo;
	
	@Autowired
	LastfmRestClient lastfmRestClient;
	
	@Autowired
	SpotifyRestClient spotifyRestClient;
	
	@Autowired 
	TrackService trackService;
	
	@Scheduled(fixedDelay = 600000 * 24, initialDelay = 600000 * 24)  
	public void addTopTracks() {
		LOGGER.info("Adding today's top tracks from Lastfm!");
		
		List<LastfmTrack> lTracks = lastfmRestClient.getTopTracks();
		
		if (lTracks == null) {
			LOGGER.info("No new tracks found");
			return;
		}
		
		for (LastfmTrack lTrack : lTracks) {
			
			if (trackRepo.findByMbid(lTrack.getMbid()).size() > 0) continue;
			if (trackRepo.findByName(lTrack.getName()).size() > 0) continue;
			
			List<SpotifyTrack> sTracks = spotifyRestClient.search(lTrack.getName() + " " + lTrack.getArtist().getName(), 1); 
			if (sTracks == null || sTracks.size() == 0) continue;
			
			SpotifyTrack sTrack = sTracks.get(0); 
			if (trackRepo.findByUri(sTrack.getUri()).size() > 0) continue;
			
			AudioFeatures audioFeatures = spotifyRestClient.getAudioFeatures(sTrack.getId());
			
			Track track = new Track();
			track.addData(sTrack);
			track.addData(lTrack);
			track.addData(audioFeatures);
			track.calculatePopularity();
			
			LOGGER.info("Adding {} by {} to database", track.getName(), track.getArtists().stream().map(a -> a.getName()).collect(Collectors.toList()));
			trackService.saveTrack(track);
		}
		
		LOGGER.info("Finished adding today's top tracks");
	
	}
	
}
