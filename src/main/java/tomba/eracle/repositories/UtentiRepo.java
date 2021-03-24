package tomba.eracle.repositories;

import org.springframework.data.repository.CrudRepository;

import tomba.eracle.entitites.Utente;

public interface UtentiRepo extends CrudRepository<Utente, Long>{
	
	public Utente findByEmailAndPsw(String email, String psw);

}
