package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tomba.eracle.entitites.Location;

public interface LocationRepo extends CrudRepository<Location, Long> {

	List<Location> findByMappa(String mappa);
	
	

}
