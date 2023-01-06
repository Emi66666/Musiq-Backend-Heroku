package MojKvart.Projekt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import MojKvart.Projekt.dao.UlicaRepository;
import MojKvart.Projekt.dao.UserRepository;
import MojKvart.Projekt.domain.Izbrisan;
import MojKvart.Projekt.domain.Ulica;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.TemaService;
import MojKvart.Projekt.service.DogadjanjeService;
import MojKvart.Projekt.service.IzbrisanService;
import MojKvart.Projekt.service.IzvjesceService;
import MojKvart.Projekt.service.KorisnikRSVPService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.rest.UserDTO;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.PrijavaService;
import MojKvart.Projekt.service.RequestDeniedException;
import MojKvart.Projekt.service.UserService;
import MojKvart.Projekt.service.ZahtjevUlogeService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

@Service
public class UserServiceJpa implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UlicaRepository ulicaRepo;

	@Autowired
	private UlicaService ulicaService;

	@Autowired
	private TemaService temaService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OdgovorService odgovorService;

	@Autowired
	private DogadjanjeService dogadjanjeService;

	@Autowired
	private IzbrisanService izbrisanService;

	@Autowired
	private KorisnikRSVPService korisnikRSVPService;

	@Autowired
	private IzvjesceService izvjesceService;

	@Autowired
	private PrijavaService prijavaService;

	@Autowired
	private ZahtjevUlogeService zahtjevUlogeService;

	@Override
	public User createUser(UserDTO user) {
		
		
		if (user.getEmail() == null || user.getName() == null || user.getPasswordNoHash() == null
				|| (user.getStreetName() == null && user.getRole() != 3) || user.getSurname() == null) {
			return null;
		}
			

		Optional<User> optionalUser = findByEmail(user.getEmail());

		if (optionalUser.isPresent()) {
			optionalUser.get().setEmail(null);
			return optionalUser.get();
		} else {
			Optional<Izbrisan> izbrisan = izbrisanService.findByEmail(user.getEmail());
			if (izbrisan.isPresent())
				return new User();

			User returnUser;
			Optional<Ulica> ulica = ulicaService.findByImeUlice(user.getStreetName());
			if (!ulica.isPresent() && user.getRole() == 3) {
				returnUser = new User(user.getName(), user.getEmail(), user.getSurname(),
						passwordEncoder.encode(user.getPasswordNoHash()), null, user.getRole(), false);
			} else if (!ulica.isPresent()) {
				returnUser = new User(user.getName(), user.getEmail(), user.getSurname(),
						passwordEncoder.encode(user.getPasswordNoHash()), null, user.getRole(), false);
				return returnUser;
			} else
				returnUser = changeIntoUser(user);
			
			SplittableRandom splittableRandom = new SplittableRandom();
			long userId;
			do {
				userId = splittableRandom.nextLong(Integer.MIN_VALUE, Integer.MAX_VALUE);
			} while (userRepo.findByUserId(userId).isPresent());
			
			returnUser.setUserId(userId);
			
			return userRepo.save(returnUser);
		}
	}

	public Optional<User> findByEmail(String email) {
		Assert.notNull(email, "Email must be given!");
		return userRepo.findByEmail(email);
	}

	public User loginUser(String email, String password) {

		Optional<User> optional = userRepo.findByEmail(email);
		
		if (!optional.isPresent())
			return null;

		if (optional.get().isBlocked())
			return optional.get();

		if (passwordEncoder.matches(password, optional.get().getPassword()) && !optional.get().isBlocked())
			return optional.get();

		return new User();
	}

	public User changeIntoUser(UserDTO user) {

		if (user.getStreetName() == null) {
			User newUser = new User(user.getName(), user.getEmail(), user.getSurname(),
					passwordEncoder.encode(user.getPasswordNoHash()), null);
			return newUser;
		}
		
		Optional<Ulica> ulica = ulicaRepo.findByImeUlice(user.getStreetName());
		if (!ulica.isPresent() && user.getRole() != 3)
			return null;

		User newUser = new User(user.getName(), user.getEmail(), user.getSurname(),
				passwordEncoder.encode(user.getPasswordNoHash()),
				ulica.isPresent() ? ulica.get().getIdUlice() : null,
				user.getRole(), user.isBlocked());
		return newUser;

	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		Optional<User> optional = userRepo.findByUserId(userId);

		if (optional.isPresent())
			return optional;

		return Optional.empty();
	}

	@Override
	public User changeUserAddress(UserDTO user) {

		Optional<Ulica> ulica = ulicaRepo.findByImeUlice(user.getStreetName());

		if (!ulica.isPresent())
			return null;

		Optional<User> existing = userRepo.findByUserId(user.getUserId());
		if (!existing.isPresent())
			return null;

		if (ulicaService.getIdCetvrtiByStreetId(ulica.get().getIdUlice())
				.equals(ulicaService.getIdCetvrtiByStreetId(existing.get().getStreetId()))) {
			existing.get().setStreetId(ulica.get().getIdUlice());
			return userRepo.save(existing.get());
		}

		temaService.deleteAllWithUserId(existing.get().getUserId());
		odgovorService.deleteAllWithUserId(existing.get().getUserId());
		dogadjanjeService.deleteAllWithUserId(existing.get().getUserId());
		korisnikRSVPService.deleteAllByUserId(existing.get().getUserId());
		izvjesceService.deleteAllWithUserId(existing.get().getUserId());
		prijavaService.deleteAllByUserId(existing.get().getUserId());
		zahtjevUlogeService.deleteZahtjevByUserId(existing.get().getUserId());

		existing.get().setStreetId(ulica.get().getIdUlice());
		existing.get().setRole(0);
		return userRepo.save(existing.get());
	}

	@Override
	public UserDTO changeIntoUserDTO(User user) {
		return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getSurname(),
				ulicaRepo.findById(user.getStreetId()).get().getImeUlice(), user.getPassword(), user.isBlocked(),
				user.getRole());
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public User changeRole(Long userId, int newRole) {

		Optional<User> user = userRepo.findByUserId(userId);

		if (!user.isPresent())
			return null;

		user.get().setRole(newRole);

		if (newRole == 3) {
			temaService.deleteAllWithUserId(userId);
			odgovorService.deleteAllWithUserId(userId);
			dogadjanjeService.deleteAllWithUserId(userId);
			korisnikRSVPService.deleteAllByUserId(userId);
			izvjesceService.deleteAllWithUserId(userId);
			prijavaService.deleteAllByUserId(userId);
			zahtjevUlogeService.deleteZahtjevByUserId(userId);
		}

		return userRepo.save(user.get());
	}

	@Override
	public User banUser(Long userId) {
		Optional<User> user = userRepo.findByUserId(userId);

		if (!user.isPresent())
			return null;

		user.get().setBlocked(true);
		return userRepo.save(user.get());
	}

	@Override
	public User unbanUser(Long userId) {
		Optional<User> user = userRepo.findByUserId(userId);

		if (!user.isPresent())
			return null;

		prijavaService.deleteAllForUser(userId);
		user.get().setBlocked(false);
		return userRepo.save(user.get());
	}

	@Override
	public void deleteUser(Long userId) {

		Optional<User> user = userRepo.findByUserId(userId);

		if (!user.isPresent())
			return;

		temaService.deleteAllWithUserId(userId);
		odgovorService.deleteAllWithUserId(userId);
		dogadjanjeService.deleteAllWithUserId(userId);
		izbrisanService.createIzbrisan(user.get().getEmail());
		korisnikRSVPService.deleteAllByUserId(userId);
		izvjesceService.deleteAllWithUserId(userId);
		prijavaService.deleteAllByUserId(userId);
		zahtjevUlogeService.deleteZahtjevByUserId(userId);

		userRepo.delete(user.get());
	}

	@Override
	public int getRoleFromUserId(Long userId) {
		Optional<User> user = userRepo.findByUserId(userId);
		if (!user.isPresent())
			return -1;

		return user.get().getRole();
	}

	@Override
	public List<UserDTO> sortUsers(List<UserDTO> list) {
		Collections.sort(list, new sortByRoleThenByName());

		return list;
	}

	private static class sortByRoleThenByName implements Comparator<UserDTO> {

		@Override
		public int compare(UserDTO o1, UserDTO o2) {
			if (o1.getRole() == o2.getRole())
				return o1.getName().compareTo(o2.getName());
			else
				return o1.getRole() - o2.getRole();
		}

	}

}
