package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Prijava;
import java.util.List;
import java.util.Optional;

public interface PrijavaService {
	
	List<Prijava> findAllByIdOdgovora(long idOdgovora);
	
	void deleteAllByUserId(long userId);
	
	Prijava createPrijava(Prijava prijava);

	void deleteAllByIdOdgovora(long idOdgovora);

	Optional<Prijava> findPrijavaByUserIdAndIdOdgovora(long userId, long idOdgovora);
	
	void deleteAllForUser(long userId);
	
}
