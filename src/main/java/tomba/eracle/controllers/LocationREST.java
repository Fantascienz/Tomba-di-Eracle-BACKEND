package tomba.eracle.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Location;
import tomba.eracle.repositories.LocationRepo;

@RestController
@RequestMapping("/locations")
public class LocationREST {

	@Autowired
	private LocationRepo locationRepo;
	
	@GetMapping(path = "/macro", produces = "application/json")
	@CrossOrigin
	public List<Location> getAllMacroLocations () {
		return (List<Location>) locationRepo.findByMappa("Macro");
	}
	
	@GetMapping("/{direzioneLibera}")
	@CrossOrigin
	public List<Location> getLocationByDirezioneNull (@PathVariable("direzioneLibera") String direzioneLibera) {
		List<Location> locations = new ArrayList<Location>();
		switch(direzioneLibera) {
		case "nord":
			locations = locationRepo.findByNordNull();
			break;
		case "est":
			locations = locationRepo.findByEstNull();
			break;
		case "sud":
			locations = locationRepo.findBySudNull();
			break;
		case "ovest":
			locations = locationRepo.findByOvestNull();
			break;
		}
		return locations;
	}
}
