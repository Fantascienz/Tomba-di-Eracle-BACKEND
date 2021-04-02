package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Personaggio;
import tomba.eracle.entitites.Utente;

public interface PersonaggiRepo extends CrudRepository<Personaggio, Long> {
	
	
	public List<Personaggio> findByUtente(Utente utente);
	
	public Personaggio findByNominativo(String nominativo);
	
	public List<Personaggio> findByRazza(String razza);
	
	public List<Personaggio> findByStato(String stato);
	
	@Query(nativeQuery = true, value = "SELECT * FROM  personaggi ORDER BY (razza)")
	public List<Personaggio> getAllOrderByRazza();
	
	@Query(nativeQuery = true, value="SELECT * FROM personaggi ORDER BY (nominativo)")
	public List<Personaggio> getAllOrderByNominativo();
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi ORDER BY (sesso)")
	public List<Personaggio> getAllOrderBySesso();
	
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi ORDER BY (rango)")
	public List<Personaggio> getAllOrderByRango();
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi ORDER BY (data_creazione)")
	public List<Personaggio> getAllOrderByDataCreazione();
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato")
	public List<Personaggio> getByRazzaAndStato(@Param(value = "razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi ORDER BY (id)")
	public List<Personaggio> getAllOrderById();
	
	
	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = :razza")
	public int countRazza(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT razza FROM personaggi GROUP BY (razza) ORDER BY (razza)")
	public List<String> getAllRazzeGroupBy();
	
}
