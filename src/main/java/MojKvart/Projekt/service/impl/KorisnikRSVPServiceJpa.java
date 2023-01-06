package MojKvart.Projekt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.KorisnikRSVPRepository;
import MojKvart.Projekt.domain.Dogadjanje;
import MojKvart.Projekt.domain.KorisnikRSVP;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.DogadjanjeService;
import MojKvart.Projekt.service.KorisnikRSVPService;
import MojKvart.Projekt.service.UserService;

@Service
public class KorisnikRSVPServiceJpa implements KorisnikRSVPService {

	@Autowired
	private KorisnikRSVPRepository korisnikRSVPRepo;
	
	@Autowired
	private DogadjanjeService dogadjanjeService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public int countComing(List<KorisnikRSVP> list) {
		int count = 0;
		
		for (KorisnikRSVP rsvp : list) {
			if (rsvp.getStatus() == 0)
				count++;
		}
		
		return count;
	}

	@Override
	public int countNotComing(List<KorisnikRSVP> list) {
		int count = 0;
		
		for (KorisnikRSVP rsvp : list) {
			if (rsvp.getStatus() == 1)
				count++;
		}
		
		return count;
	}

	@Override
	public int countMaybeComing(List<KorisnikRSVP> list) {
		int count = 0;
		
		for (KorisnikRSVP rsvp : list) {
			if (rsvp.getStatus() == 2)
				count++;
		}
		
		return count;
	}

	@Override
	public List<KorisnikRSVP> findAllByIdDogadjaja(long idDogadjaja) {
		return korisnikRSVPRepo.findAllByIdDogadjaja(idDogadjaja);
	}

	@Override
	public void deleteAllByUserId(long userId) {
		List<KorisnikRSVP> list = korisnikRSVPRepo.findAll();
		
		for (KorisnikRSVP rsvp : list) {
			if (rsvp.getUserId().equals(userId)) {
				korisnikRSVPRepo.delete(rsvp);
			}
		}
	}
	
	@Override
	public void deleteAllByIdDogadjaja(long idDogadjaja) {
		List<KorisnikRSVP> list = korisnikRSVPRepo.findAll();
		
		for (KorisnikRSVP rsvp : list) {
			if (rsvp.getIdDogadjaja().equals(idDogadjaja)) {
				korisnikRSVPRepo.delete(rsvp);
			}
		}
	}

	@Override
	public KorisnikRSVP createKorisnikRSVP(KorisnikRSVP rsvp) {
		
		if (rsvp.getUserId() == null || rsvp.getIdDogadjaja() == null)
			return null;
		
		Optional<User> user = userService.findByUserId(rsvp.getUserId());
		Optional<Dogadjanje> dogadjanje = dogadjanjeService.findByIdDogadjaja(rsvp.getIdDogadjaja());
		if (!user.isPresent() || !dogadjanje.isPresent() ||
				rsvp.getStatus() < 0 || rsvp.getStatus() > 2)
			return null;
		
		Optional<KorisnikRSVP> other = korisnikRSVPRepo.findByUserIdAndIdDogadjaja(rsvp.getUserId(), rsvp.getIdDogadjaja());
		if (other.isPresent()) {
			other.get().setStatus(rsvp.getStatus());
			return korisnikRSVPRepo.save(other.get());
		}
		
		return korisnikRSVPRepo.save(rsvp);
	}
	
	@Override
	public Optional<KorisnikRSVP> findKorisnikRSVPByUserIdAndIdDogadjaja(long userId, long idDogadjaja) {
		return korisnikRSVPRepo.findByUserIdAndIdDogadjaja(userId, idDogadjaja);
	}
	
}
