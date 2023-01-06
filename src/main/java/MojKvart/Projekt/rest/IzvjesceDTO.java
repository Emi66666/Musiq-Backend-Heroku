package MojKvart.Projekt.rest;

import MojKvart.Projekt.domain.Tema;
import MojKvart.Projekt.domain.User;

import java.util.Date;

public class IzvjesceDTO {
	private Long idIzvjesca;
	private String sadrzaj;
	private String naslov;
	private Long userId;
	private Date datumIzvjesca;

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;

	private User user;
	private Tema tema = null;

	public IzvjesceDTO() {
		super();
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Long getIdIzvjesca() {
		return idIzvjesca;
	}

	public void setIdIzvjesca(Long idIzvjesca) {
		this.idIzvjesca = idIzvjesca;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IzvjesceDTO(Long idIzvjesca, String sadrzaj, String naslov, Long userId, int year, int month, int day,
			int hour, int minute, User user) {
		this.idIzvjesca = idIzvjesca;
		this.sadrzaj = sadrzaj;
		this.naslov = naslov;
		this.userId = userId;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.user = user;
	}

	public IzvjesceDTO(Long idIzvjesca, String sadrzaj, String naslov, Long userId, Date datumIzvjesca, User user) {
		this.idIzvjesca = idIzvjesca;
		this.sadrzaj = sadrzaj;
		this.naslov = naslov;
		this.userId = userId;
		this.datumIzvjesca = datumIzvjesca;
		this.user = user;
	}

	@Override
	public String toString() {
		return "IzvjesceDTO{" + "idIzvjesca=" + idIzvjesca + ", sadrzaj='" + sadrzaj + '\'' + ", naslov='" + naslov
				+ '\'' + ", userId=" + userId + ", datumIzvjesca=" + datumIzvjesca + ", year=" + year + ", month="
				+ month + ", day=" + day + ", hour=" + hour + ", minute=" + minute + ", user=" + user + '}';
	}
}
