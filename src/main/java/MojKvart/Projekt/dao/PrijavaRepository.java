package MojKvart.Projekt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Prijava;

public interface PrijavaRepository extends JpaRepository<Prijava, Long> {

	List<Prijava> findAllByIdOdgovora(long idOdgovora);
	
	Optional<Prijava> findByUserIdAndIdOdgovora(long userId, long idOdgovora);
	
}
