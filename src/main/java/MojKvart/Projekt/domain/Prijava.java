package MojKvart.Projekt.domain;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "PRIJAVA")
public class Prijava implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long prijavaId;
	  private Long userId;
	  private Long idOdgovora;

	  public Prijava() {}

	public Prijava(Long prijavaId, Long userId, Long idOdgovora) {
		super();
		this.prijavaId = prijavaId;
		this.userId = userId;
		this.idOdgovora = idOdgovora;
	}

	public Long getPrijavaId() {
		return prijavaId;
	}

	public void setPrijavaId(Long prijavaId) {
		this.prijavaId = prijavaId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getIdOdgovora() {
		return idOdgovora;
	}

	public void setIdOdgovora(Long idOdgovora) {
		this.idOdgovora = idOdgovora;
	}

	@Override
	public String toString() {
		return "Prijava [prijavaId=" + prijavaId + ", userId=" + userId + ", idOdgovora=" + idOdgovora + "]";
	} 
	  
	}
