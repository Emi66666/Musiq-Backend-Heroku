package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Odgovor;
import java.util.List;
import java.util.Optional;

public interface OdgovorService {
	
	Odgovor createOdgovor(Odgovor odgovor);

	List<Odgovor> getAllOdgovorByIdTeme(Long idTeme);
	
	void deleteOdgovor(Long idOdgovora);
	
	Odgovor editOdgovor(Long idOdgovora, String newSadrzaj);
	
	void deleteAllWithUserId(Long userId);
	
	void deleteAllWithIdTeme(Long idTeme);
	
	Optional<Odgovor> getOdgovorByIdOdgovora(Long idOdgovora);
	
	List<Odgovor> getAllOdgovorByUserId(Long userId);
	
	Odgovor changeOdgovor(Long idOdgovora, String sadrzaj);
	
}
