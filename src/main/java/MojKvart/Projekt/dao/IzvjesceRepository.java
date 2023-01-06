package MojKvart.Projekt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.Izvjesce;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IzvjesceRepository extends JpaRepository<Izvjesce, Long> {

  List<Izvjesce> findByOrderByDatumIzvjescaDesc();

  List<Izvjesce> findByOrderByDatumIzvjescaAsc();

  Optional<Izvjesce> findByIdIzvjesca(long idIzvjesca);
  
  Optional<Izvjesce> findByNaslov(String naslov);

  List<Izvjesce> findByDatumIzvjescaGreaterThanOrderByDatumIzvjescaAsc(Date datumIzvjesca);
  List<Izvjesce> findByDatumIzvjescaLessThanEqualOrderByDatumIzvjescaDesc(Date datumIzvjesca);
  List<Izvjesce> findByDatumIzvjescaLessThanEqualOrderByDatumIzvjescaAsc(Date datumIzvjesca);

  @Query
    ("SELECT i FROM Izvjesce i " +
    "WHERE i.datumIzvjesca < :date and i.sadrzaj is not null" +
    " order by i.datumIzvjesca DESC")
  List<Izvjesce> returnReportsNotNullContentSorted(
    @Param("date") Date datum);
}
