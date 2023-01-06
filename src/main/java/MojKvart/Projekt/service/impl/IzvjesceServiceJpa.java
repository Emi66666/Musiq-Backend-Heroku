package MojKvart.Projekt.service.impl;

import MojKvart.Projekt.dao.IzvjesceRepository;
import MojKvart.Projekt.dao.TemaRepository;
import MojKvart.Projekt.domain.Izvjesce;
import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.rest.IzvjesceDTO;
import MojKvart.Projekt.service.IzvjesceService;
import MojKvart.Projekt.service.TemaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IzvjesceServiceJpa implements IzvjesceService {

  @Autowired
  private IzvjesceRepository izvjesceRepo;
  
  @Autowired
  private TemaService temaService;
  
  @Autowired
  private TemaRepository temaRepo;

  @Override
  public Izvjesce createIzvjesce(Izvjesce izvjesce) {
    return izvjesceRepo.save(izvjesce);
  }

  @Override
  public void deleteIzvjesce(Long idIzvjesca) {
    Optional<Izvjesce> izvjesce = izvjesceRepo.findByIdIzvjesca(idIzvjesca);

    if (!izvjesce.isPresent())
      return;
    
    Optional<Tema> tema = temaService.getTemaByIdIzvjesca(idIzvjesca);
    if (tema.isPresent()) {
    	tema.get().setIdIzvjesca(null);
    	temaRepo.save(tema.get());
    }

    izvjesceRepo.delete(izvjesce.get());

  }

  @Override
  public List<Izvjesce> getAllIzvjescaOrdered() {
    List<Izvjesce> list = izvjesceRepo.returnReportsNotNullContentSorted(new Date());

    return list;
  }

  @Override
  public List<Izvjesce> getAllIzvjescaOrderedWithNullContent() {
    List<Izvjesce> list = izvjesceRepo.findByDatumIzvjescaLessThanEqualOrderByDatumIzvjescaAsc(new Date());

    return list;
  }

  @Override
  public List<Izvjesce> findBuduce() {
    List<Izvjesce> list = izvjesceRepo.findByDatumIzvjescaGreaterThanOrderByDatumIzvjescaAsc(new Date());
    return list;
  }

  @Override
  public Izvjesce createReport(IzvjesceDTO izvjesceDTO) {
    @SuppressWarnings("deprecation")
	Date date = new Date(izvjesceDTO.getYear() - 1900,
      izvjesceDTO.getMonth(),
      izvjesceDTO.getDay(),
      izvjesceDTO.getHour(),
      izvjesceDTO.getMinute());

    if (date.compareTo(new Date()) < 0)
      return null;

    Izvjesce izvjesce = new Izvjesce(izvjesceDTO.getNaslov(),
      izvjesceDTO.getSadrzaj(), izvjesceDTO.getUserId(), date);
    return izvjesceRepo.save(izvjesce);
  }

  @Override
  public Optional<Izvjesce> findByIzvjesceId(Long izvjesceId) {
    Optional<Izvjesce> optional = izvjesceRepo.findByIdIzvjesca(izvjesceId);

    if (optional.isPresent())
      return optional;

    return Optional.empty();
  }
  
  @Override
  public void deleteAllWithUserId(Long userId) {
	  List<Izvjesce> izvjesca = izvjesceRepo.findAll();
	  for (Izvjesce izvjesce : izvjesca) {
		  if (izvjesce.getUserId().equals(userId)) {
			  Optional<Tema> tema = temaService.getTemaByIdIzvjesca(izvjesce.getIdIzvjesca());
			    if (tema.isPresent()) {
			    	tema.get().setIdIzvjesca(null);
			    	temaRepo.save(tema.get());
			    }
			  
			  izvjesceRepo.delete(izvjesce);
		  }
	  }
  }

@Override
public Optional<Izvjesce> findIzvjesceByNaslov(String naslov) {
	return izvjesceRepo.findByNaslov(naslov);
}
}
