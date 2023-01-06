package MojKvart.Projekt.rest;

import java.util.Date;

public class OdgovorDTO {

	private Long idOdgovora;
	private Long idTeme;
	private String sadrzaj;
	private Long userId;
	private Date datumOdgovora;
	private boolean isReported;

	public OdgovorDTO(Long idOdgovora, Long idTeme, String sadrzaj, Long userId, Date datumOdgovora,
			boolean isReported) {
		super();
		this.idOdgovora = idOdgovora;
		this.idTeme = idTeme;
		this.sadrzaj = sadrzaj;
		this.userId = userId;
		this.datumOdgovora = datumOdgovora;
		this.isReported = isReported;
	}

	public Long getIdOdgovora() {
		return idOdgovora;
	}

	public void setIdOdgovora(Long idOdgovora) {
		this.idOdgovora = idOdgovora;
	}

	public Long getIdTeme() {
		return idTeme;
	}

	public void setIdTeme(Long idTeme) {
		this.idTeme = idTeme;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDatumOdgovora() {
		return datumOdgovora;
	}

	public void setDatumOdgovora(Date datumOdgovora) {
		this.datumOdgovora = datumOdgovora;
	}

	public boolean isReported() {
		return isReported;
	}

	public void setReported(boolean isReported) {
		this.isReported = isReported;
	}

}
