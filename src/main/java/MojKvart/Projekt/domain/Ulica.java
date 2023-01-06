package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "ULICA")
public class Ulica implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idUlice;
  private String imeUlice;
  private Long idCetvrti;

  public Ulica() {}

  public Ulica(String imeUlice, Long idCetvrti) {
    Assert.hasText(imeUlice, "Ime ulice mora postojati.");
    this.imeUlice = imeUlice;
    this.idCetvrti = idCetvrti;
  }

  public Long getIdCetvrti() {
    return idCetvrti;
  }

  public void setIdCetvrti(Long idCetvrti) {
    this.idCetvrti = idCetvrti;
  }

  public Long getIdUlice() {
    return idUlice;
  }

  public void setIdUlice(Long idUlice) {
    this.idUlice = idUlice;
  }

  public String getImeUlice() {
    return imeUlice;
  }

  public void setImeUlice(String imeUlice) {
    this.imeUlice = imeUlice;
  }

  @Override
  public String toString() {
    return "Ulica{" +
      "idUlice=" + idUlice +
      ", ime='" + imeUlice + '\'' +
      ", idCetvrti='" + idCetvrti + '\'' +
      '}';
  }

}
