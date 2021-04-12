package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Messaggio;

public interface MessaggiRepo extends CrudRepository<Messaggio, Long> {

	@Query(value = "SELECT * FROM messaggi WHERE id_utente = :id", nativeQuery = true)
	List<Messaggio> getConversazione(@Param("id") Long idUtente);
	
	@Query(value = "SELECT DISTINCT(id_utente) FROM messaggi", nativeQuery = true)
	List<Long> getAllConversazioniAttive();
}
