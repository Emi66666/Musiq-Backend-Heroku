package hr.fer.drumre.musiq.db.mongo.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	public long totalUsers() {
		return repo.count();
	}
	
	public void saveUser(User user) {
		if (repo.findById(user.getId()).isEmpty()) {
			repo.save(user);
		}
	}
	
	public void updateUser(User user) {
		repo.save(user);
	}
	
	public User findUserById(String id) {
		Optional<User> user = repo.findById(id);
		if (user.isEmpty())
			return null;
		return user.get();
	}
	
	public double averageLiked() {
		return repo.findAll().stream()
			.mapToInt(user -> user.getLikedTrackIds().size())
			.average()
			.getAsDouble();
	}
}
