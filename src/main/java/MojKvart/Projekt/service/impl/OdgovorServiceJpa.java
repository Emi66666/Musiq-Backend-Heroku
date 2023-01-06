package MojKvart.Projekt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.OdgovorRepository;
import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.PrijavaService;
import MojKvart.Projekt.service.TemaService;

@Service
public class OdgovorServiceJpa implements OdgovorService {

	@Autowired
	private OdgovorRepository odgovorRepo;

	@Autowired
	private TemaService temaService;

	@Autowired
	private PrijavaService prijavaService;

	@Override
	public Odgovor createOdgovor(Odgovor odgovor) {
		Optional<Tema> tema = temaService.getTemaByIdTeme(odgovor.getIdTeme());
		if (!tema.isPresent())
			return null;
		
		temaService.addBrojOdgovora(odgovor.getIdTeme(), 1);
		return odgovorRepo.save(odgovor);
	}

	@Override
	public List<Odgovor> getAllOdgovorByIdTeme(Long idTeme) {
		List<Odgovor> list = odgovorRepo.findAllByIdTeme(idTeme);

		Collections.sort(list, new SortByDateOdgovori());

		return list;
	}

	// sort odgovora od najstarijih do najnovijih
	private static class SortByDateOdgovori implements Comparator<Odgovor> {

		@Override
		public int compare(Odgovor o1, Odgovor o2) {
			return o1.getDatumOdgovora().compareTo(o2.getDatumOdgovora());
		}

	}

	@Override
	public void deleteOdgovor(Long idOdgovora) {
		Optional<Odgovor> odgovor = odgovorRepo.findByIdOdgovora(idOdgovora);

		if (!odgovor.isPresent())
			return;

		temaService.removeBrojOdgovora(odgovor.get().getIdTeme(), 1);
		prijavaService.deleteAllByIdOdgovora(idOdgovora);
		odgovorRepo.delete(odgovor.get());

	}

	@Override
	public Odgovor editOdgovor(Long idOdgovora, String newSadrzaj) {
		Optional<Odgovor> odgovor = odgovorRepo.findByIdOdgovora(idOdgovora);

		if (!odgovor.isPresent())
			return null;

		odgovor.get().setSadrzaj(newSadrzaj);

		return odgovorRepo.save(odgovor.get());
	}

	@Override
	public void deleteAllWithUserId(Long userId) {

		List<Odgovor> odgovori = odgovorRepo.findAll();
		for (Odgovor odgovor : odgovori) {
			if (odgovor.getUserId().equals(userId)) {
				temaService.removeBrojOdgovora(odgovor.getIdTeme(), 1);
				prijavaService.deleteAllByIdOdgovora(odgovor.getIdOdgovora());

				odgovorRepo.delete(odgovor);
			}
		}
	}

	@Override
	public void deleteAllWithIdTeme(Long idTeme) {
		int brojOdgovora = 0;

		List<Odgovor> odgovori = odgovorRepo.findAll();
		for (Odgovor odgovor : odgovori) {
			if (odgovor.getIdTeme().equals(idTeme)) {
				brojOdgovora++;
				prijavaService.deleteAllByIdOdgovora(odgovor.getIdOdgovora());
				odgovorRepo.delete(odgovor);
			}
		}

		temaService.removeBrojOdgovora(idTeme, brojOdgovora);
	}

	@Override
	public Optional<Odgovor> getOdgovorByIdOdgovora(Long idOdgovora) {
		return odgovorRepo.findByIdOdgovora(idOdgovora);
	}

	@Override
	public List<Odgovor> getAllOdgovorByUserId(Long userId) {
		return odgovorRepo.findAllByUserId(userId);
	}

	@Override
	public Odgovor changeOdgovor(Long idOdgovora, String sadrzaj) {
		Optional<Odgovor> odgovor = odgovorRepo.findByIdOdgovora(idOdgovora);
		if (!odgovor.isPresent())
			return null;
		
		odgovor.get().setSadrzaj(sadrzaj);
		return odgovorRepo.save(odgovor.get());
	}

}
