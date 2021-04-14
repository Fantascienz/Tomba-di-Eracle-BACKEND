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
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi ORDER BY (id_utente)")
	public List<Personaggio> getAllOrderByIdUtente();
	
	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = :razza")
	public int countRazza(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT razza FROM personaggi GROUP BY (razza) ORDER BY (razza)")
	public List<String> getAllRazzeGroupBy();
	
	@Query(nativeQuery = true, value= "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (nominativo)")
	public List<Personaggio> getAllByRazzaOrderBy(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (nominativo)")
	public List<Personaggio> getAllByRazzaAndStatoOrderBy(@Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (id)")
	public List<Personaggio> getAllByRazzaOrderById(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value ="SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (id)")
	public List<Personaggio> getAllByRazzaAndStatoOrderById(@Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (sesso)")
	public List<Personaggio> getAllByRazzaOrderBySesso(@Param("razza") String razza);
	
	@Query(nativeQuery= true, value ="SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (sesso)")
	public List<Personaggio> getAllByRazzaAndStatoOrderBySesso(@Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value= "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (rango)")
	public List<Personaggio> getAllByRazzaOrderByRango(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (rango)")
	public List<Personaggio> getAllByRazzaAndStatoOrderByRango(@Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value= "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (data_creazione)")
	public List<Personaggio> getAllByRazzaOrderByDataCreazione(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (data_creazione)")
	public List<Personaggio> getAllByRazzaAndStatoOrderByDataCreazione(@Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value= "SELECT * FROM personaggi WHERE razza = :razza ORDER BY (id_utente)")
	public List<Personaggio> getAllByRazzaOrderByIdUtente(@Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE razza = :razza AND stato = :stato ORDER BY (id_utente)")
	public List<Personaggio> getAllByRazzaAndStatoOrderByIdUtente(@Param("razza") String razza, @Param("stato") String stato);

	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (nominativo)")
	public List<Personaggio> getAllByIdUtenteOrderByNominativo(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (sesso)")
	public List<Personaggio> getAllByIdUtenteOrderBySesso(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (razza)")
	public List<Personaggio> getAllByIdUtenteOrderByRazza(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (id)")
	public List<Personaggio> getAllByIdUtenteOrderById(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (rango)")
	public List<Personaggio> getAllByIdUtenteOrderByRango(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente ORDER BY (data_creazione)")
	public List<Personaggio> getAllByIdUtenteOrderByDataCreazione(@Param("idutente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza")
	public List<Personaggio> getAllByIdUtenteAndRazza(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza ORDER BY (id)")
	public List<Personaggio> getAllByIdUtenteAndRazzaOrderById(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza ORDER BY (nominativo)")
	public List<Personaggio> getAllByIdUtenteAndRazzaOrderByNominativo(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza ORDER BY (sesso)")
	public List<Personaggio> getAllByIdUtenteAndRazzaOrderBySesso(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza ORDER BY (rango)")
	public List<Personaggio> getAllByIdUtenteAndRazzaOrderByRango(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza ORDER BY (data_creazione)")
	public List<Personaggio> getAllByIdUtenteAndRazzaOrderByDataCreazione(@Param("idutente") Long idUtente, @Param("razza") String razza);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND razza = :razza AND stato = :stato")
	public List<Personaggio> getByIdUtenteAndRazzaAndStato(@Param("idutente") Long idUtente, @Param("razza") String razza, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value = "SELECT * FROM personaggi WHERE id_utente = :idutente AND  stato = :stato")
	public List<Personaggio> getByIdUtenteAndStato(@Param("idutente") Long idUtente, @Param("stato") String stato);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = 'Umano' and id_utente = :idUtente")
	public Integer countUmanoByUtente(@Param("idUtente") Long idUtente);

	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = 'Homid' and id_utente = :idUtente")
	public Integer countHomidByUtente(@Param("idUtente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = 'Lupus' and id_utente = :idUtente")
	public Integer countLupusByUtente(@Param("idUtente") Long idUtente);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(razza) FROM personaggi WHERE razza = 'Metis' and id_utente = :idUtente")
	public Integer countMetisByUtente(@Param("idUtente") Long idUtente);
	
}
