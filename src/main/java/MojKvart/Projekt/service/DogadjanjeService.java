package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Dogadjanje;
import MojKvart.Projekt.rest.DogadjanjeDTO;
import java.util.List;
import java.util.Optional;

public interface DogadjanjeService {
	
	Dogadjanje createDogadjanje(Dogadjanje dogadjanje);
	
	List<DogadjanjeDTO> getAllPublicEvents(Long userId);
	
	List<DogadjanjeDTO> getAllNonPublicEvents(Long userId);
	
	void deleteAllWithUserId(Long userId);
	
	Dogadjanje addEvent(DogadjanjeDTO dogadjanjedto);
	
	Optional<Dogadjanje> findByIdDogadjaja(Long idDogadjaja);
	
	DogadjanjeDTO acceptDogadjanje(Long idDogadjaja, String kratkiOpis, String mjesto, String naslov);
	
	void denyDogadjanje(Long idDogadjaja);
	
	void deleteByIdDogadjaja(Long idDogadjaja);
	
}
