package MojKvart.Projekt.rest;

import java.util.List;
import java.util.*;
import java.util.Optional;

import MojKvart.Projekt.domain.*;
import MojKvart.Projekt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UlicaService ulicaService;

	@Autowired
	private CetvrtService cetvrtService;

	@Autowired
	private ZahtjevUlogeService zahtjevUlogeService;

	@Autowired
	private OdgovorService odgovorService;

	@Autowired
	private PrijavaService prijavaService;
	
//	@Autowired
//	private TemaService temaService;
//	
//	@Autowired
//	private DogadjanjeService dogadjanjeService;
//	
//	@Autowired
//	private IzvjesceService izvjesceService;
//	
//	@Autowired
//	private IzbrisanService izbrisanService;

	@PostMapping("/register")
	public User registerUser(@RequestBody UserDTO user) {
		return userService.createUser(user);
	}

	@PostMapping("/login")
	public User loginUser(@RequestBody UserDTO user) {
		
		User existing = userService.loginUser(user.getEmail(), user.getPasswordNoHash());
		
		if (existing == null) {
			return new User();
		} else if (existing.isBlocked()) {
			User blockedUser = new User();
			blockedUser.setBlocked(true);
			return blockedUser;
		} else {
			return existing;
		}
	}

	@PostMapping("/logoutUser")
	public void logout() {

	}

	@PostMapping("/profil")
	public UserDTO showUser(@RequestBody User user) {
		Optional<User> returnUser = userService.findByUserId(user.getUserId());

		if (!returnUser.isPresent())
			return new UserDTO();

		if (returnUser.get().getRole() == 3) {
			UserDTO userDTO = new UserDTO(returnUser.get().getUserId(), returnUser.get().getName(),
					returnUser.get().getEmail(), returnUser.get().getSurname(), "", "", false, 3);
			return userDTO;
		}

		UserDTO returnUserDTO = userService.changeIntoUserDTO(returnUser.get());
		Optional<ZahtjevUloge> zahtjev = zahtjevUlogeService.getZahtjevUlogeByUserId(returnUserDTO.getUserId());
		if (zahtjev.isPresent())
			returnUserDTO.setZahtjev(zahtjev.get());

		return returnUserDTO;
	}

	@PostMapping("/changeaddress")
	public User changeUserAddress(@RequestBody UserDTO user) {
		User returnUser = userService.changeUserAddress(user);

		if (returnUser == null)
			return new User();

		return returnUser;
	}

	// funkcije za administratore

	@GetMapping("/neighborhoods")
	public List<Cetvrt> getAllCetvrti() {
		return cetvrtService.getAllCetvrti();
	}

	@PostMapping("/neighborhoods/add")
	public Cetvrt addNewCetvrt(@RequestBody Cetvrt cetvrt) {
		Optional<Cetvrt> existing = cetvrtService.findByImeCetvrti(cetvrt.getImeCetvrti());
		if (existing.isPresent())
			return new Cetvrt();

		Cetvrt returnCetvrt = cetvrtService.createCetvrt(cetvrt);
		return returnCetvrt;

	}

	@GetMapping("/neighborhoods/{id}")
	public List<Ulica> getAllUlicaByIdCetvrti(@PathVariable(value = "id") long idCetvrti) {

		return ulicaService.getAllByIdCetvrti(idCetvrti);
	}

	@PostMapping("/neighborhoods/{id}/add")
	public Ulica addUlica(@PathVariable(value = "id") long idCetvrti, @RequestBody Ulica ulica) {
		Optional<Ulica> existing = ulicaService.findByImeUlice(ulica.getImeUlice());
		if (existing.isPresent())
			return new Ulica();

		Optional<Cetvrt> existingCetvrt = cetvrtService.findByIdCetvrti(idCetvrti);
		if (!existingCetvrt.isPresent())
			return null;

		return ulicaService.createUlica(new Ulica(ulica.getImeUlice(), idCetvrti));
	}

	@GetMapping("/users")
	public List<UserDTO> getAllUsers() {
		List<User> users = userService.getAllUsers();

		List<UserDTO> usersDTO = new LinkedList<>();

		for (User user : users) {

			if (user.getRole() == 3) {
				UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getSurname(), "",
						"", false, 3);
				usersDTO.add(userDTO);
				continue;
			}

			UserDTO userDTO = userService.changeIntoUserDTO(user);

			int reportCount = 0;
			List<Odgovor> odgovori = odgovorService.getAllOdgovorByUserId(userDTO.getUserId());

			for (Odgovor odgovor : odgovori)
				reportCount += prijavaService.findAllByIdOdgovora(odgovor.getIdOdgovora()).size();

			userDTO.setReports(reportCount);
			usersDTO.add(userDTO);
		}

		return userService.sortUsers(usersDTO);
	}

	@PostMapping("/users/changeRole")
	public User changeUserRole(@RequestBody User user) {
		Optional<User> admin = userService.findByUserId(user.getStreetId());
		if (!admin.isPresent())
			return null;

		int role = userService.getRoleFromUserId(user.getUserId());
		if (role == 3)
			return null;

		return userService.changeRole(user.getUserId(), user.getRole());
	}

	@PostMapping("/users/ban")
	public User banUser(@RequestBody User user) {
		return userService.banUser(user.getUserId());
	}

	@PostMapping("/users/unban")
	public User unbanUser(@RequestBody User user) {
		return userService.unbanUser(user.getUserId());
	}

	@PostMapping("/users/delete")
	public void deleteUser(@RequestBody User user) {
		userService.deleteUser(user.getUserId());
	}

	// koristi se za pocetno punjenje baze ako se ista resetira
//	@GetMapping("/napunibazu")
//	public void napuniBazu() {
//		userService.createUser(new UserDTO(1L, "Admin", "admin.adminic@fer.hr", "Adminić", null, "passAdmin",
//				false, 3));
//	}
}
