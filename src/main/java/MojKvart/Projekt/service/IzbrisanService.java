package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Izbrisan;
import java.util.Optional;

public interface IzbrisanService {

	Optional<Izbrisan> findByEmail(String email);
	
	Izbrisan createIzbrisan(String email);
	
	void deleteByEmail(String email);
}
