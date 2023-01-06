package MojKvart.Projekt.rest;

import java.util.LinkedList;
import java.util.List;

import MojKvart.Projekt.domain.Odgovor;
import MojKvart.Projekt.domain.Tema;

public class TemaDTO {

	private Tema tema;
	private List<OdgovorDTO> odgovori;
	
	public TemaDTO() {
		odgovori = new LinkedList<>();
	}

	public TemaDTO(Tema tema, List<OdgovorDTO> odgovori) {
		super();
		this.tema = tema;
		this.odgovori = odgovori;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public List<OdgovorDTO> getOdgovori() {
		return odgovori;
	}

	public void setOdgovori(List<Odgovor> odgovori) {
		
		List<OdgovorDTO> tmp = new LinkedList<>();
		
		for (Odgovor odgovor : odgovori) {
			tmp.add(new OdgovorDTO(odgovor.getIdOdgovora(), odgovor.getIdTeme(), odgovor.getSadrzaj(), odgovor.getUserId(), odgovor.getDatumOdgovora(), false));
		}
		
		this.odgovori = tmp;
	}

	@Override
	public String toString() {
		return "TemaDTO [tema=" + tema + ", odgovori=" + odgovori + "]";
	}
	
	
}
