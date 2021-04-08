package tomba.eracle.controllers;

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

import tomba.eracle.entitites.Direzione;
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
		List<Location> lista = (List<Location>) locationRepo.findAll();
		locationService.setDirezioni(lista);
		return lista;
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void creaLocation(@RequestBody CreazioneLocation pojo) {
		pojo.getLocation().setTipo("Reame");
		pojo.getLocation().setMappa("Esterna");
		Location location = locationRepo.save(pojo.getLocation());
		Location umbra = locationService.generaUmbra(location, pojo.getUmbra());
		locationService.salvaDirezioniIngresso(location, umbra, pojo);

	}

	@PostMapping(path = "/stanze", consumes = "application/json")
	@CrossOrigin
	public void creaStanza(@RequestBody CreazioneLocation pojo, Optional<Location> superLocation, Direzione direzione) {
		String mappa = locationRepo.findMappa(pojo.getSuperLocation());
		pojo.getLocation().setMappa(mappa);
		superLocation = locationRepo.findById(pojo.getSuperLocation());
		Location location = locationRepo.save(pojo.getLocation());
		System.out.println(superLocation.get().getTipo());
		// STANZE
		Stanza stanza = new Stanza();
		stanza.setLocation(superLocation.get());
		stanza.setSubLocation(location);
		stanzeRepo.save(stanza);
		// gestire tipo stanza umbra
		if (!superLocation.get().getTipo().equalsIgnoreCase("Umbra")
				&& !superLocation.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
			System.out.println("CIAO FACCIO UNA STANZA UMBRA");
			Location umbra = locationService.generaUmbra(pojo.getLocation(), pojo.getUmbra());
			System.out.println(umbra);
			umbra = locationRepo.save(umbra);
			Stanza stanzaUmbra = new Stanza();
			stanzaUmbra.setLocation(
					locationRepo.findById(direzioniRepo.findUmbraByLocation(superLocation.get().getId())).get());
			stanzaUmbra.setSubLocation(umbra);
			stanzeRepo.save(stanzaUmbra);
			locationService.salvaDirezioniUscita(location, umbra, pojo);
		} else {
			location.setTipo("Stanza Umbra");
			locationService.salvaDirezioniUscita(location, null, pojo);
		}

	}

	@GetMapping(path = "/stanze", produces = "application/json")
	@CrossOrigin
	public List<Stanza> getAllStanze() {
		return (List<Stanza>) stanzeRepo.findAll();
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
		System.out.println("CANCELLA LOCATIONS");
		// LOCATION DA ELIMINARE
		Optional<Location> location = locationRepo.findById(id);
		Long idUmbra = direzioniRepo.findUmbraByLocation(id);
		if (idUmbra != null) {
			Optional<Location> umbra = locationRepo.findById(idUmbra);
			if (location.get().getMappa().equalsIgnoreCase("Esterna")
					|| location.get().getTipo().equalsIgnoreCase("Stanza")) {
				locationService.cancellaLocation(location.get(), umbra.get());
			}
		} else {
			if (location.get().getMappa().equalsIgnoreCase("Esterna")
					|| location.get().getTipo().equalsIgnoreCase("Stanza")
					|| location.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
				locationService.cancellaLocation(location.get());
			}
		}

	}

	@GetMapping(path = "/mappa/{id}", produces = "text/plain")
	@CrossOrigin
	public String trovaMappa(@PathVariable("id") Long id) {
		return locationRepo.findMappa(id);
	}

}
