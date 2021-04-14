package tomba.eracle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import tomba.eracle.entitites.Chirottero;

public interface ChirotteriRepo extends CrudRepository<Chirottero, Long>{

	@Query(value = "SELECT * FROM chirotteri WHERE id_destinatario = :id", nativeQuery = true)
	List<Chirottero> getRicevuti (@Param("id")Long idPersonaggio);
	
}
