package MojKvart.Projekt.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MojKvart.Projekt.domain.Ulica;
import MojKvart.Projekt.service.UlicaService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("")
public class UlicaController {

	@Autowired
	private UlicaService ulicaService;
	
	@GetMapping("/changeaddress")
	public ResponseEntity<List<Ulica>> getAddressesToChange() {
		return new ResponseEntity<>(ulicaService.getAllUlica(), HttpStatus.OK);
	}
	
	@GetMapping("/register")
	public ResponseEntity<List<Ulica>> getAllAddresses() {
		return new ResponseEntity<>(ulicaService.getAllUlica(), HttpStatus.OK);
	}
}
