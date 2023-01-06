package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "CETVRT")
public class Cetvrt implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idCetvrti;
  private String imeCetvrti;

  public Cetvrt() {}

  public Cetvrt(String imeCetvrti) {
    Assert.hasText(imeCetvrti, "Ime četvrti mora postojati.");
    this.imeCetvrti = imeCetvrti;
  }

  public Long getIdCetvrti() {
    return idCetvrti;
  }

  public void setIdCetvrti(Long idCetvrti) {
    this.idCetvrti = idCetvrti;
  }

  public String getImeCetvrti() {
    return imeCetvrti;
  }

  public void setImeCetvrti(String imeCetvrti) {
    this.imeCetvrti = imeCetvrti;
  }

  @Override
  public String toString() {
    return "Cetvrt{" +
      "idCetvrti=" + idCetvrti +
      ", ime='" + imeCetvrti + '\'' +
      '}';
  }

}
