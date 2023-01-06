package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Tema;
import java.util.List;
import java.util.Optional;

public interface TemaService {
	
	Tema createTema(Tema tema, String sadrzaj);

	List<Tema> getAllTemaByCetvrt(Long userId);
	
	void deleteAllWithUserId(Long userId);
	
	Optional<Tema> getTemaByIdTeme(Long idTeme);
	
	int addBrojOdgovora(long idTeme, int brojOdgovora);
	
	int removeBrojOdgovora(long idTeme, int brojOdgovora);
	
	void deleteByIdTeme(Long idTeme);
	
	Optional<Tema> getTemaByNaslov(String naslov);
	
	Optional<Tema> getTemaByIdIzvjesca(Long idIzvjesca);
	
}
