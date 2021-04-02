package tomba.eracle.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Location;
import tomba.eracle.entitites.Stanza;
import tomba.eracle.pojo.CreazioneLocation;
import tomba.eracle.repositories.DirezioniRepo;
import tomba.eracle.repositories.LocationRepo;
import tomba.eracle.repositories.StanzeRepo;
import tomba.eracle.services.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationREST {

	@Autowired
	private LocationRepo locationRepo;

	@Autowired
	private DirezioniRepo direzioniRepo;

	@Autowired
	private StanzeRepo stanzeRepo;

	@Autowired
	private LocationService locationService;

	@GetMapping(produces = "application/json")
	@CrossOrigin
	public List<Location> getAllLocations() {
		return (List<Location>) locationRepo.findAll();
	}

	@GetMapping(path = "/macro", produces = "application/json")
	@CrossOrigin
	public List<Location> getAllMacroLocations() {
		return (List<Location>) locationRepo.findByMappa("Macro");
	}

	@GetMapping(path = "/esterne/{tipo}", produces = "application/json")
	@CrossOrigin
	public List<Location> getAllLocationsEsterne(@PathVariable String tipo) {
		return (List<Location>) locationRepo.findEsterneByTipo(tipo);
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void creaLocation(@RequestBody CreazioneLocation pojo) {
		pojo.getLocation().setTipo("Reame");
		pojo.getLocation().setMappa("Esterna");
		Location location = locationRepo.save(pojo.getLocation());
		Location umbra = locationService.generaUmbra(location, pojo.getUmbra());
		locationService.salvaDirezioni(location, umbra, pojo);

	}

	@PostMapping(path = "/stanze",consumes = "application/json")
	@CrossOrigin
	public void creaStanza(@RequestBody CreazioneLocation pojo, Stanza stanza, Location superLocation, Location umbra) {
		String mappa = locationRepo.findMappa(pojo.getSuperLocation());
		pojo.getLocation().setMappa(mappa);
		superLocation.setId(pojo.getSuperLocation());
		stanza.setLocation(superLocation);
		stanza.setSubLocation(pojo.getLocation());
		locationRepo.save(pojo.getLocation());
		stanzeRepo.save(stanza);
		umbra = locationService.generaUmbra(pojo.getLocation(), pojo.getUmbra());
		locationRepo.save(umbra);
	}

	@PostMapping(path = "/update", consumes = "application/json")
	@CrossOrigin
	public void modificaLocation(@RequestBody Location mod) {
		Optional<Location> location = locationRepo.findById(mod.getId());
		locationService.modificaLocation(location.get(), mod);
	}

	@DeleteMapping(path = "/delete/{id}")
	@CrossOrigin
	public void cancellaLocation(@PathVariable("id") Long id) {
		System.out.println("cancella");
		// LOCATION DA ELIMINARE
		Optional<Location> location = locationRepo.findById(id);
		Optional<Location> umbra = locationRepo.findById(direzioniRepo.findUmbraByLocation(id));
		if (location.get().getMappa().equalsIgnoreCase("Esterna")) {
			locationService.cancellaLocation(location.get(), umbra.get());
		}

	}

	@GetMapping("/{direzioneLibera}")
	@CrossOrigin
	public List<Location> getLocationByDirezioneNull(@PathVariable("direzioneLibera") String direzioneLibera) {
		List<Location> locations = new ArrayList<Location>();
		switch (direzioneLibera) {
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

	@GetMapping(path = "/mappa/{id}", produces = "text/plain")
	@CrossOrigin
	public String trovaMappa(@PathVariable("id") Long id) {
		return locationRepo.findMappa(id);
	}

}
