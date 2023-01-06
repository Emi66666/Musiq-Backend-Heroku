package MojKvart.Projekt.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MojKvart.Projekt.domain.Izvjesce;
import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.domain.Prijava;
import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.IzvjesceService;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.TemaService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.service.UserService;
import MojKvart.Projekt.service.PrijavaService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("/forum")
public class TemaController {

	@Autowired
	private TemaService temaService;

	@Autowired
	private OdgovorService odgovorService;

	@Autowired
	private UserService userService;

	@Autowired
	private UlicaService ulicaService;

	@Autowired
	private PrijavaService prijavaService;

	@Autowired
	private IzvjesceService izvjesceService;

	@PostMapping("")
	public ResponseEntity<List<Tema>> getThemes(@RequestBody User user) {
		return new ResponseEntity<>(temaService.getAllTemaByCetvrt(user.getUserId()), HttpStatus.OK);
	}

	@PostMapping("/add")
	public Tema addTheme(@RequestBody OtvorenaTemaDTO temadto) {

		if (temadto.getSadrzaj() == null || temadto.getNaslov() == null)
			return null;

		if (temadto.getIdIzvjesca() != null) {

			Optional<Izvjesce> izvjesce = izvjesceService.findByIzvjesceId(temadto.getIdIzvjesca());
			if (!izvjesce.isPresent())
				return null;

			if (izvjesce.get().getDatumIzvjesca().after(new Date()))
				return null;

			if (!izvjesce.get().getNaslov().equals(temadto.getNaslov()))
				return null;

		}

		Tema tema = new Tema(temadto.getNaslov(), temadto.getUserId(), temadto.getIdIzvjesca());

		return temaService.createTema(tema, temadto.getSadrzaj());
	}

	@PostMapping("/{id}")
	public TemaDTO getTheme(@PathVariable(value = "id") long idTeme, @RequestBody User user) {
		TemaDTO tema = new TemaDTO();

		Optional<User> newUser = userService.findByUserId(user.getUserId());
		if (!newUser.isPresent())
			return null;

		Optional<Tema> existing = temaService.getTemaByIdTeme(idTeme);

		if (!existing.isPresent())
			return null;

		if (!ulicaService.getIdCetvrtiByStreetId(newUser.get().getStreetId()).equals(ulicaService
				.getIdCetvrtiByStreetId(userService.findByUserId(existing.get().getUserId()).get().getStreetId())))
			return null;

		tema.setTema(existing.get());

		tema.setOdgovori(odgovorService.getAllOdgovorByIdTeme(idTeme));

		for (OdgovorDTO odgovor : tema.getOdgovori()) {
			if (prijavaService.findPrijavaByUserIdAndIdOdgovora(user.getUserId(), odgovor.getIdOdgovora()).isPresent())
				odgovor.setReported(true);
		}

		return tema;
	}

	@PostMapping("/{id}/add")
	public Odgovor getOdgovoriOnTema(@PathVariable(value = "id") long idTeme, @RequestBody Odgovor odgovor) {

		Optional<User> user = userService.findByUserId(odgovor.getUserId());

		if (!user.isPresent())
			return null;

		Long idCetvrti = ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId());

		Optional<Tema> tema = temaService.getTemaByIdTeme(idTeme);

		if (!tema.isPresent())
			return null;

		Long idCetvrtiTema = ulicaService
				.getIdCetvrtiByStreetId(userService.findByUserId(tema.get().getUserId()).get().getStreetId());

		if (!idCetvrti.equals(idCetvrtiTema))
			return null;

		odgovor.setIdTeme(idTeme);
		odgovor.setDatumOdgovora(new Date());
		odgovor.setIdOdgovora(null);
		return odgovorService.createOdgovor(odgovor);
	}

	@PostMapping("/deleteAnswer")
	public void removeOdgovorOnTema(@RequestBody Odgovor odgovor) {
		Optional<Odgovor> noviOdgovor = odgovorService.getOdgovorByIdOdgovora(odgovor.getIdOdgovora());
		if (!noviOdgovor.isPresent())
			return;

		odgovorService.deleteOdgovor(odgovor.getIdOdgovora());
	}

	@PostMapping("/report")
	public Prijava reportOdgovor(@RequestBody Prijava prijava) {
		return prijavaService.createPrijava(prijava);
	}

	@PostMapping("/deletePost")
	public void removeTema(@RequestBody Tema tema) {
		Optional<User> user = userService.findByUserId(tema.getUserId());
		if (!user.isPresent())
			return;

		int role = userService.getRoleFromUserId(tema.getUserId());
		if (role != 1)
			return;

		Optional<Tema> otherTema = temaService.getTemaByIdTeme(tema.getIdTeme());
		if (!otherTema.isPresent())
			return;

		if (!ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(ulicaService
				.getIdCetvrtiByStreetId(userService.findByUserId(otherTema.get().getUserId()).get().getStreetId())))
			return;

		temaService.deleteByIdTeme(tema.getIdTeme());
	}

	@PostMapping("/deleteOwnPost")
	public void removeOwnTema(@RequestBody Tema tema) {
		Optional<User> user = userService.findByUserId(tema.getUserId());
		if (!user.isPresent())
			return;

		Optional<Tema> otherTema = temaService.getTemaByIdTeme(tema.getIdTeme());
		if (!otherTema.isPresent())
			return;

		if (!tema.getUserId().equals(otherTema.get().getUserId()))
			return;

		temaService.deleteByIdTeme(tema.getIdTeme());
	}
	
	@PostMapping("/changeAnswer")
	public Odgovor changeOdgovor(@RequestBody Odgovor odgovor) {
		if (odgovor.getIdOdgovora() == null || odgovor.getSadrzaj() == null ||
				odgovor.getUserId() == null)
			return null;
		
		Optional<Odgovor> optional = odgovorService.getOdgovorByIdOdgovora(odgovor.getIdOdgovora());
		if (!optional.isPresent())
			return null;
		
		if (!odgovor.getUserId().equals(optional.get().getUserId()))
			return null;
		
		return odgovorService.changeOdgovor(odgovor.getIdOdgovora(), odgovor.getSadrzaj());
	}

}
