package tomba.eracle.controllers;

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

import tomba.eracle.entitites.Utente;
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/utenti")
public class UtentiREST {

	@Autowired
	private UtentiRepo utentiRepo;

	@GetMapping(produces = "application/json")
	public List<Utente> getAll() {
		return (List<Utente>) utentiRepo.findAll();
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> registrazione(@RequestBody Utente utente) {
		System.out.println("registrazione");
		try {
			utente.setTipo("standard");
			utente = utentiRepo.save(utente);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null); 
		}
		return ResponseEntity.ok(utente);
	}

	@PostMapping( path = "/login", consumes = "application/json", produces = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> login(@RequestBody Utente utente) {		
		utente = utentiRepo.findByEmailAndPsw(utente.getEmail(), utente.getPsw());		
		if (utente == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}		
		return ResponseEntity.ok(utente);

	}

}
