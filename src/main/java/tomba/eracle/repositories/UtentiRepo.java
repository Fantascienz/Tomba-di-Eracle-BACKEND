package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Utente;

public interface UtentiRepo extends CrudRepository<Utente, Long>{
	
	public Utente findByEmailAndPsw(String email, String psw);
	
	@Query(value = "SELECT psw FROM utenti WHERE id = :id", nativeQuery = true)
	public String findPasswordByUtente(@Param("id")Long id);
	
	@Query(value = "SELECT COUNT(*) FROM personaggi WHERE id_utente = :id", nativeQuery = true)
	public int findNumeroPgUtente(@Param("id")Long id);
	
	@Query(nativeQuery = true, value = "SELECT tipo FROM utenti GROUP BY (tipo) ORDER BY (tipo)")
	public List<String> findAllTipoUtente();
	
	@Query(nativeQuery = true, value = "SELECT * FROM utenti WHERE tipo = :tipo")
	public List<Utente> findAllByTipoUtente(@Param("tipo") String tipo);
	
	@Query(nativeQuery = true, value = "SELECT * FROM utenti WHERE nominativo LIKE CONCAT('%',:nominativo,'%')")
	public List<Utente> findByNominativo(@Param("nominativo") String nominativo);
	
	@Query(nativeQuery = true, value = "SELECT * FROM utenti WHERE nominativo LIKE CONCAT('%',:nominativo,'%') AND tipo = :tipo")
	public List<Utente> findByNominativoAndTipo(@Param("nominativo") String nominativo, @Param("tipo") String tipo);
	
}
