package MojKvart.Projekt.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MojKvart.Projekt.domain.ZahtjevUloge;

public interface ZahtjevUlogeRepository extends JpaRepository<ZahtjevUloge, Long> {

	Optional<ZahtjevUloge> findByIdZahtjeva(Long idZahtjeva);
	
	Optional<ZahtjevUloge> findByUserId(Long userId);
	
}
