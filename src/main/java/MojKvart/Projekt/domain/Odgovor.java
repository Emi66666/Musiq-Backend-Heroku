package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "ODGOVOR")
public class Odgovor implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idOdgovora;
  private Long idTeme;
  private String sadrzaj;
  private Long userId;
  private Date datumOdgovora;

  public Odgovor() {}

  public Odgovor(Long userId, Date datumOdgovora, Long idTeme, String sadrzaj) {
    Assert.hasText(sadrzaj, "Sadržaj mora postojati.");
    this.userId = userId;
    this.datumOdgovora = datumOdgovora;
    this.idTeme = idTeme;
    this.sadrzaj = sadrzaj;
  }

  public Odgovor(Long userId, Long idTeme, String sadrzaj) {
    Assert.hasText(sadrzaj, "Sadržaj mora postojati.");
    this.userId = userId;
    this.datumOdgovora = new Date();
    this.idTeme = idTeme;
    this.sadrzaj = sadrzaj;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getIdTeme() {
    return idTeme;
  }

  public void setIdTeme(Long idTeme) {
    this.idTeme = idTeme;
  }

  public void setSadrzaj(String sadrzaj) {
    this.sadrzaj = sadrzaj;
  }

  public String getSadrzaj() {
    return sadrzaj;
  }

  public Date getDatumOdgovora() {
    return datumOdgovora;
  }

  public void setDatumOdgovora(Date datumOdgovora) {
    this.datumOdgovora = datumOdgovora;
  }

  public Long getIdOdgovora() {
    return idOdgovora;
  }

  public void setIdOdgovora(Long idOdgovora) {
    this.idOdgovora = idOdgovora;
  }

  @Override
  public String toString() {
    return "Tema{" +
      "idOdgovora=" + idOdgovora +
      ", idTeme=" + idTeme +
      ", sadrzaj=" + sadrzaj +
      ", datumOdgovora='" + datumOdgovora + '\'' +
      ", korisnikId='" + userId + '\'' +
      '}';
  }

}
