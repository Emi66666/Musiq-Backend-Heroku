package hr.fer.drumre.musiq.login.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterTokenVerifier {

	@Value("${twitter.oauth.consumer.key}")
	private String consumerKey;

	@Value("${twitter.oauth.consumer.secret}")
	private String consumerSecret;
	
	@Autowired
	private UserService userService;

	public boolean verifyUser(String userId, String token, String tokenSecret) {
		twitter4j.User tUser;
		
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);
			builder.setOAuthAccessToken(token);
			builder.setOAuthAccessTokenSecret(tokenSecret);
			builder.setIncludeEmailEnabled(true);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();
			tUser = twitter.verifyCredentials();
		} catch (IllegalStateException | TwitterException e) {
			return false;
		}
		
		User user = userService.findUserById(userId);
		if (user == null || !user.getName().equals(tUser.getScreenName()))
			return false;
		
		return true;
	}

}
