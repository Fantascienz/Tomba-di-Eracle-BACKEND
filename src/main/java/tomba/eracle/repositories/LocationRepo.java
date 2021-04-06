package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Location;

public interface LocationRepo extends CrudRepository<Location, Long> {

	List<Location> findByMappa(String mappa);
	
	@Query(value="SELECT * FROM locations WHERE id IN (SELECT ultima_location FROM personaggi WHERE id = :id)",nativeQuery=true)
	Location findUltimaLocationPg (@Param("id") Long idPersonaggio);
	
	@Query(value = "SELECT mappa FROM locations WHERE id = :id" ,nativeQuery=true)
	String findMappa(@Param("id")Long id);
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_sublocation FROM stanze WHERE id_location = :id)",nativeQuery = true)
	List<Location> findStanzeByLocation(@Param("id") Long idLocation);

	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_nord IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByNordNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_est IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByEstNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_sud IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findBySudNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_ovest IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByOvestNull();
	
	@Query(value = "SELECT * FROM locations WHERE mappa = 'Esterna' AND tipo = :tipo", nativeQuery = true)
	List<Location> findEsterneByTipo(@Param("tipo")String tipo);
}
