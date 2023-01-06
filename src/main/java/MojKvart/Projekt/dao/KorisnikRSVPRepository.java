package MojKvart.Projekt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.KorisnikRSVP;

public interface KorisnikRSVPRepository extends JpaRepository<KorisnikRSVP, Long> {

	List<KorisnikRSVP> findAllByIdDogadjaja(long idDogadjaja);
	
	List<KorisnikRSVP> findAllByUserId(long userId);
	
	Optional<KorisnikRSVP> findByUserIdAndIdDogadjaja(long userId, long idDogadjaja);
	
}
