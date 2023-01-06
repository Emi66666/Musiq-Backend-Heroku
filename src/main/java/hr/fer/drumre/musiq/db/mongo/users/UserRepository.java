package hr.fer.drumre.musiq.db.mongo.users;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

	List<User> findByName(String name);
	List<User> findByEmail(String email);
	
}
