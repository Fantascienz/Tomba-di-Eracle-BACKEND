package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Location;

public interface LocationRepo extends CrudRepository<Location, Long> {
	@Query(value="SELECT * FROM locations WHERE mappa = :mappa AND (tipo = 'Umbra' OR tipo = 'Reame')",nativeQuery=true)
	List<Location> findByMappa(@Param("mappa")String mappa);
	
	@Query(value="SELECT * FROM locations WHERE id IN (SELECT ultima_location FROM personaggi WHERE id = :id)",nativeQuery=true)
	Location findUltimaLocationPg (@Param("id") Long idPersonaggio);
	
	@Query(value = "SELECT mappa FROM locations WHERE id = :id" ,nativeQuery=true)
	String findMappa(@Param("id")Long id);
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_sublocation FROM stanze WHERE id_location = :id)",nativeQuery = true)
	List<Location> findStanzeByLocation(@Param("id") Long idLocation);

	@Query(value = "SELECT * FROM locations WHERE mappa = 'Esterna' AND tipo = :tipo", nativeQuery = true)
	List<Location> findEsterneByTipo(@Param("tipo")String tipo);
	
	@Query(value = "SELECT * FROM locations WHERE id >= 1 AND id <= 288", nativeQuery = true)
	List<Location> findMacroLocations ();
	
	@Query(value = "SELECT * FROM locations ORDER BY id ASC",nativeQuery = true)
	List<Location> getAllLocations ();
	
	@Query(value="SELECT * FROM locations WHERE id = :id + (1000 * :idCella) or id = :id + (1000 * :idCella) + 48",nativeQuery=true)
	List<Location> findMidLocationsBySuperLocation (@Param("id") Long idSuperLocation,@Param("idCella")int idCella);
	
	@Query(value="SELECT * FROM locations WHERE id = :id + (10000 * :idCella) or id >= :id + (10000 * :idCella) + 48",nativeQuery=true)
	List<Location> findInnerLocationsBySuperLocation (@Param("id") Long idSuperLocation,@Param("idCella")int idCella);
	
	@Query(value="SELECT * FROM locations WHERE id = :id + 100000",nativeQuery=true) //stanza umbra extra ha + 200000
	List<Location> findStanzeLocationsBySuperLocation (@Param("id") Long idSuperLocation);
}
