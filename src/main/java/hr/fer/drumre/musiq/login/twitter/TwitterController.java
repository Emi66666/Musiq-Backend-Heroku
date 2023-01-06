package hr.fer.drumre.musiq.login.twitter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import hr.fer.drumre.musiq.db.mongo.users.SocialNetwork;
import hr.fer.drumre.musiq.db.mongo.users.User;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@RestController
@CrossOrigin
public class TwitterController {

	private static final String TWITTERCALLBACKURL = "http://localhost:8080/user/twitterCallback";
	private static final String MUSIQCALLBACKURL = "http://127.0.0.1:5501/index.html";
	
	@Value("${twitter.oauth.consumer.key}")
	private String consumerKey;
	
	@Value("${twitter.oauth.consumer.secret}")
	private String consumerSecret;
	

	@Autowired
	UserService service;
	
	@GetMapping("/user/twitterLogin")
	public RedirectView getToken(HttpServletRequest request, Model model) {
		String twitterUrl;
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);
			builder.setIncludeEmailEnabled(true);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			Twitter twitter = factory.getInstance();

			RequestToken requestToken = twitter.getOAuthRequestToken(TWITTERCALLBACKURL);

			request.getSession().setAttribute("requestToken", requestToken);
			request.getSession().setAttribute("twitter", twitter);

			twitterUrl = requestToken.getAuthorizationURL();
		} catch (Exception e) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(MUSIQCALLBACKURL);
			return redirectView;
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(twitterUrl);
		return redirectView;
	}

	@GetMapping("/user/twitterCallback")
	public RedirectView twitterCallback(@RequestParam(value = "oauth_token", required = false) String oauthToken,
			@RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
			@RequestParam(value = "denied", required = false) String denied, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		if (denied != null || oauthToken == null) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(MUSIQCALLBACKURL);
			return redirectView;
		}
		
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
		
		AccessToken token = null;
		
		try {
			token = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
			request.getSession().removeAttribute("requestToken");
		} catch (Exception e) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(MUSIQCALLBACKURL);
			return redirectView;
		}
		
		String userId = Long.toString(token.getUserId());
		String email;
		// TODO: Need to find a way to save twitter
		try {
			twitter4j.User twitterUser = twitter.verifyCredentials();
			email = twitterUser.getEmail();
		} catch (IllegalStateException | TwitterException e) {
			email = null;
		}
		
		User user = new User();
		user.setId(userId);
		user.setSocialNetwork(SocialNetwork.TWITTER);
		user.setName(token.getScreenName());
		user.setEmail(email);
		
		// TODO: Given id must not be null?
		service.saveUser(user);
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(MUSIQCALLBACKURL);
		Map<String, Object> attributesMap = redirectView.getAttributesMap();
		attributesMap.put("userId", userId);
		attributesMap.put("token", token.getToken());
		attributesMap.put("secret", token.getTokenSecret());
		return redirectView;
	}

}
