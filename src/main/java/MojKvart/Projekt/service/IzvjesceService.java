package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.Izvjesce;

import java.util.List;
import java.util.Optional;

import MojKvart.Projekt.rest.IzvjesceDTO;

public interface IzvjesceService {

  List<Izvjesce> findBuduce();

  Izvjesce createIzvjesce(Izvjesce izvjesce);

  List<Izvjesce> getAllIzvjescaOrdered();

  public List<Izvjesce> getAllIzvjescaOrderedWithNullContent();

  Izvjesce createReport(IzvjesceDTO izvjesceDTO);

  void deleteIzvjesce(Long izvjesceId);

  Optional<Izvjesce> findByIzvjesceId(Long izvjesceId);

  void deleteAllWithUserId(Long userId);

  Optional<Izvjesce> findIzvjesceByNaslov(String naslov);
}
