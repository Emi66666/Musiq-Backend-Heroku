package MojKvart.Projekt.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Cetvrt;

public interface CetvrtRepository extends JpaRepository<Cetvrt, Long> {

	Optional<Cetvrt> findByImeCetvrti(String imeCetvrti);
	
	Optional<Cetvrt> findByIdCetvrti(Long idCetvrti);
	
}
