package hr.fer.drumre.musiq.api.spotify;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.Util;
import hr.fer.drumre.musiq.api.spotify.dto.SpotifyTrack;
import hr.fer.drumre.musiq.db.mongo.tracks.AudioFeatures;
import hr.fer.drumre.musiq.db.mongo.tracks.Track;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackRepository;

@Service
public class SpotifyUpdateTracksService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpotifyUpdateTracksService.class.getName());
	
	@Autowired
	TrackRepository trackRepo;
	
	@Autowired
	SpotifyRestClient restClient;
	
	@Scheduled(fixedDelay = 600000 * 24 * 7, initialDelay = 600000 * 24 * 7) //every week TODO add initial delay
	public void updateTracks() {
		int tracksPerRequest = 50;
		long numTracks = trackRepo.count();
		for (long i = 34500; i <= numTracks; i+=tracksPerRequest) {
			if (i % 1000 == 0) Util.sleep(35000);
			LOGGER.info("Updating all tracks in database with info from Spotify {} / {}", i, numTracks);
			List<Track> tracks = trackRepo.findAll(PageRequest.of((int) (i / tracksPerRequest), tracksPerRequest)).stream()
					.filter(track -> track.getSpotify_id() != null)
					.collect(Collectors.toList());
			if (tracks.size() == 0) continue;
			List<String> trackIds = tracks.stream()
					.map(track -> track.getSpotify_id())
					.collect(Collectors.toList());
			List<SpotifyTrack> spotifyTracks = restClient.getTracks(trackIds);
			List<AudioFeatures> audioFeatures = restClient.getMultipleAudioFeatures(trackIds);
			
			if (audioFeatures==null || spotifyTracks==null) {
				//LOGGER.info("Request fail, moving on");
				continue;
			}
			
			for (int j = 0; j < tracks.size(); j++) {
				Track track = tracks.get(j);
				SpotifyTrack sTrack = spotifyTracks.get(j);
				AudioFeatures features = audioFeatures.get(j);

				track.addData(sTrack);
				track.addData(features);
				track.calculatePopularity();
				//LOGGER.info("{} {}", track.getSpotify_id(), track.getName());
				
				trackRepo.save(track);

			}

		}
		
		LOGGER.info("Finished updating tracks with info from Spotify");
	}

	
}
