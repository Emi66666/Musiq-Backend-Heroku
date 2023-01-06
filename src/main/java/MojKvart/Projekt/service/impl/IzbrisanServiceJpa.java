package MojKvart.Projekt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MojKvart.Projekt.dao.IzbrisanRepository;
import MojKvart.Projekt.domain.Izbrisan;
import MojKvart.Projekt.service.IzbrisanService;
import java.util.Optional;


@Service
public class IzbrisanServiceJpa implements IzbrisanService {

	@Autowired
	private IzbrisanRepository izbrisanRepo;
	
	@Override
	public Optional<Izbrisan> findByEmail(String email) {
		return izbrisanRepo.findByEmail(email);
	}

	@Override
	public Izbrisan createIzbrisan(String email) {
		Izbrisan izbrisan = new Izbrisan(email);
		return izbrisanRepo.save(izbrisan);
	}

	@Override
	public void deleteByEmail(String email) {
		Optional<Izbrisan> izbrisan = izbrisanRepo.findByEmail(email);
		if (!izbrisan.isPresent())
			return;
		
		izbrisanRepo.delete(izbrisan.get());
	}
	
}
