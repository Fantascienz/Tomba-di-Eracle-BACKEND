package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Direzione;

public interface DirezioniRepo extends CrudRepository<Direzione, Long>{

	@Query(value = "SELECT id_location_specchio FROM direzioni WHERE id_location = :id", nativeQuery=true)
	Long findUmbraByLocation (@Param("id") Long id);
	
	@Query(value = "SELECT * FROM direzioni WHERE id_location = :id",nativeQuery = true)
	Direzione findByIdLocation (@Param("id") Long id);
	
	@Query(value = "SELECT * FROM direzioni WHERE id_location_nord = :id OR id_location_est = :id OR id_location_sud = :id OR id_location_ovest = :id",nativeQuery = true)
	List<Direzione> findDirezioniRelative (@Param("id") Long id);
	
	@Query(value = "SELECT * FROM direzioni WHERE id_location = :id",nativeQuery = true)
	Direzione findByLocation (@Param("id") Long idLocation);
}
