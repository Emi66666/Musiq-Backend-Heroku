package MojKvart.Projekt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.TemaRepository;
import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.TemaService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.service.UserService;

@Service
public class TemaServiceJpa implements TemaService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UlicaService ulicaService;

	@Autowired
	private TemaRepository temaRepo;
	
	@Autowired
	private OdgovorService odgovorService;

	@Override
	public List<Tema> getAllTemaByCetvrt(Long userId) {
		
		Optional<User> user = userService.findByUserId(userId);
		if (!user.isPresent())
			return null;
		
		Long idCetvrti = ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId());
		
		List<Tema> list = temaRepo.findAll();
		List<Tema> tmp = new LinkedList<>();
		
		for (Tema tema : list) {
			if (ulicaService.getIdCetvrtiByStreetId(userService.findByUserId(
					tema.getUserId()).get().getStreetId()).equals(idCetvrti)) {
				System.out.println(tema.getDatumTeme());
				tmp.add(tema);
			}
		}
		
		Collections.sort(tmp, new sortByDateTema());
		
		return tmp;
	}

	// sort teme od najnovije do najstarije
	private static class sortByDateTema implements Comparator<Tema> {

		@Override
		public int compare(Tema o1, Tema o2) {
			return o2.getDatumTeme().compareTo(o1.getDatumTeme());
		}
		
	}

	@Override
	public Tema createTema(Tema tema, String sadrzaj) {
		
		Optional<User> user = userService.findByUserId(tema.getUserId());
		
		if (!user.isPresent())
			return null;
		
		if (sadrzaj == null) {
			temaRepo.save(tema);
			return tema;
		}
		
		Optional<Tema> existing = temaRepo.findByNaslov(tema.getNaslov());
		
		if (existing.isPresent() &&
				ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(
						ulicaService.getIdCetvrtiByStreetId(userService.findByUserId(existing.get().getUserId()).get().getStreetId()))) {
			existing.get().setDatumTeme(null);
			return existing.get();
		}
		
		Tema novaTema = temaRepo.save(tema);
		
		odgovorService.createOdgovor(new Odgovor(tema.getUserId(), novaTema.getIdTeme(), sadrzaj));
		removeBrojOdgovora(novaTema.getIdTeme(), 1);
		
		return novaTema;
	}

	@Override
	public void deleteAllWithUserId(Long userId) {
		
		List<Tema> teme = temaRepo.findAll();
		for (Tema tema : teme) {
			if (tema.getUserId().equals(userId)) {
				temaRepo.delete(tema);
				odgovorService.deleteAllWithIdTeme(tema.getIdTeme());
			}
		}
		
	}

	@Override
	public Optional<Tema> getTemaByIdTeme(Long idTeme) {
		return temaRepo.findByIdTeme(idTeme);
	}

	@Override
	public int addBrojOdgovora(long idTeme, int brojOdgovora) {
		Optional<Tema> tema = getTemaByIdTeme(idTeme);
		if (!tema.isPresent())
			return -1;
		
		int noviBrojOdgovora = tema.get().getBrojOdgovora() + brojOdgovora;
		tema.get().setBrojOdgovora(noviBrojOdgovora);
		temaRepo.save(tema.get());
		return noviBrojOdgovora;
	}

	@Override
	public int removeBrojOdgovora(long idTeme, int brojOdgovora) {
		Optional<Tema> tema = getTemaByIdTeme(idTeme);
		if (!tema.isPresent())
			return -1;
		
		int noviBrojOdgovora = tema.get().getBrojOdgovora() - brojOdgovora;
		
		if (noviBrojOdgovora < 0)
			return -1;
		
		tema.get().setBrojOdgovora(noviBrojOdgovora);
		temaRepo.save(tema.get());
		return noviBrojOdgovora;
	}

	@Override
	public void deleteByIdTeme(Long idTeme) {
		Optional<Tema> tema = getTemaByIdTeme(idTeme);
		if (!tema.isPresent())
			return;
		
		temaRepo.delete(tema.get());
	}

	@Override
	public Optional<Tema> getTemaByNaslov(String naslov) {
		return temaRepo.findByNaslov(naslov);
	}

	@Override
	public Optional<Tema> getTemaByIdIzvjesca(Long idIzvjesca) {
		return temaRepo.findByIdIzvjesca(idIzvjesca);
	}
	
}
