package tomba.eracle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Meteo;
import tomba.eracle.repositories.MeteoRepo;

@RestController
@RequestMapping("/meteo")
public class MeteoREST {

	@Autowired
	private MeteoRepo meteoRepo;
	
	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void modificaMeteoReale(@RequestBody Meteo meteoReale) {
		Meteo meteoAttuale = getMeteoReale();
		meteoAttuale.setClima(meteoReale.getClima());
		meteoAttuale.setData_inserimento(meteoReale.getData_inserimento());
		meteoRepo.save(meteoAttuale);
		
	}
	
	
	
	@GetMapping(path ="/reale", produces = "application/json")
	@CrossOrigin
	public Meteo getMeteoReale() {
		return meteoRepo.findById((long) 1).orElseThrow();
	}
	
	
	
	@GetMapping(path ="/{id}", produces = "application/json")
	@CrossOrigin
	public Meteo getMeteoSpecifico(@PathVariable Long id) {
		return meteoRepo.findById(id).orElseThrow();
	}
	
	
}
