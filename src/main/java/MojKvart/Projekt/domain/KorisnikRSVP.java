package MojKvart.Projekt.domain;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "KORISNIKRSVP")
public class KorisnikRSVP implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long korisnikRSVPId;
	  private Long userId;
	  private Long idDogadjaja;
	  private int status;

	  public KorisnikRSVP() {}

	  public KorisnikRSVP(Long userId, long idDogadjaja, int status) {
	    this.userId = userId;
	    this.idDogadjaja = idDogadjaja;
	    this.status = status;
	  }

	  public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getKorisnikRSVPId() {
		return korisnikRSVPId;
	}

	public void setKorisnikRSVPId(Long korisnikRSVPId) {
		this.korisnikRSVPId = korisnikRSVPId;
	}

	public void setUserId(Long userId) {
	    this.userId = userId;
	  }

	  public Long getUserId() {
	    return userId;
	  }

	  public Long getIdDogadjaja() {
	    return idDogadjaja;
	  }

	  public void setIdDogadjaja(Long idDogadjaja) {
	    this.idDogadjaja = idDogadjaja;
	  }

	  @Override
	  public String toString() {
	    return "KorisnikRSVP{" +
	      "KorisnikId=" + userId +
	      ", idDogadjanja='" + idDogadjaja + '\'' +
	      '}';
	  }
	}
