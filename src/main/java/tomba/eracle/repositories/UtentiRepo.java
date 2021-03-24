package tomba.eracle.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Utente;

public interface UtentiRepo extends CrudRepository<Utente, Long>{
	
	public Utente findByEmailAndPsw(String email, String psw);
	
	@Query(value = "SELECT psw FROM utenti WHERE id = :id", nativeQuery = true)
	public String findPasswordByUtente(@Param("id")Long id);

}
