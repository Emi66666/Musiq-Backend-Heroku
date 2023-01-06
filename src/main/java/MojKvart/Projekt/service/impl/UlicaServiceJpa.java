package MojKvart.Projekt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.UlicaRepository;
import MojKvart.Projekt.domain.Cetvrt;
import MojKvart.Projekt.domain.Ulica;
import MojKvart.Projekt.service.UlicaService;

@Service
public class UlicaServiceJpa implements UlicaService {

	@Autowired
	private UlicaRepository ulicaRepo;

	@Override
	public Ulica createUlica(Ulica ulica) {
		return ulicaRepo.save(ulica);
	}

	@Override
	public List<Ulica> getAllUlica() {
		return ulicaRepo.findAll();
	}

	@Override
	public Long getIdCetvrtiByStreetId(Long streetId) {
		Optional<Ulica> opt = ulicaRepo.findByIdUlice(streetId);
		
		if (!opt.isPresent())
			return null;
		
		return opt.get().getIdCetvrti();
	}

	@Override
	public List<Ulica> getAllByIdCetvrti(Long idCetvrti) {
		List<Ulica> list = ulicaRepo.findAllByIdCetvrti(idCetvrti);
		Collections.sort(list, new sortByName());
		return list;
	}
	
	private static class sortByName implements Comparator<Ulica> {

		@Override
		public int compare(Ulica o1, Ulica o2) {
			return o1.getImeUlice().compareTo(o2.getImeUlice());
		}

	}

	@Override
	public Optional<Ulica> findByImeUlice(String imeUlice) {
		return ulicaRepo.findByImeUlice(imeUlice);
	}
	
}
