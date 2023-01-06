package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Cetvrt;
import java.util.List;
import java.util.Optional;

public interface CetvrtService {
	
	List<Cetvrt> getAllCetvrti();
	
	Cetvrt createCetvrt(Cetvrt cetvrt);
	
	Optional<Cetvrt> findByImeCetvrti(String imeCetvrti);
	
	Optional<Cetvrt> findByIdCetvrti(Long idCetvrti);
	
}
