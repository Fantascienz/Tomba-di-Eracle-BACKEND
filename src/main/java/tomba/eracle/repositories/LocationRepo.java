package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tomba.eracle.entitites.Location;

public interface LocationRepo extends CrudRepository<Location, Long> {

	List<Location> findByMappa(String mappa);

	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_nord IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByNordNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_est IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByEstNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_sud IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findBySudNull();
	
	@Query(value = "SELECT * FROM locations WHERE id IN (SELECT id_location FROM direzioni WHERE id_location_ovest IS NULL AND tipo = 'Reame')", nativeQuery = true)
	List<Location> findByOvestNull();
	
}
