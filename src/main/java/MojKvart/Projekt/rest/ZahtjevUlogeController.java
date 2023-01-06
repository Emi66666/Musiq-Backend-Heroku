package MojKvart.Projekt.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.domain.ZahtjevUloge;
import MojKvart.Projekt.service.UserService;
import MojKvart.Projekt.service.ZahtjevUlogeService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("")
public class ZahtjevUlogeController {
	
	@Autowired
	private ZahtjevUlogeService zahtjevUlogeService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/checkRequests")
	public List<ZahtjevUloge> getAllRequests() {
		return zahtjevUlogeService.getAllRequests();
	}
	
	@PostMapping("/checkRequests/accept")
	public User acceptRequest(@RequestBody ZahtjevUloge request) {
		Optional<User> user = userService.findByUserId(request.getUserId());
		if (!user.isPresent())
			return null;
		
		int role = userService.getRoleFromUserId(request.getUserId());
		if (role != 3)
			return null;
		
		return zahtjevUlogeService.acceptRequest(request.getIdZahtjeva());
	}
	
	@PostMapping("/checkRequests/deny")
	public User denyRequest(@RequestBody ZahtjevUloge request) {
		return zahtjevUlogeService.denyRequest(request.getIdZahtjeva());
	}
	
	@PostMapping("/request")
	public ZahtjevUloge findRequest(@RequestBody ZahtjevUloge request) {
		
		Optional<User> user = userService.findByUserId(request.getUserId());
		if (!user.isPresent())
			return null;
		
		Optional<ZahtjevUloge> zahtjev = zahtjevUlogeService.getZahtjevUlogeByUserId(request.getUserId());
		
		if (zahtjev.isPresent())
			return zahtjev.get();
		return request;
	}
	
	@PostMapping("/request/add")
	public ZahtjevUloge addRequest(@RequestBody ZahtjevUloge request) {
		return zahtjevUlogeService.createZahtjevUloge(request);
	}
	
}
