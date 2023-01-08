package hr.fer.drumre.musiq.rest;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.drumre.musiq.db.mongo.tracks.Track;
import hr.fer.drumre.musiq.db.mongo.tracks.TrackService;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.login.Authenticator;
import hr.fer.drumre.musiq.services.TrackRecommenderService;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin
public class TrackController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackController.class.getName());

	@Autowired
	TrackService service;

	@Autowired
	TrackRecommenderService recommender;

	@Autowired
	UserService userService;

	@Autowired
	Authenticator auth;

	@GetMapping("/track/popular")
	public List<Track> getPopular(@RequestParam(required = false) int count, @RequestParam(required = false) int page) {
		if (count == 0)
			count = 10;
		List<Track> popularTracks = service.getPopular(count, page);
		return popularTracks;
	}

	@GetMapping("/track/hot")
	public List<Track> getHot(@RequestParam(required = false) int count, @RequestParam(required = false) int page) {
		if (count == 0)
			count = 10;
		List<Track> popularTracks = service.getHot(count, page);
		return popularTracks;
	}

	@GetMapping("/track/recommended")
	public List<Track> getRecommended(@RequestParam(required = false) int count,
			@RequestParam(required = false) int page, @RequestParam String id, @RequestParam String token,
			@RequestParam(required = false) String secret) {

		if (count == 0)
			count = 10;

		User user = auth.authenticate(id, token, secret);
		if (user == null)
			return null;

		int requiredSize = page * count + count;

		List<String> recommendedTrackIds = user.getRecommendedTrackIds();
		if (recommendedTrackIds == null || recommendedTrackIds.size() == 0
				|| recommendedTrackIds.size() < requiredSize) {
			recommender.awaitCalculateSimilar(user, requiredSize, token, secret);
			recommendedTrackIds = user.getRecommendedTrackIds();
		}

		List<Track> recommendedTracks = recommendedTrackIds.stream().map(tid -> service.getTrack(tid)).toList();
		return recommendedTracks.subList(page * count, page * count + count);
	}

	@GetMapping("/track/{id}")
	public ResponseEntity<Track> getTrack(@PathVariable(value = "id") String id) {
		Track track = service.getTrack(id);
		if (track == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return ResponseEntity.ok(track);
	}

	@GetMapping("/track/search")
	public List<Track> search(@RequestParam String query) {
		LOGGER.info("Searching database for {}", query);
		List<Track> tracks = service.search(query);
		tracks.sort((t1, t2) -> t1.getSpotifyPopularity() - t2.getSpotifyPopularity());
		LOGGER.info("Found {} results for {}", tracks.size(), query);
		return tracks;
	}

	@PostMapping("/track/{id}/like")
	public boolean likeTrack(@PathVariable(value = "id") String songId,
			@RequestBody UserIdAndTokenAndSecret idAndTokenAndSecret) {
		String userId = idAndTokenAndSecret.id;
		String token = idAndTokenAndSecret.token;
		String tokenSecret = idAndTokenAndSecret.secret;

		User user = auth.authenticate(userId, token, tokenSecret);
		if (user == null)
			return false;

		Track track = service.getTrack(songId);
		if (track == null)
			return false;

		if (!user.getLikedTrackIds().contains(songId)) {
			user.addLikedTrackId(songId);
			track.increaseLikeNumber();
			userService.updateUser(user);
			service.updateTrack(track);
			recommender.calculateSimilar(user, 99, token, tokenSecret);
		}
		return true;
	}

	@PostMapping("/track/{id}/dislike")
	public boolean dislikeTrack(@PathVariable(value = "id") String songId,
			@RequestBody UserIdAndTokenAndSecret idAndTokenAndSecret) {
		String userId = idAndTokenAndSecret.id;
		String token = idAndTokenAndSecret.token;
		String tokenSecret = idAndTokenAndSecret.secret;

		User user = auth.authenticate(userId, token, tokenSecret);
		if (user == null)
			return false;

		Track track = service.getTrack(songId);
		if (track == null)
			return false;

		if (user.getLikedTrackIds().contains(songId)) {
			user.removeLikedTrackId(songId);
			track.decreaseLikeNumber();
			userService.updateUser(user);
			service.updateTrack(track);
			recommender.calculateSimilar(user, 99, token, tokenSecret);
		}

		return true;
	}

	@GetMapping("/track/liked")
	public List<Track> getLiked(@RequestParam(required = false) int count, @RequestParam(required = false) int page,
			@RequestParam String id, @RequestParam String token, @RequestParam(required = false) String secret) {

		User user = auth.authenticate(id, token, secret);
		if (user == null)
			return new LinkedList<>();

		List<Track> likedTracks = service.getLiked(count, page, id);
		return likedTracks;
	}

	@Data
	@NoArgsConstructor
	public static class UserIdAndTokenAndSecret {
		public String id;
		public String token;
		public String secret;
	}

}
