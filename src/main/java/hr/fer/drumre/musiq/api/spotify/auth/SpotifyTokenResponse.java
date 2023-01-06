package hr.fer.drumre.musiq.api.spotify.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpotifyTokenResponse {

	@JsonProperty("access_token")
	private String token;
	
	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("expires_in")
	private int expiry;
	
}
