package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.Util;
import hr.fer.drumre.musiq.db.mongo.users.SocialNetwork;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import hr.fer.drumre.musiq.login.twitter.TwitterService;

@Service
public class TrackRecommenderService {

	@Autowired
	TrackRepository repo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TwitterService twitterService;
	
	private Map<User, Thread> recommenderThreads = new HashMap<>();
	
	public void calculateSimilar(User user, int count, String token, String secret) {
		if (recommenderThreads.containsKey(user)) {
			recommenderThreads.get(user).interrupt();
		}
		
		Thread thread = new Thread(() -> {
			user.setRecommendedTrackIds(getSimilar(user, count, 0, token, secret));
			userService.updateUser(user);
			recommenderThreads.remove(user);
		});
		recommenderThreads.put(user, thread);
		thread.start();
	}
	
	public void awaitCalculateSimilar(User user, int count, String token, String secret) {
		if (recommenderThreads.containsKey(user)) {
			recommenderThreads.get(user).interrupt();
		}
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		Thread thread = new Thread(() -> {
			user.setRecommendedTrackIds(getSimilar(user, count, 0, token, secret));
			userService.updateUser(user);
			recommenderThreads.remove(user);
			latch.countDown();
		});
		recommenderThreads.put(user, thread);
		thread.start();
		
		try {
		    latch.await();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	
	public boolean isCalculating(User user) {
		return recommenderThreads.containsKey(user);
	}
	
	public List<String> getSimilar(User user, int count, int page) {
		return getSimilar(user, count, page, null, null);
	}
	
	public List<String> getSimilar(User user, int count, int page, String token, String secret) {
		List<Track> tracks = user.getLikedTrackIds().stream().map(id -> repo.findById(id)).filter(Optional::isPresent).map(Optional::get).toList();
		
		List<Track> friendTracks = List.of();
		if (user.getSocialNetwork() == SocialNetwork.TWITTER && token!=null && secret!=null) {
			friendTracks = twitterService.findFollowing(user.getId(), token, secret).stream()
					.map(u -> u.getLikedTrackIds())
					.flatMap(List::stream)
					.distinct()
					.map(id -> repo.findById(id))
					.filter(opt -> opt.isPresent())
					.map(opt  -> opt.get())
					.toList();
		} 
		
		return getSimilar(tracks, friendTracks, count, page).stream().map(track -> track.getId()).toList();
	}
	
	public List<Track> getSimilar(List<Track> tracks, List<Track> friendTracks, int count, int page) {
		int numTracks = Math.max(Math.min(1200, (page*count+count)*15),2500);
		
		List<Track> all = repo.findAll(PageRequest.of(0, numTracks, Sort.by(Sort.Order.desc("popularity")))).toList();
		List<TrackSimilarity> trackSimilarities = all.stream().filter(t -> t.isComplete() && !tracks.contains(t)).map(t -> {return new TrackSimilarity(t, tracks, friendTracks);}).toList();		
		List<TrackSimilarity> topK = Util.heapselect(trackSimilarities, page*count + count).subList(page*count, page*count + count);
		return topK.stream().map(ts -> ts.track).toList();
	}
	
	private class TrackSimilarity implements Comparable<TrackSimilarity>{
		public Track track;
		public double similarity;
		public TrackSimilarity(Track track, List<Track> compareTracks, List<Track> friendTracks) {
			this.track = track;
			this.similarity = 0;
			for (Track t : compareTracks) {
				this.similarity += track.similarityTo(t);
			}
			for (Track t : friendTracks) {
				this.similarity += track.similarityTo(t)/4;
			}
		}
		@Override
		public int compareTo(TrackSimilarity o) {
			return (int) (this.similarity - o.similarity);
		}
	}
	
}
