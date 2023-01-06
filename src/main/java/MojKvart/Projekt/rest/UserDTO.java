package MojKvart.Projekt.rest;

import MojKvart.Projekt.domain.ZahtjevUloge;

public class UserDTO {

	private Long userId;
	private String name;
	private String email;
	private String surname;
	private String streetName;
	private String passwordNoHash;
	private boolean blocked;
	private int reports;
	private int role;
	private ZahtjevUloge zahtjev;

	public UserDTO() {
	}

	public UserDTO(Long userId, String name, String email, String surname, String streetName, String passwordNoHash,
			boolean blocked, int role) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.streetName = streetName;
		this.passwordNoHash = passwordNoHash;
		this.blocked = blocked;
		this.role = role;
	}

	public ZahtjevUloge getZahtjev() {
		return zahtjev;
	}

	public void setZahtjev(ZahtjevUloge zahtjev) {
		this.zahtjev = zahtjev;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getReports() {
		return reports;
	}

	public void setReports(int reports) {
		this.reports = reports;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPasswordNoHash() {
		return passwordNoHash;
	}

	public void setPasswordNoHash(String passwordNoHash) {
		this.passwordNoHash = passwordNoHash;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", email=" + email + ", surname=" + surname
				+ ", streetName=" + streetName + "]";
	}

}
