package MojKvart.Projekt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.DogadjanjeRepository;
import MojKvart.Projekt.dao.UserRepository;
import MojKvart.Projekt.domain.Dogadjanje;
import MojKvart.Projekt.domain.KorisnikRSVP;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.rest.DogadjanjeDTO;
import MojKvart.Projekt.service.DogadjanjeService;
import MojKvart.Projekt.service.KorisnikRSVPService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.service.UserService;

@Service
public class DogadjanjeServiceJpa implements DogadjanjeService {
	
	@Autowired
	private DogadjanjeRepository dogadjanjeRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UlicaService ulicaService;
	
	@Autowired
	private KorisnikRSVPService korisnikRSVPService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Dogadjanje createDogadjanje(Dogadjanje dogadjanje) {
		return dogadjanjeRepo.save(dogadjanje);
	}

	@Override
	public List<DogadjanjeDTO> getAllPublicEvents(Long userId) {
		List<DogadjanjeDTO> list = new LinkedList<>();
		
		Optional<User> user = userRepo.findByUserId(userId);
		if (!user.isPresent())
			return null;
		
		List<Dogadjanje> tmp = dogadjanjeRepo.findAll();
		
		for (Dogadjanje dogadjanje : tmp) {
			
			Optional<User> userOther = userRepo.findByUserId(dogadjanje.getUserid());
			if (!userOther.isPresent())
				return null;
			
			@SuppressWarnings("deprecation")
			Date date = new Date(dogadjanje.getVrijeme().getYear(),
					dogadjanje.getVrijeme().getMonth(),
					dogadjanje.getVrijeme().getDate(),
					dogadjanje.getVrijeme().getHours(),
					dogadjanje.getVrijeme().getMinutes() + dogadjanje.getTrajanje());
			
			if (date.compareTo(new Date()) < 0) {
				dogadjanjeRepo.delete(dogadjanje);
			} else if (dogadjanje.getVidljivost() &&
					ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(
							ulicaService.getIdCetvrtiByStreetId(userOther.get().getStreetId()))) {
				@SuppressWarnings("deprecation")
				DogadjanjeDTO novoDogadjanje = (new DogadjanjeDTO(dogadjanje.getIdDogadjaja(), dogadjanje.getNaziv(),
						dogadjanje.getMjesto(), dogadjanje.getVrijeme().getYear() + 1900,
						dogadjanje.getVrijeme().getMonth() + 1, dogadjanje.getVrijeme().getDate(),
						dogadjanje.getVrijeme().getHours(), dogadjanje.getVrijeme().getMinutes(), dogadjanje.getTrajanje(),
						dogadjanje.getKratkiOpis(), dogadjanje.getVidljivost(), dogadjanje.getUserid(),
						userOther.get()));
				List<KorisnikRSVP> allRSVP = korisnikRSVPService.findAllByIdDogadjaja(novoDogadjanje.getIdDogadjaja());
				novoDogadjanje.setComing(korisnikRSVPService.countComing(allRSVP));
				novoDogadjanje.setNotComing(korisnikRSVPService.countNotComing(allRSVP));
				novoDogadjanje.setMaybeComing(korisnikRSVPService.countMaybeComing(allRSVP));
				
				Optional<KorisnikRSVP> userRSVP = korisnikRSVPService.findKorisnikRSVPByUserIdAndIdDogadjaja(userId, dogadjanje.getIdDogadjaja());
				if (!userRSVP.isPresent())
					novoDogadjanje.setUserReaction(-1);
				else
					novoDogadjanje.setUserReaction(userRSVP.get().getStatus());
				
				list.add(novoDogadjanje);
			}
		}
		
		Collections.sort(list, new SortByDateDogadjanjeDTO());
		
		return list;
	}
	
	// sort dogadjanja od najstarijih do najnovijih
	private static class SortByDateDogadjanjeDTO implements Comparator<DogadjanjeDTO> {
		
		@Override
		public int compare(DogadjanjeDTO o1, DogadjanjeDTO o2) {
			return o1.getVrijeme().compareTo(o2.getVrijeme());
		}
		
	}

	@Override
	public void deleteAllWithUserId(Long userId) {
		
		List<Dogadjanje> dogadjanja = dogadjanjeRepo.findAll();
		for (Dogadjanje dogadjanje : dogadjanja) {
			if (dogadjanje.getUserid().equals(userId)) {
				dogadjanjeRepo.delete(dogadjanje);
			}
		}
		
	}

	@Override
	public Dogadjanje addEvent(DogadjanjeDTO dogadjanjedto) {
		@SuppressWarnings("deprecation")
		Date date = new Date(dogadjanjedto.getYear() - 1900,
				dogadjanjedto.getMonth() - 1,
				dogadjanjedto.getDay(),
				dogadjanjedto.getHour(),
				dogadjanjedto.getMinute());
		
		if (date.compareTo(new Date()) < 0)
			return null;
		
		Dogadjanje dogadjanje = new Dogadjanje(dogadjanjedto.getNaziv(),
				dogadjanjedto.getMjesto(), date,
				dogadjanjedto.getTrajanje(), dogadjanjedto.getKratkiOpis(),
				false, dogadjanjedto.getUserId());
		
		int role = userService.getRoleFromUserId(dogadjanjedto.getUserId());
		if (role == 1)
			dogadjanje.setVidljivost(true);
		
		return dogadjanjeRepo.save(dogadjanje);
	}

	@Override
	public Optional<Dogadjanje> findByIdDogadjaja(Long idDogadjaja) {
		return dogadjanjeRepo.findByIdDogadjaja(idDogadjaja);
	}

	@Override
	public List<DogadjanjeDTO> getAllNonPublicEvents(Long userId) {
		List<DogadjanjeDTO> list = new LinkedList<>();
		
		Optional<User> user = userRepo.findByUserId(userId);
		if (!user.isPresent())
			return null;
		
//		List<Dogadjanje> tmp = dogadjanjeRepo.findAllByUserId(userId);
		List<Dogadjanje> tmp = dogadjanjeRepo.findAll();
		
		for (Dogadjanje dogadjanje : tmp) {
			
			Optional<User> userOther = userRepo.findByUserId(dogadjanje.getUserid());
			if (!userOther.isPresent())
				return null;
			
			@SuppressWarnings("deprecation")
			Date date = new Date(dogadjanje.getVrijeme().getYear(),
					dogadjanje.getVrijeme().getMonth(),
					dogadjanje.getVrijeme().getDate(),
					dogadjanje.getVrijeme().getHours(),
					dogadjanje.getVrijeme().getMinutes() + dogadjanje.getTrajanje());
			
//			if (dogadjanje.getVrijeme().compareTo(new Date()) < 0) {
			if (date.compareTo(new Date()) < 0) {
				dogadjanjeRepo.delete(dogadjanje);
			} else if (!dogadjanje.getVidljivost() &&
					ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(
							ulicaService.getIdCetvrtiByStreetId(userOther.get().getStreetId()))) {
				@SuppressWarnings("deprecation")
				DogadjanjeDTO novoDogadjanje = (new DogadjanjeDTO(dogadjanje.getIdDogadjaja(), dogadjanje.getNaziv(),
						dogadjanje.getMjesto(), dogadjanje.getVrijeme().getYear() + 1900,
						dogadjanje.getVrijeme().getMonth() + 1, dogadjanje.getVrijeme().getDate(),
						dogadjanje.getVrijeme().getHours(), dogadjanje.getVrijeme().getMinutes(), dogadjanje.getTrajanje(),
						dogadjanje.getKratkiOpis(), dogadjanje.getVidljivost(), dogadjanje.getUserid(),
						userOther.get()));
				List<KorisnikRSVP> allRSVP = korisnikRSVPService.findAllByIdDogadjaja(novoDogadjanje.getIdDogadjaja());
				novoDogadjanje.setComing(korisnikRSVPService.countComing(allRSVP));
				novoDogadjanje.setNotComing(korisnikRSVPService.countNotComing(allRSVP));
				novoDogadjanje.setMaybeComing(korisnikRSVPService.countMaybeComing(allRSVP));
				list.add(novoDogadjanje);
			}
		}
		
		Collections.sort(list, new SortByDateDogadjanjeDTO());
		
		return list;
	}

	@Override
	public DogadjanjeDTO acceptDogadjanje(Long idDogadjaja, String kratkiOpis,
			String mjesto, String naziv) {
		Optional<Dogadjanje> dogadjanje = findByIdDogadjaja(idDogadjaja);
		if (!dogadjanje.isPresent())
			return null;
		
		dogadjanje.get().setKratkiOpis(kratkiOpis);
		dogadjanje.get().setMjesto(mjesto);
		dogadjanje.get().setNaziv(naziv);
		dogadjanje.get().setVidljivost(true);
		return changeIntoDogadjanjeDTO(dogadjanjeRepo.save(dogadjanje.get()));
	}
	
	private DogadjanjeDTO changeIntoDogadjanjeDTO(Dogadjanje dogadjanje) {
		@SuppressWarnings("deprecation")
		DogadjanjeDTO novoDogadjanje = (new DogadjanjeDTO(dogadjanje.getIdDogadjaja(), dogadjanje.getNaziv(),
				dogadjanje.getMjesto(), dogadjanje.getVrijeme().getYear() + 1900,
				dogadjanje.getVrijeme().getMonth() + 1, dogadjanje.getVrijeme().getDate(),
				dogadjanje.getVrijeme().getHours(), dogadjanje.getVrijeme().getMinutes(), dogadjanje.getTrajanje(),
				dogadjanje.getKratkiOpis(), dogadjanje.getVidljivost(), dogadjanje.getUserid(),
				userService.findByUserId(dogadjanje.getUserid()).get()));
		return novoDogadjanje;
		
	}

	@Override
	public void denyDogadjanje(Long idDogadjaja) {
		
		Optional<Dogadjanje> dogadjanje = dogadjanjeRepo.findByIdDogadjaja(idDogadjaja);
		if (!dogadjanje.isPresent() ||
				dogadjanje.get().getVidljivost() == true)
			return;
		
		dogadjanjeRepo.delete(dogadjanje.get());
			
	}

	@Override
	public void deleteByIdDogadjaja(Long idDogadjaja) {
		Optional<Dogadjanje> dogadjanje = findByIdDogadjaja(idDogadjaja);
		if (!dogadjanje.isPresent())
			return;
		
		dogadjanjeRepo.delete(dogadjanje.get());
	}
	
}
