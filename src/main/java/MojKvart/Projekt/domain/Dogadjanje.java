package MojKvart.Projekt.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "DOGADANJE")
public class Dogadjanje implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long idDogadjaja;
  private String naziv;
  private String mjesto;
  private Date vrijeme;
  private int trajanje;
  private String kratkiOpis;
  private Boolean vidljivost;
  private Long userid;


  public Dogadjanje() {}

  public Dogadjanje(String naziv, String mjesto, Date vrijeme, int trajanje,
                    String kratkiOpis, Boolean vidljivost, Long userid) {
    this.naziv = naziv;
    this.mjesto = mjesto;
    this.vrijeme = vrijeme;
    this.trajanje = trajanje;
    this.kratkiOpis = kratkiOpis;
    this.vidljivost = vidljivost;
    this.userid = userid;
  }

  public Boolean getVidljivost() {
    return vidljivost;
  }

  public void setVidljivost(Boolean vidljivost) {
    this.vidljivost = vidljivost;
  }

  public Date getVrijeme() {
    return vrijeme;
  }

  public void setVrijeme(Date vrijeme) {
    this.vrijeme = vrijeme;
  }

  public int getTrajanje() {
    return trajanje;
  }

  public void setTrajanje(int trajanje) {
    this.trajanje = trajanje;
  }

  public Long getIdDogadjaja() {
    return idDogadjaja;
  }

  public void setIdDogadjaja(Long idDogadjaja) {
    this.idDogadjaja = idDogadjaja;
  }

  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  public String getKratkiOpis() {
    return kratkiOpis;
  }

  public void setKratkiOpis(String kratkiOpis) {
    this.kratkiOpis = kratkiOpis;
  }

  public String getMjesto() {
    return mjesto;
  }

  public void setMjesto(String mjesto) {
    this.mjesto = mjesto;
  }

  public String getNaziv() {
    return naziv;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

  @Override
  public String toString() {
    return "Dogadjanje{" +
      "idDogadjanje=" + idDogadjaja +
      ", naziv='" + naziv + '\'' +
      ", vrijeme='" + vrijeme + '\'' +
      ", trajanje='" + trajanje + '\'' +
      ", kratkiOpis='" + kratkiOpis+ '\'' +
      ", korisnikId='" + userid + '\'' +
      ", vidljivost='" + vidljivost + '\'' +
      '}';
  }

}
