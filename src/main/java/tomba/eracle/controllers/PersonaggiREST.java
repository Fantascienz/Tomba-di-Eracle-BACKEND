package tomba.eracle.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Personaggio;
import tomba.eracle.entitites.Utente;
import tomba.eracle.repositories.PersonaggiRepo;

@RestController
@RequestMapping("/personaggi")
public class PersonaggiREST {

	@Autowired
	private PersonaggiRepo personaggiRepo;

	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> findByUser(@RequestBody Utente utente) {

		List<Personaggio> personaggiUtente = personaggiRepo.findByUtente(utente);

		return ResponseEntity.ok(personaggiUtente);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<Personaggio> createPg(@RequestBody Personaggio model) {
		if(!findByNominativo(model)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		model.setChirottero(false);
		model.setUmbra(false);
		personaggiRepo.save(model);

		return ResponseEntity.ok(model);
	}

	private boolean findByNominativo(Personaggio model) {
		model = personaggiRepo.findByNominativo(model.getNominativo());
		if (model != null) {
			return false;
		}
		
		return true;
	
	}

}
