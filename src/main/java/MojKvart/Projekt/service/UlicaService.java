package MojKvart.Projekt.service;

import java.util.List;
import java.util.Optional;

import MojKvart.Projekt.domain.Ulica;

public interface UlicaService {

	Ulica createUlica(Ulica ulica);
	
	List<Ulica> getAllUlica();
	
	Long getIdCetvrtiByStreetId(Long streetId);
	
	List<Ulica> getAllByIdCetvrti(Long idCetvrti);
	
	Optional<Ulica> findByImeUlice(String imeUlice);
	
}
