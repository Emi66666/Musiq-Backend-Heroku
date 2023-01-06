package MojKvart.Projekt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import MojKvart.Projekt.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	int countByEmail(String email);
	
	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByUserId(Long userId);
}
