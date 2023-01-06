package MojKvart.Projekt.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "IZBRISAN")
public class Izbrisan implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long izbrisanId;
  private String email;

  public Izbrisan() {}

  public Izbrisan(String email) {
    Assert.hasText(email, "Email mora postojati.");
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getIzbrisanId() {
	return izbrisanId;
  }

  public void setIzbrisanId(Long izbrisanId) {
	this.izbrisanId = izbrisanId;
	}

@Override
  public String toString() {
    return "Izbrisan{" +
      ", email='" + email + '\'' +
      '}';
  }

}

