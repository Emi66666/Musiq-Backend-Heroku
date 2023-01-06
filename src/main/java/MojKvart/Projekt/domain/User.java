package MojKvart.Projekt.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "KORISNIK")
public class User implements Serializable {
	

	
	
  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long userId;
  private String name;
  private String email;
  private String surname;
  private String password;
  private int role;
  private Long idUlice;
  private boolean blocked;

  public User() {}

  public User(String name, String email, String surname, String password, Long streetId, int role, boolean blocked) {
    Assert.hasText(name, "Ime mora postojati.");
    Assert.hasText(email, "Email mora postojati.");
    this.name = name;
    this.email = email;
    this.surname = surname;
    this.password = password;
    this.role = role;
    this.idUlice = streetId;
    this.blocked = blocked;
  }
  public User(String name, String email, String surname, String password, Long streetId) {
	  Assert.hasText(name, "Ime mora postojati.");
    this.name = name;
    this.email = email;
    this.surname = surname;
    this.password = password;
    this.idUlice = streetId;
    this.role = 0;
    this.blocked = false;
  }

  public String getEmail() {
    return email;
  }

  public int getRole() {
    return role;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getSurname() {
    return surname;
  }

  public Long getStreetId() {
    return idUlice;
  }

  public Long getUserId() {
    return userId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public void setStreetId(Long streetId) {
    this.idUlice = streetId;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public boolean isBlocked() {
	return blocked;
  }

  public void setBlocked(boolean blocked) {
	this.blocked = blocked;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
  

  @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (blocked ? 1231 : 1237);
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + ((idUlice == null) ? 0 : idUlice.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((password == null) ? 0 : password.hashCode());
	result = prime * result + role;
	result = prime * result + ((surname == null) ? 0 : surname.hashCode());
	result = prime * result + ((userId == null) ? 0 : userId.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	User other = (User) obj;
	if (blocked != other.blocked)
		return false;
	if (email == null) {
		if (other.email != null)
			return false;
	} else if (!email.equals(other.email))
		return false;
	if (idUlice == null) {
		if (other.idUlice != null)
			return false;
	} else if (!idUlice.equals(other.idUlice))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (password == null) {
		if (other.password != null)
			return false;
	} else if (!password.equals(other.password))
		return false;
	if (role != other.role)
		return false;
	if (surname == null) {
		if (other.surname != null)
			return false;
	} else if (!surname.equals(other.surname))
		return false;
	if (userId == null) {
		if (other.userId != null)
			return false;
	} else if (!userId.equals(other.userId))
		return false;
	return true;
}

@Override
  public String toString() {
    return "User{" +
      "id=" + userId +
      ", name='" + name + '\'' +
      ", surname='" + surname + '\'' +
      ", email='" + email + '\'' +
      ", role='" + role + '\'' +
      ", password='" + password + '\'' +
      ", streetId='" + idUlice + '\'' +
      '}';
  }

}

