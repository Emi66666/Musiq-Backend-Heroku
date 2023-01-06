package MojKvart.Projekt.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Dogadjanje;

public interface DogadjanjeRepository extends JpaRepository<Dogadjanje, Long> {
	
	Optional<Dogadjanje> findByIdDogadjaja(Long idDogadjaja);
	
}
