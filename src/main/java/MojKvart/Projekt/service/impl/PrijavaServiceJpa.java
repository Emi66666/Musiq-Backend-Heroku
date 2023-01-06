package MojKvart.Projekt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MojKvart.Projekt.dao.PrijavaRepository;
import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.domain.Prijava;
import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.service.OdgovorService;
import MojKvart.Projekt.service.PrijavaService;
import MojKvart.Projekt.service.UserService;

@Service
public class PrijavaServiceJpa implements PrijavaService {
	
	@Autowired
	private PrijavaRepository prijavaRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OdgovorService odgovorService;

	@Override
	public List<Prijava> findAllByIdOdgovora(long idOdgovora) {
		return prijavaRepo.findAllByIdOdgovora(idOdgovora);
	}

	@Override
	public void deleteAllByUserId(long userId) {
		List<Prijava> list = prijavaRepo.findAll();
		
		for (Prijava prijava : list) {
			if (prijava.getUserId().equals(userId)) {
				prijavaRepo.delete(prijava);
			}
		}
	}

	@Override
	public Prijava createPrijava(Prijava prijava) {
		if (prijava.getUserId() == null || prijava.getIdOdgovora() == null)
			return null;
		
		Optional<User> user = userService.findByUserId(prijava.getUserId());
		Optional<Odgovor> odgovor = odgovorService.getOdgovorByIdOdgovora(prijava.getIdOdgovora());
		if (!user.isPresent() || !odgovor.isPresent())
			return null;
		
		Optional<Prijava> other = prijavaRepo.findByUserIdAndIdOdgovora(prijava.getUserId(), prijava.getIdOdgovora());
		if (other.isPresent())
			return null;
		
		if (prijava.getUserId().equals(odgovor.get().getUserId()))
			return null;
		
		return prijavaRepo.save(prijava);
	}

	@Override
	public void deleteAllByIdOdgovora(long idOdgovora) {
		List<Prijava> list = prijavaRepo.findAll();
		
		for (Prijava prijava : list) {
			if (prijava.getIdOdgovora().equals(idOdgovora)) {
				prijavaRepo.delete(prijava);
			}
		}
	}

	@Override
	public Optional<Prijava> findPrijavaByUserIdAndIdOdgovora(long userId, long idOdgovora) {
		return prijavaRepo.findByUserIdAndIdOdgovora(userId, idOdgovora);
	}

	@Override
	public void deleteAllForUser(long userId) {
		List<Prijava> list = prijavaRepo.findAll();
		
		for (Prijava prijava : list) {
			if (odgovorService.getOdgovorByIdOdgovora(prijava.getIdOdgovora()).get().getUserId().equals(userId)) {
				prijavaRepo.delete(prijava);
			}
		}
	}
	
}
