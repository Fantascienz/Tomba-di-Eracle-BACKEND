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
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/personaggi")
public class PersonaggiREST {

	@Autowired
	private PersonaggiRepo personaggiRepo;
	
	@Autowired
	private UtentiRepo utentiRepo;
	
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
	public ResponseEntity<Utente> createPg(@RequestBody Personaggio model) {
		System.out.println(model.getRazza());
		if(!findByNominativo(model)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}

		model.setDataCreazione(LocalDate.now());
		if(model.getNomeGarou() != null) {
			model.setChirottero(false);
			model.setUmbra(false);
		}
		
		model.setTribu("Senza Tribu");
		model.setUltimaLocation((long) 68);
		personaggiRepo.save(model);
		
		Utente utente = utentiRepo.findById(model.getUtente().getId()).get();
		utente.setContatoreUmani(personaggiRepo.countUmanoByUtente(utente.getId()));
		utente.setContatoreHomid(personaggiRepo.countHomidByUtente(utente.getId()));
		utente.setContatoreLupus(personaggiRepo.countLupusByUtente(utente.getId()));
		utente.setContatoreMetis(personaggiRepo.countMetisByUtente(utente.getId()));
		return ResponseEntity.ok(utente);
	}
	
	
	@CrossOrigin
	@PostMapping(path  = "/modifica", consumes = "application/json" )
	public ResponseEntity<Personaggio> modificaPersonaggio(@RequestBody Personaggio model) {
		
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
	@GetMapping(path = "/getAllOrderByIdUtente", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllOrderByIdUtente() {
		
		List<Personaggio> models = personaggiRepo.getAllOrderByIdUtente();
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/razzaAndStato", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> findByRazzaAndStato(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getByRazzaAndStato(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getByIdUtenteAndRazzaAndStato", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getByIdUtenteAndRazzaAndStato(@RequestBody Personaggio model) {
		System.out.println(model.getRazza());
		
		List<Personaggio> models = personaggiRepo.getByIdUtenteAndRazzaAndStato(model.getUtente().getId(), model.getRazza(), model.getStato());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path ="/getByIdUtenteAndStato", consumes ="application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getByIdUtenteAndStato(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getByIdUtenteAndStato(model.getUtente().getId(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@GetMapping(path ="/getAllRazze", produces = "application/json")
	public ResponseEntity<List<String>> getAllRazzeGroupBy() {
		
		List<String> models = personaggiRepo.getAllRazzeGroupBy();
		
		return ResponseEntity.ok(models);
		
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllRazzeOrderBy", consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzeOrderBy(@RequestBody Personaggio model) {
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderBy(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderBy", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderBy(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderBy(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaOrderById", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaOrderById(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderById(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderById", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderById(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderById(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaOrderBySesso", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaOrderBySesso(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderBySesso(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderBySesso", consumes="application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderBySesso(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderBySesso(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaOrderByRango", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaOrderByRango(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderByRango(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderByRango", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderByRango(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderByRango(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaOrderByDataCreazione", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaOrderByDataCreazione(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderByDataCreazione(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderByDataCreazione", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderByDataCreazione(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderByDataCreazione(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaOrderByIdUtente", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByRazzaOrderByIdUtente(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaOrderByIdUtente(model.getRazza());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByRazzaAndStatoOrderByIdUtente", consumes = "application/json", produces = "application/json" )
	public ResponseEntity<List<Personaggio>> getAllByRazzaAndStatoOrderByIdUtente(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByRazzaAndStatoOrderByIdUtente(model.getRazza(), model.getStato());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderByNominativo", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderByNominativo(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderByNominativo(model.getId());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderBySesso", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderBySesso(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderBySesso(model.getId());
		
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderByRazza", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderByRazza(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderByRazza(model.getId());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderById", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderById(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderById(model.getId());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderByRango", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderByRango(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderByRango(model.getId());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteOrderByDataCreazione", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteOrderByDataCreazione(@RequestBody Utente model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteOrderByDataCreazione(model.getId());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteAndRazza", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazza(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazza(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteAndRazzaOrderById", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazzaOrderById(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazzaOrderById(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteAndRazzaOrderByNominativo", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazzaOrderByNominativo(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazzaOrderByNominativo(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path ="/getAllByIdUtenteAndRazzaOrderBySesso", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazzaOrderBySesso(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazzaOrderBySesso(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteAndRazzaOrderByRango", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazzaOrderByRango(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazzaOrderByRango(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
	@CrossOrigin
	@PostMapping(path = "/getAllByIdUtenteAndRazzaOrderByDataCreazione", consumes = "application/json", produces ="application/json")
	public ResponseEntity<List<Personaggio>> getAllByIdUtenteAndRazzaOrderByDataCreazione(@RequestBody Personaggio model) {
		
		List<Personaggio> models = personaggiRepo.getAllByIdUtenteAndRazzaOrderByDataCreazione(model.getUtente().getId(), model.getRazza());
		return ResponseEntity.ok(models);
	}
	
 	
	@CrossOrigin
	@PostMapping(path = "/countUmanoByUtente", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> countUmanoByUtente(@RequestBody Utente model) {
		
		Integer count = personaggiRepo.countUmanoByUtente(model.getId());
		return ResponseEntity.ok(count);
	}
	
	@CrossOrigin
	@PostMapping(path = "/countHomidByUtente", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> countHomidByUtente(@RequestBody Utente model) {
		
		Integer count = personaggiRepo.countHomidByUtente(model.getId());
		return ResponseEntity.ok(count);
	}
	
	@CrossOrigin
	@PostMapping(path = "/countLupusByUtente", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> countLupusByUtente(@RequestBody Utente model) {
		
		Integer count = personaggiRepo.countLupusByUtente(model.getId());
		return ResponseEntity.ok(count);
	}
	
	@CrossOrigin
	@PostMapping(path = "/countMethisByUtente", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Integer> countMethisByUtente(@RequestBody Utente model) {
		
		Integer count = personaggiRepo.countMetisByUtente(model.getId());
		return ResponseEntity.ok(count);
	}
	
	
	private boolean findByNominativo(Personaggio model) {
		model = personaggiRepo.findByNominativo(model.getNominativo());
		if (model != null) {
			return false;
		}
		return true;
	
	}
	
	

}
