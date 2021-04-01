package tomba.eracle.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		model.setTribu("Senza Tribu");
		personaggiRepo.save(model);

		return ResponseEntity.ok(model);
	}
	
	
	@CrossOrigin
	@PostMapping(path  = "/modifica", consumes = "application/json" )
	public ResponseEntity<Personaggio> modificaPersonaggio(@RequestBody Personaggio model) {
		
		System.out.println("MODIFICA");
		
		model.setDataUltimaModifica(LocalDate.now());
		
		personaggiRepo.save(model);
		
		return ResponseEntity.ok(model);
	}
	
	@CrossOrigin
	@GetMapping(path = "/filtraRazza/{razza}", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getByRazza(@PathVariable("razza") String razza) {
		
		List<Personaggio> models = personaggiRepo.findByRazza(razza);
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path = "/filtraStato/{stato}", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getByStato(@PathVariable("stato") String stato) {
		
		List<Personaggio> models = personaggiRepo.findByStato(stato);
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path = "/orderRazza", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderByRazza() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderByRazza();
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path= "/orderNominativo", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderByNominativo() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderByNominativo();
		
		return ResponseEntity.ok(models);
		
	}
	
	@CrossOrigin
	@GetMapping(path = "/orderSesso", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderBySesso() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderBySesso();
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path = "/orderRango", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderByRango() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderByRango();
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path = "/orderDataCreazione", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderByDataCreazione() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderByDataCreazione();
		
		return ResponseEntity.ok(models);
	}
	
	
	@CrossOrigin
	@GetMapping(path = "/orderId", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderById() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderById();
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/razzaAndStato", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> findByRazzaAndStato(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getByRazzaAndStato(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	
	private boolean findByNominativo(Personaggio model) {
		model = personaggiRepo.findByNominativo(model.getNominativo());
		if (model != null) {
			return false;
		}
		return true;
	
	}

}
