package hr.fer.drumre.musiq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.db.mongo.tracks.Track;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackRepository;

@Service
public class DbOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbOperations.class.getName());
	
	@Autowired
	TrackRepository trackRepo;
	
	public void updateTracks() {
		int tracksPerRequest = 50;
		long numTracks = trackRepo.count();
		for (long i = 58400; i <= numTracks; i+=tracksPerRequest) {
			LOGGER.info("Updating all tracks {} / {}", i, numTracks);
			List<Track> tracks = trackRepo.findAll(PageRequest.of((int) (i / tracksPerRequest), tracksPerRequest)).toList();
			
			for (int j = 0; j < tracks.size(); j++) {
				Track t = tracks.get(j);
				t.calculatePopularity();
				trackRepo.save(t);
			}

		}
	}

	
}
