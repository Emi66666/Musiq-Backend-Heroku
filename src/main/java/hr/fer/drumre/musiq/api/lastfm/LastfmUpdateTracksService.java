package hr.fer.drumre.musiq.api.lastfm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.api.lastfm.dto.LastfmTrack;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackRepository;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackService;

@Service
public class LastfmUpdateTracksService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LastfmUpdateTracksService.class.getName());
	
	@Autowired
	TrackRepository trackRepo;
	
	@Autowired
	TrackService trackService;
	
	@Autowired
	LastfmRestClient restClient;
	
	private long count;
	
	@Scheduled(fixedDelay = 600000 * 24 * 7, initialDelay = 600000 * 24 * 7) //every week
	public void updateTracks() {
		count = 0;
		long total = trackRepo.count();
		
		trackRepo.findAll().stream()
			.forEach(track -> {
				if (count % 50 == 0) LOGGER.info("Updating all tracks in database with info from LastFM {} / {}", count, total);
				count++;
				
				if (track.getMbid() != null) {
					LastfmTrack lTrack = restClient.getTrack(track.getMbid());
					if (lTrack != null) {
						track.addData(lTrack);
						track.calculatePopularity();
						trackService.saveTrack(track);
					}
				}
			});
		LOGGER.info("Finished updating tracks with info from LastFM");
	}
	
}
