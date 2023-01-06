package hr.fer.drumre.musiq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import hr.fer.drumre.musiq.api.TopTracksService;
import hr.fer.drumre.musiq.api.lastfm.LastfmUpdateTracksService;
import hr.fer.drumre.musiq.api.spotify.SpotifyUpdateTracksService;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackRepository;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackService;
import hr.fer.drumre.musiq.login.facebook.FacebookTokenVerifier;
import hr.fer.drumre.musiq.login.twitter.TwitterService;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class MusiqMain implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(MusiqMain.class.getName());

	@Autowired
	SpotifyUpdateTracksService spotifyUpdateService;

	@Autowired
	LastfmUpdateTracksService lastfmUpdateService;

	@Autowired
	TopTracksService topTracksService;

	@Autowired
	FacebookTokenVerifier fbTokenVerifier;

	@Autowired
	TrackService trackService;
	
	@Autowired
	DbOperations dbops;
	
	@Autowired
	TrackRepository repo;

	@Autowired
	TwitterService twitterService;


	public static void main(String[] args) {
		SpringApplication.run(MusiqMain.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Started");
	}

}
