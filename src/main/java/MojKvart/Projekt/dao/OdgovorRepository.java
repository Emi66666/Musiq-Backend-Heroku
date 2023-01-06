package MojKvart.Projekt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Odgovor;

public interface OdgovorRepository extends JpaRepository<Odgovor, Long> {

	List<Odgovor> findAllByIdTeme(Long idTeme);
	
	Optional<Odgovor> findByIdOdgovora(Long idOdgovora);
	
	List<Odgovor> findAllByUserId(Long userId);
	
}
