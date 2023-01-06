package MojKvart.Projekt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Ulica;

public interface UlicaRepository extends JpaRepository<Ulica, Long> {

	Optional<Ulica> findByImeUlice(String imeUlice);
	
	Optional<Ulica> findByIdUlice(Long idUlice);
	
	List<Ulica> findAllByIdCetvrti(Long IdCetvrti);
	
}
