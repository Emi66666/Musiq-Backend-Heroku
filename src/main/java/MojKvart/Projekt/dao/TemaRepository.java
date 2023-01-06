package MojKvart.Projekt.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

	Optional<Tema> findByNaslov(String naslov);
	
	Optional<Tema> findByIdTeme(Long idTeme);
	
	Optional<Tema> findByIdIzvjesca(Long idIzvjesca);
	
}
