package tomba.eracle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;
import tomba.eracle.entitites.Personaggio;
import tomba.eracle.repositories.DirezioniRepo;
import tomba.eracle.repositories.LocationRepo;
import tomba.eracle.repositories.PersonaggiRepo;

@RestController
@RequestMapping("/game")
public class GameREST {

	@Autowired
	private LocationRepo locationRepo;
	
	@Autowired
	private DirezioniRepo direzioniRepo;
	
	@Autowired
	private PersonaggiRepo personaggiRepo;
	
	@GetMapping(path = "/ultimaLocation/{id}", produces = "application/json")
	@CrossOrigin
	public Location ultimaLocation (@PathVariable("id") Long id) {
		return locationRepo.findUltimaLocationPg(id);
	}
	
	@GetMapping(path = "/direzioniRelativeLocation/{id}", produces = "application/json")
	@CrossOrigin
	public Direzione direzioniRelativeLocation (@PathVariable("id") Long id) {
		return direzioniRepo.findByIdLocation(id);
	}
	
	@PostMapping(path = "/naviga/{id}", produces = "application/json", consumes = "application/json")
	@CrossOrigin
	public Location naviga (@PathVariable("id") Long idLocation, @RequestBody Personaggio pg) {
		pg.setUltimaLocation(idLocation);
		personaggiRepo.save(pg);
		return locationRepo.findById(idLocation).get();
	}
	
}
