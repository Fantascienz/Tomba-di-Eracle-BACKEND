package tomba.eracle.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Personaggio;
import tomba.eracle.repositories.PersonaggiRepo;

@RestController
@RequestMapping("/personaggi")
@CrossOrigin(origins = "http://localhost:3000")
public class PersonaggiREST {

	@Autowired
	private PersonaggiRepo personaggiRepo;
	
	
	@GetMapping(path="/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Personaggio>> findByIdUser(@PathVariable("id") Long idUser) {
		
		List<Personaggio> models = personaggiRepo.findByIdUser(idUser);
		
		return ResponseEntity.ok(models);
	}
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<Personaggio> createPg(@RequestBody Personaggio model) {
		
		model.setChirottero(false);
		model.setUmbra(false);
		personaggiRepo.save(model);
		
		return ResponseEntity.ok(model);
	}
	
	
	
	
	
	
}
