package MojKvart.Projekt.rest;

import java.util.Date;
import MojKvart.Projekt.domain.User;

public class DogadjanjeDTO {

	private Long idDogadjaja;
	private String naziv;
	private String mjesto;
	private Date vrijeme;

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;

	private int trajanje;
	private String kratkiOpis;
	private Boolean vidljivost;
	private Long userId;
	private User user;
	private int coming;
	private int notComing;
	private int maybeComing;

	private int userReaction;

	public DogadjanjeDTO() {

	}

	public DogadjanjeDTO(Long idDogadjaja, String naziv, String mjesto, Date vrijeme, int trajanje, String kratkiOpis,
			Boolean vidljivost, Long userId, User user) {
		super();
		this.idDogadjaja = idDogadjaja;
		this.naziv = naziv;
		this.mjesto = mjesto;
		this.vrijeme = vrijeme;
		this.trajanje = trajanje;
		this.kratkiOpis = kratkiOpis;
		this.vidljivost = vidljivost;
		this.userId = userId;
		this.user = user;
	}

	@SuppressWarnings("deprecation")
	public DogadjanjeDTO(Long idDogadjaja, String naziv, String mjesto, int year, int month, int day, int hour,
			int minute, int trajanje, String kratkiOpis, Boolean vidljivost, Long userId, User user) {
		super();
		this.idDogadjaja = idDogadjaja;
		this.naziv = naziv;
		this.mjesto = mjesto;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.trajanje = trajanje;
		this.kratkiOpis = kratkiOpis;
		this.vidljivost = vidljivost;
		this.userId = userId;
		this.user = user;
		this.vrijeme = new Date(year - 1900, month - 1, day, hour, minute);
	}

	public int getUserReaction() {
		return userReaction;
	}

	public void setUserReaction(int userReaction) {
		this.userReaction = userReaction;
	}

	public int getComing() {
		return coming;
	}

	public void setComing(int coming) {
		this.coming = coming;
	}

	public int getNotComing() {
		return notComing;
	}

	public void setNotComing(int notComing) {
		this.notComing = notComing;
	}

	public int getMaybeComing() {
		return maybeComing;
	}

	public void setMaybeComing(int maybeComing) {
		this.maybeComing = maybeComing;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public Long getIdDogadjaja() {
		return idDogadjaja;
	}

	public void setIdDogadjaja(Long idDogadjaja) {
		this.idDogadjaja = idDogadjaja;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getMjesto() {
		return mjesto;
	}

	public void setMjesto(String mjesto) {
		this.mjesto = mjesto;
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

	public String getKratkiOpis() {
		return kratkiOpis;
	}

	public void setKratkiOpis(String kratkiOpis) {
		this.kratkiOpis = kratkiOpis;
	}

	public Boolean getVidljivost() {
		return vidljivost;
	}

	public void setVidljivost(Boolean vidljivost) {
		this.vidljivost = vidljivost;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "DogadjanjeDTO [idDogadjaja=" + idDogadjaja + ", naziv=" + naziv + ", mjesto=" + mjesto + ", vrijeme="
				+ vrijeme + ", trajanje=" + trajanje + ", kratkiOpis=" + kratkiOpis + ", vidljivost=" + vidljivost
				+ ", userId=" + userId + ", user=" + user + "]";
	}

}
