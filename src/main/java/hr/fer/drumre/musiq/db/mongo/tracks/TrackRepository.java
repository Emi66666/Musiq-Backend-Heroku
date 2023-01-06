package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TrackRepository extends MongoRepository<Track, String>{
	
	List<Track> findByMbid(String mbid);
	List<Track> findByName(String name);
	List<Track> findByUri(String uri);
	
	List<Track> findByNameExists(boolean exists);
	
	@Query(" {name : { '$regex' : /?0/i } } ")
	List<Track> searchName(String name);
	
}
