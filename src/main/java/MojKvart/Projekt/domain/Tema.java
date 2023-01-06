package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "TEMA")
public class Tema implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idTeme;
  private Long idIzvjesca;
  private int brojOdgovora;
  private String naslov;
  private Long userId;
  private Date datumTeme;

  public Tema() {}

  public Tema(String naslov, Long userId, Long idIzvjesca, Date datumTeme, int brojOdgovora) {
    Assert.hasText(naslov, "Naslov mora postojati.");
    this.userId = userId;
    this.idIzvjesca = idIzvjesca;
    this.naslov = naslov;
    this.datumTeme = datumTeme;
    this.brojOdgovora = brojOdgovora;
  }

  public Tema(String naslov, Long userId, Long idIzvjesca) {
    this.userId = userId;
    this.idIzvjesca = idIzvjesca;
    this.naslov = naslov;
    this.datumTeme = new Date();
    this.brojOdgovora = 0;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getIdIzvjesca() {
    return idIzvjesca;
  }

  public void setIdIzvjesca(Long idIzvjesca) {
    this.idIzvjesca = idIzvjesca;
  }

  public String getNaslov() {
    return naslov;
  }

  public void setNaslov(String naslov) {
    this.naslov = naslov;
  }

  public Date getDatumTeme() {
    return datumTeme;
  }

  public void setDatumTeme(Date datumTeme) {
    this.datumTeme = datumTeme;
  }

  public int getBrojOdgovora() {
    return brojOdgovora;
  }

  public void setBrojOdgovora(int brojOdgovora) {
    this.brojOdgovora = brojOdgovora;
  }

  public Long getIdTeme() {
    return idTeme;
  }

  public void setIdTeme(Long idTeme) {
    this.idTeme = idTeme;
  }

  @Override
  public String toString() {
    return "Tema{" +
      "idTeme=" + idTeme +
      ", idIzvjesca=" + idIzvjesca +
      ", naslov='" + naslov + '\'' +
      ", datumTeme='" + datumTeme + '\'' +
      ", korisnikId='" + userId + '\'' +
      ", brojOdgovora='" + brojOdgovora + '\'' +
      '}';
  }

}
