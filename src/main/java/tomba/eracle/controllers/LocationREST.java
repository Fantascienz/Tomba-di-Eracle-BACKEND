package tomba.eracle.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
