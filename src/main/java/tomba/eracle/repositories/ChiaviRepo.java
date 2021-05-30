package tomba.eracle.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.ChiaveLocation;

public interface ChiaviRepo extends CrudRepository<ChiaveLocation, Long>{

	@Query(value="SELECT * FROM chiavi_location WHERE id_location = :id",nativeQuery = true)
	public Optional<ChiaveLocation> findByLocation(@Param("id") Long idLocation);
}
