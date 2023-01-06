package MojKvart.Projekt.rest;

import MojKvart.Projekt.domain.Izvjesce;
import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.IzvjesceService;
import MojKvart.Projekt.service.TemaService;
import MojKvart.Projekt.service.UlicaService;
import MojKvart.Projekt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://progifroggies-moj-kvart.herokuapp.com"})
@RequestMapping("/hall")
public class HallController {
	@Autowired
	private IzvjesceService izvjesceService;

	@Autowired
	private UserService userService;

	@Autowired
	private UlicaService ulicaService;

	@Autowired
	private TemaService temaService;

	@PostMapping("")
	public List<List<IzvjesceDTO>> getAllReports(@RequestBody User user) {
		Optional<User> userCur = userService.findByUserId(user.getUserId());

		List<Izvjesce> sortiranaIzvjesca = izvjesceService.getAllIzvjescaOrdered();
		List<IzvjesceDTO> sortiranaIzvjescaCetvrt = new ArrayList<IzvjesceDTO>();

		List<Izvjesce> buduceTeme = izvjesceService.findBuduce();
		List<IzvjesceDTO> buduceTemeCetvrt = new ArrayList<IzvjesceDTO>();

		if (TrueIfCetvrt(userCur, sortiranaIzvjesca, sortiranaIzvjescaCetvrt))
			return null;
		if (TrueIfCetvrt(userCur, buduceTeme, buduceTemeCetvrt))
			return null;

		for (IzvjesceDTO izvjesce : sortiranaIzvjescaCetvrt) {
			Optional<Tema> tema = temaService.getTemaByIdIzvjesca(izvjesce.getIdIzvjesca());
			if (tema.isPresent())
				izvjesce.setTema(tema.get());
		}

		List<List<IzvjesceDTO>> listOfLists = new ArrayList<>();
		listOfLists.add(sortiranaIzvjescaCetvrt);
		listOfLists.add(buduceTemeCetvrt);

		return listOfLists;
	}

	@SuppressWarnings("deprecation")
	private boolean TrueIfCetvrt(Optional<User> userCur, List<Izvjesce> sortiranaIzvjesca,
			List<IzvjesceDTO> sortiranaIzvjescaCetvrt) {
		for (int i = 0; i < sortiranaIzvjesca.size(); i++) {
			Izvjesce izvjesceTmp = sortiranaIzvjesca.get(i);
			Optional<User> userOther = userService.findByUserId(izvjesceTmp.getUserId());
			if (!userOther.isPresent())
				return true;

			if (ulicaService.getIdCetvrtiByStreetId(userCur.get().getStreetId())
					.equals(ulicaService.getIdCetvrtiByStreetId(userOther.get().getStreetId()))) {

				sortiranaIzvjescaCetvrt.add(new IzvjesceDTO(izvjesceTmp.getIdIzvjesca(), izvjesceTmp.getSadrzaj(),
						izvjesceTmp.getNaslov(), izvjesceTmp.getUserId(),
						izvjesceTmp.getDatumIzvjesca().getYear() + 1900, izvjesceTmp.getDatumIzvjesca().getMonth() + 1,
						izvjesceTmp.getDatumIzvjesca().getDate(), izvjesceTmp.getDatumIzvjesca().getHours(),
						izvjesceTmp.getDatumIzvjesca().getMinutes(), userOther.get()));
			}
		}
		return false;
	}

	@PostMapping("/add")
	public IzvjesceDTO addReport(@RequestBody Izvjesce izvjesce) {
		Optional<User> user = userService.findByUserId(izvjesce.getUserId());

		if (!user.isPresent())
			return null;

		if (izvjesce.getSadrzaj() == null) {
			return null;
		}

		if (izvjesce.getDatumIzvjesca().after(new Date())) {
			return new IzvjesceDTO();
		}

		int role = userService.getRoleFromUserId(user.get().getUserId());
		if (role != 2)
			return null;

		Optional<Izvjesce> otherIzvjesce = izvjesceService.findIzvjesceByNaslov(izvjesce.getNaslov());
		if (otherIzvjesce.isPresent()) {
			IzvjesceDTO izvjesceDTO = new IzvjesceDTO(otherIzvjesce.get().getIdIzvjesca(),
					otherIzvjesce.get().getSadrzaj(), otherIzvjesce.get().getNaslov(), otherIzvjesce.get().getUserId(),
					null, user.get());
			return izvjesceDTO;
		}

		izvjesce = izvjesceService.createIzvjesce(izvjesce);

		IzvjesceDTO izvjesceDTO = new IzvjesceDTO(izvjesce.getIdIzvjesca(), izvjesce.getSadrzaj(), izvjesce.getNaslov(),
				izvjesce.getUserId(), izvjesce.getDatumIzvjesca(), user.get());

		Optional<Tema> tema = temaService.getTemaByNaslov(izvjesce.getNaslov());

		if (tema.isPresent()) {
			tema.get().setIdIzvjesca(izvjesce.getIdIzvjesca());
			temaService.createTema(tema.get(), null);
		}

		return izvjesceDTO;

	}

	@PostMapping("/topic")
	public IzvjesceDTO addBuducaTema(@RequestBody Izvjesce izvjesce) {
		Optional<User> user = userService.findByUserId(izvjesce.getUserId());

		if (izvjesce.getSadrzaj() != null) {
			return null;
		}
		if (!user.isPresent()) {
			return null;
		}
		if (izvjesce.getDatumIzvjesca().before(new Date())) {
			return new IzvjesceDTO();
		}

		int role = userService.getRoleFromUserId(user.get().getUserId());
		if (role != 2)
			return null;

		Optional<Izvjesce> otherIzvjesce = izvjesceService.findIzvjesceByNaslov(izvjesce.getNaslov());
		if (otherIzvjesce.isPresent()) {
			IzvjesceDTO izvjesceDTO = new IzvjesceDTO(otherIzvjesce.get().getIdIzvjesca(),
					otherIzvjesce.get().getSadrzaj(), otherIzvjesce.get().getNaslov(), otherIzvjesce.get().getUserId(),
					null, user.get());
			return izvjesceDTO;
		}

		// provjera ima li bivsih buducih dogadjaja koje treba izbrisati
		// to jest, izvjesca sa sadrzajem null
		List<Izvjesce> svaIzvjesca = izvjesceService.getAllIzvjescaOrderedWithNullContent();
		for (Izvjesce izvjesceUnutarListe : svaIzvjesca) {
			if (izvjesceUnutarListe.getSadrzaj() == null) {
				izvjesceService.deleteIzvjesce(izvjesceUnutarListe.getIdIzvjesca());
			}
		}

		izvjesce = izvjesceService.createIzvjesce(izvjesce);

		IzvjesceDTO izvjesceDTO = new IzvjesceDTO(izvjesce.getIdIzvjesca(), null, izvjesce.getNaslov(),
				izvjesce.getUserId(), izvjesce.getDatumIzvjesca(), user.get());

		return izvjesceDTO;

	}

	@PostMapping("/deleteReport")
	public void removeIzvjesce(@RequestBody Izvjesce izvjesce) {
		Optional<User> user = userService.findByUserId(izvjesce.getUserId());
		if (!user.isPresent())
			return;

		int role = userService.getRoleFromUserId(izvjesce.getUserId());
		if (role != 1)
			return;

		Optional<Izvjesce> otherIzvjesce = izvjesceService.findByIzvjesceId(izvjesce.getIdIzvjesca());
		if (!otherIzvjesce.isPresent())
			return;

		if (!ulicaService.getIdCetvrtiByStreetId(user.get().getStreetId()).equals(ulicaService
				.getIdCetvrtiByStreetId(userService.findByUserId(otherIzvjesce.get().getUserId()).get().getStreetId())))
			return;

		izvjesceService.deleteIzvjesce(izvjesce.getIdIzvjesca());
	}

	@PostMapping("/deleteOwnReport")
	public void removeOwnIzvjesce(@RequestBody Izvjesce izvjesce) {
		Optional<User> user = userService.findByUserId(izvjesce.getUserId());
		if (!user.isPresent())
			return;

		Optional<Izvjesce> izvjesceToDelete = izvjesceService.findByIzvjesceId(izvjesce.getIdIzvjesca());
		if (!izvjesceToDelete.isPresent())
			return;

		if (!izvjesce.getUserId().equals(izvjesceToDelete.get().getUserId())) {
			return;
		}

		izvjesceService.deleteIzvjesce(izvjesce.getIdIzvjesca());

	}

}
