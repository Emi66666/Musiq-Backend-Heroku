package hr.fer.drumre.musiq.login.twitter;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterService {

	@Value("${twitter.oauth.consumer.key}")
	private String consumerKey;

	@Value("${twitter.oauth.consumer.secret}")
	private String consumerSecret;

	@Autowired
	private UserService userService;

	public List<User> findFollowing(String userId, String token, String secret) {
		Twitter twitter;

		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);
			builder.setOAuthAccessToken(token);
			builder.setOAuthAccessTokenSecret(secret);
			builder.setIncludeEmailEnabled(true);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();
			twitter.verifyCredentials();
		} catch (IllegalStateException | TwitterException e) {
			return new LinkedList<>();
		}

		List<User> followers = new LinkedList<>();

		try {
			long cursor = -1;
			IDs ids;

			do {
				ids = twitter.getFriendsIDs(cursor);
				cursor = ids.getNextCursor();

				for (long id : ids.getIDs()) {
					User user = userService.findUserById(Long.toString(id));
					if (user != null)
						followers.add(user);
				}
			} while (ids.hasNext());
		} catch (TwitterException e) {
			//LOGGER.info("Error while getting followers");
		}

		return followers;
	}

}
