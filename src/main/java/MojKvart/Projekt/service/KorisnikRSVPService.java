package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.KorisnikRSVP;
import java.util.List;
import java.util.Optional;

public interface KorisnikRSVPService {
	
	int countComing(List<KorisnikRSVP> list);
	
	int countNotComing(List<KorisnikRSVP> list);
	
	int countMaybeComing(List<KorisnikRSVP> list);
	
	List<KorisnikRSVP> findAllByIdDogadjaja(long idDogadjaja);
	
	void deleteAllByUserId(long userId);
	
	KorisnikRSVP createKorisnikRSVP(KorisnikRSVP rsvp);

	void deleteAllByIdDogadjaja(long idDogadjaja);

	Optional<KorisnikRSVP> findKorisnikRSVPByUserIdAndIdDogadjaja(long userId, long idDogadjaja);
	
}
