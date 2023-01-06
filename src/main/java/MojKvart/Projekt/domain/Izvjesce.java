package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "IZVJESCE")
public class Izvjesce implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idIzvjesca;
  private String sadrzaj;
  private String naslov;
  private Long userId;
  private Date datumIzvjesca;

  public Izvjesce() {}

  public Izvjesce(String sadrzaj, String naslov, Long userId, Date datumIzvjesca) {
    Assert.hasText(naslov, "Naslov mora postojati.");
    Assert.hasText(sadrzaj, "Sadržaj mora postojati.");
    this.userId = userId;
    this.sadrzaj = sadrzaj;
    this.naslov = naslov;
    this.datumIzvjesca = datumIzvjesca;
  }

  public Izvjesce( String naslov, Long userId, Date datumIzvjesca) {
    Assert.hasText(naslov, "Naslov mora postojati.");
    this.userId = userId;
    this.naslov = naslov;
    this.datumIzvjesca = datumIzvjesca;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getDatumIzvjesca() {
    return datumIzvjesca;
  }

  public void setDatumIzvjesca(Date datumIzvjesca) {
    this.datumIzvjesca = datumIzvjesca;
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

  public String getSadrzaj() {
    return sadrzaj;
  }

  public void setSadrzaj(String sadrzaj) {
    this.sadrzaj = sadrzaj;
  }

  @Override
  public String toString() {
    return "Izvjesce{" +
      "idIzvjesca=" + idIzvjesca +
      ", naslov='" + naslov + '\'' +
      ", datumIzvjesca='" + datumIzvjesca + '\'' +
      ", korisnikId='" + userId + '\'' +
      ", sadrzaj='" + sadrzaj + '\'' +
      '}';
  }

}
