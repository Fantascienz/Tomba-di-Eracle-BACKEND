package tomba.eracle.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@CrossOrigin
	@GetMapping(produces = "application/json")
	public List<Personaggio> getAllPersonaggi () {
		return (List<Personaggio>) personaggiRepo.findAll();
	}
	
	@CrossOrigin
	@PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> findByUser(@RequestBody Utente utente) {

		List<Personaggio> personaggiUtente = personaggiRepo.findByUtente(utente);

		return ResponseEntity.ok(personaggiUtente);
	}

	@CrossOrigin
	@PostMapping(consumes = "application/json")
	public ResponseEntity<Personaggio> createPg(@RequestBody Personaggio model) {
		if(!findByNominativo(model)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		model.setDataCreazione(LocalDate.now());
		model.setChirottero(false);
		model.setUmbra(false);
		personaggiRepo.save(model);

		return ResponseEntity.ok(model);
	}
	
	
	@CrossOrigin
	@PostMapping(path  = "/modifica", consumes = "application/json" )
	public ResponseEntity<Personaggio> modificaPersonaggio(@RequestBody Personaggio model) {
		
		model.setDataUltimaModifica(LocalDate.now());
		
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
