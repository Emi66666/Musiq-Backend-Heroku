package MojKvart.Projekt.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MojKvart.Projekt.domain.Dogadjanje;
import MojKvart.Projekt.domain.KorisnikRSVP;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.DogadjanjeService;
import MojKvart.Projekt.service.KorisnikRSVPService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.service.UserService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("/events")
public class DogadjanjeController {

	@Autowired
	private DogadjanjeService dogadjanjeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private KorisnikRSVPService korisnikRSVPService;
	
	@Autowired
	private UlicaService ulicaService;
	
	@PostMapping("")
	public List<DogadjanjeDTO> getAllPublicEvents(@RequestBody User user) {
		return dogadjanjeService.getAllPublicEvents(user.getUserId());
	}
	
	@PostMapping("/add")
	public DogadjanjeDTO addEvent (@RequestBody DogadjanjeDTO dogadjanjedto) {
		
		Optional<User> user = userService.findByUserId(dogadjanjedto.getUserId());
		if (!user.isPresent())
			return null;
		dogadjanjedto.setUser(user.get());
		
		Dogadjanje dogadjanje = dogadjanjeService.addEvent(dogadjanjedto);
		
		if (dogadjanje == null) {
			dogadjanjedto.setIdDogadjaja(null);
			return dogadjanjedto;
		}
			
		dogadjanjedto.setIdDogadjaja(dogadjanje.getIdDogadjaja());
		return dogadjanjedto;
	}
	
	@PostMapping("/rsvp")
	public KorisnikRSVP createKorisnikRSVP(@RequestBody KorisnikRSVP rsvp) {
		return korisnikRSVPService.createKorisnikRSVP(rsvp);
	}
	
	@PostMapping("/suggestions")
	public List<DogadjanjeDTO> getAllNonPublicEvents(@RequestBody User user) {
		return dogadjanjeService.getAllNonPublicEvents(user.getUserId());
	}
	
	@PostMapping("/suggestions/accept")
	public DogadjanjeDTO acceptEvent(@RequestBody Dogadjanje dogadjanje) {
		return dogadjanjeService.acceptDogadjanje(dogadjanje.getIdDogadjaja(), dogadjanje.getKratkiOpis(),
				dogadjanje.getMjesto(), dogadjanje.getNaziv());
	}
	
	@PostMapping("/suggestions/delete")
	public void denyEvent(@RequestBody Dogadjanje dogadjanje) {
		dogadjanjeService.denyDogadjanje(dogadjanje.getIdDogadjaja());
	}
	
	@PostMapping("/deleteEvent")
	public void removeDogadjanje(@RequestBody Dogadjanje dogadjanje) {
		Optional<User> user = userService.findByUserId(dogadjanje.getUserid());
		if (!user.isPresent())
			return;
		
		int role = userService.getRoleFromUserId(dogadjanje.getUserid());
		if (role != 1)
			return;
		
		Optional<Dogadjanje> otherDogadjanje = dogadjanjeService.findByIdDogadjaja(dogadjanje.getIdDogadjaja());
		if (!otherDogadjanje.isPresent())
			return;
		
		if (!ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(
				ulicaService.getIdCetvrtiByStreetId(userService.findByUserId(otherDogadjanje.get().getUserid()).get().getStreetId())))
			return;
		
		dogadjanjeService.deleteByIdDogadjaja(dogadjanje.getIdDogadjaja());
	}
	
	@PostMapping("/deleteOwnEvent")
	public void removeOwnDogadjanje(@RequestBody Dogadjanje dogadjanje) {
		Optional<User> user = userService.findByUserId(dogadjanje.getUserid());
		if (!user.isPresent())
			return;
		
		Optional<Dogadjanje> otherDogadjanje = dogadjanjeService.findByIdDogadjaja(dogadjanje.getIdDogadjaja());
		if (!otherDogadjanje.isPresent())
			return;
		
		if (!dogadjanje.getUserid().equals(otherDogadjanje.get().getUserid()))
			return;
		
		dogadjanjeService.deleteByIdDogadjaja(dogadjanje.getIdDogadjaja());
	}
	
}
