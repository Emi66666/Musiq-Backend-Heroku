package MojKvart.Projekt.rest;

public class OtvorenaTemaDTO {

	private Long userId;
	private String naslov;
	private Long idIzvjesca;
	private String sadrzaj;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
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

}
