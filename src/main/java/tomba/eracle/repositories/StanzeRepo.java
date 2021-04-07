package tomba.eracle.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Stanza;

public interface StanzeRepo extends CrudRepository<Stanza, Long>{

	@Query(value = "SELECT * FROM stanze WHERE id_sublocation = :id" ,nativeQuery = true)
	public Stanza findStanzaBySubLocation (@Param("id") Long idLocation);
}
