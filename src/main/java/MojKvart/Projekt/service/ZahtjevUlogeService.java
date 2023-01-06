package MojKvart.Projekt.service;

import MojKvart.Projekt.domain.User;
import MojKvart.Projekt.domain.ZahtjevUloge;
import java.util.List;
import java.util.Optional;

public interface ZahtjevUlogeService {

	List<ZahtjevUloge> getAllRequests();
	
	ZahtjevUloge createZahtjevUloge(ZahtjevUloge zahtjevUloge);
	
	User acceptRequest(Long idZahtjeva);
	
	User denyRequest(Long idZahtjeva);
	
	Optional<ZahtjevUloge> getZahtjevUlogeByUserId(Long userId);
	
	void deleteZahtjevByUserId(Long userId);
	
}
