package hr.fer.drumre.musiq.db.mongo.users;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("users")
public class User {

	@Id
	private String id;
	private SocialNetwork socialNetwork;
	private String name;
	private String email;
	private List<String> likedTrackIds = new LinkedList<>();
	
	public void addLikedTrackId(String id) {
		if (!likedTrackIds.contains(id))
			likedTrackIds.add(id);
	}
	
	public void removeLikedTrackId(String id) {
		if (likedTrackIds.contains(id))
			likedTrackIds.remove(id);
	}
	
	public List<String> recommendedTrackIds;
	
}
