package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tomba.eracle.entitites.Personaggio;
import tomba.eracle.entitites.Utente;

public interface PersonaggiRepo extends CrudRepository<Personaggio, Long> {
	
	
	public List<Personaggio> findByUtente(Utente utente);
	
	public Personaggio findByNominativo(String nominativo);
	
	public List<Personaggio> findByRazza(String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM  personaggi ORDER BY (razza)")
	public List<Personaggio> getAllOrderByRazza();
}
