package MojKvart.Projekt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.CetvrtRepository;
import MojKvart.Projekt.domain.Cetvrt;
import MojKvart.Projekt.service.CetvrtService;

@Service
public class CetvrtServiceJpa implements CetvrtService {
	
	@Autowired
	CetvrtRepository cetvrtRepo;

	@Override
	public List<Cetvrt> getAllCetvrti() {
		List<Cetvrt> list = cetvrtRepo.findAll();
		Collections.sort(list, new sortByName());
		return list;
	}

	private static class sortByName implements Comparator<Cetvrt> {

		@Override
		public int compare(Cetvrt o1, Cetvrt o2) {
			return o1.getImeCetvrti().compareTo(o2.getImeCetvrti());
		}

	}
	
	@Override
	public Cetvrt createCetvrt(Cetvrt cetvrt) {
		return cetvrtRepo.save(cetvrt);
	}

	@Override
	public Optional<Cetvrt> findByImeCetvrti(String imeCetvrti) {
		return cetvrtRepo.findByImeCetvrti(imeCetvrti);
	}

	@Override
	public Optional<Cetvrt> findByIdCetvrti(Long idCetvrti) {
		return cetvrtRepo.findByIdCetvrti(idCetvrti);
	}
	
}
