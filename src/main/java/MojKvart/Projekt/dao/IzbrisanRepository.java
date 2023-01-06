package MojKvart.Projekt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import MojKvart.Projekt.domain.Izbrisan;

public interface IzbrisanRepository extends JpaRepository<Izbrisan, Long> {

	Optional<Izbrisan> findByEmail(String email);

}
