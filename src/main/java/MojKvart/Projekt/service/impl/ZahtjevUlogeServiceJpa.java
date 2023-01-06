package MojKvart.Projekt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MojKvart.Projekt.dao.UserRepository;
import MojKvart.Projekt.dao.ZahtjevUlogeRepository;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.domain.ZahtjevUloge;
import MojKvart.Projekt.service.TemaService;
import MojKvart.Projekt.service.DogadjanjeService;
import MojKvart.Projekt.service.IzvjesceService;
import MojKvart.Projekt.service.KorisnikRSVPService;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.PrijavaService;
import MojKvart.Projekt.service.UserService;
import MojKvart.Projekt.service.ZahtjevUlogeService;
import java.util.List;
import java.util.Optional;


@Service
public class ZahtjevUlogeServiceJpa implements ZahtjevUlogeService {

	@Autowired
	private ZahtjevUlogeRepository zahtjevUlogeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TemaService temaService;
	
	@Autowired
	private OdgovorService odgovorService;
	
	@Autowired
	private DogadjanjeService dogadjanjeService;

	@Autowired
	private KorisnikRSVPService korisnikRSVPService;
	
	@Autowired
	private IzvjesceService izvjesceService;
	
	@Autowired
	private PrijavaService prijavaService;
	
	@Autowired
	private ZahtjevUlogeService zahtjevUlogeService;
	
	@Override
	public List<ZahtjevUloge> getAllRequests() {
		return zahtjevUlogeRepo.findAll();
	}

	@Override
	public ZahtjevUloge createZahtjevUloge(ZahtjevUloge zahtjevUloge) {
		
		int previousRole = userService.getRoleFromUserId(zahtjevUloge.getUserId());
		if (previousRole == 3)
			return null;
		
		Optional<User> user = userService.findByUserId(zahtjevUloge.getUserId());
		if (!user.isPresent())
			return null;
		
		if (user.get().getRole() == zahtjevUloge.getVrstaZatrazenogRolea() ||
				user.get().getRole() == 3)
			return null;
		
		Optional<ZahtjevUloge> zahtjev = getZahtjevUlogeByUserId(zahtjevUloge.getUserId());
		if (zahtjev.isPresent()) {
			zahtjev.get().setVrstaZatrazenogRolea(zahtjevUloge.getVrstaZatrazenogRolea());
			return zahtjevUlogeRepo.save(zahtjev.get());
		}
		
		return zahtjevUlogeRepo.save(zahtjevUloge);
	}

	@Override
	public User acceptRequest(Long idZahtjeva) {
		Optional<ZahtjevUloge> request = zahtjevUlogeRepo.findByIdZahtjeva(idZahtjeva);
		
		if (!request.isPresent())
			return null;
		
		int previousRole = userService.getRoleFromUserId(request.get().getUserId());
		if (previousRole == 3)
			return null;
		
		Optional<User> user = userRepo.findByUserId(request.get().getUserId());
		
		if (!user.isPresent())
			return null;
		
		user.get().setRole(request.get().getVrstaZatrazenogRolea());
		zahtjevUlogeRepo.delete(request.get());
		
		if (request.get().getVrstaZatrazenogRolea() == 3) {
			temaService.deleteAllWithUserId(request.get().getUserId());
			odgovorService.deleteAllWithUserId(request.get().getUserId());
			dogadjanjeService.deleteAllWithUserId(request.get().getUserId());
			korisnikRSVPService.deleteAllByUserId(request.get().getUserId());
			izvjesceService.deleteAllWithUserId(request.get().getUserId());
			prijavaService.deleteAllByUserId(request.get().getUserId());
			zahtjevUlogeService.deleteZahtjevByUserId(request.get().getUserId());
		}
		
		return userRepo.save(user.get());
			
	}

	@Override
	public User denyRequest(Long idZahtjeva) {
		Optional<ZahtjevUloge> request = zahtjevUlogeRepo.findByIdZahtjeva(idZahtjeva);
		
		if (!request.isPresent())
			return null;
		
		Optional<User> user = userRepo.findByUserId(request.get().getUserId());
		
		if (!user.isPresent())
			return null;
		
		zahtjevUlogeRepo.delete(request.get());
		
		return userRepo.save(user.get());
		
	}

	@Override
	public Optional<ZahtjevUloge> getZahtjevUlogeByUserId(Long userId) {
		return zahtjevUlogeRepo.findByUserId(userId);
	}

	@Override
	public void deleteZahtjevByUserId(Long userId) {
		Optional<ZahtjevUloge> request = zahtjevUlogeRepo.findByUserId(userId);
		if (!request.isPresent())
			return;
		
		zahtjevUlogeRepo.delete(request.get());
	}
	
}
