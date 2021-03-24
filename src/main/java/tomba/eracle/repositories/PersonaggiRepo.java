package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Personaggio;

public interface PersonaggiRepo extends CrudRepository<Personaggio, Long> {
	
	@Query(nativeQuery = true, value="SELECT * FROM personaggi WHERE id_utente = :id")	
	public List<Personaggio> findByIdUser(@Param("id") Long id);
	
	public Personaggio findByNominativo(String nominativo);
}
