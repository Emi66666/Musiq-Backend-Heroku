package MojKvart.Projekt.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.service.OdgovorService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("")
public class OdgovorController {

	@Autowired
	private OdgovorService odgovorService;

	@PostMapping("/deleteOwnMessage")
	public void deleteOdgovor(@RequestBody Odgovor odgovor) {
		Optional<Odgovor> noviOdgovor = odgovorService.getOdgovorByIdOdgovora(odgovor.getIdOdgovora());
		if (!noviOdgovor.isPresent())
			return;

		odgovorService.deleteOdgovor(odgovor.getIdOdgovora());
	}

	@PostMapping("/editOwnMessage")
	public Odgovor editOdgovor(@RequestBody Odgovor odgovor) {
		Optional<Odgovor> noviOdgovor = odgovorService.getOdgovorByIdOdgovora(odgovor.getIdOdgovora());
		if (!noviOdgovor.isPresent())
			return null;

		return odgovorService.editOdgovor(odgovor.getIdOdgovora(), odgovor.getSadrzaj());
	}
}
