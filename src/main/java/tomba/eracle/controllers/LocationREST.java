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
import tomba.eracle.pojo.LocationPOJO;
import tomba.eracle.pojo.Room;
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
		List<Location> lista = (List<Location>) locationRepo.getAllLocations();
		locationService.setDirezioni(lista);
//		locationService.setNumeroStanze(lista);
		return lista;
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void creaLocation(@RequestBody LocationPOJO pojo) {
		locationService.setMeteo(pojo.getLocation(), pojo.getMeteoGiorno(), pojo.getMeteoNotte());
		pojo.getLocation().setTipo("Reame");
		pojo.getLocation().setMappa("Esterna");
		Location location = locationRepo.save(pojo.getLocation());
		Location umbra = locationRepo.findById(direzioniRepo.findUmbraByLocation(location.getId())).get();
		locationService.setUmbra(umbra, location, pojo.getUmbra());
		locationRepo.save(umbra);
	}

	@PostMapping(path = "/rooms", consumes = "application/json")
	@CrossOrigin
	public void creaRoom(@RequestBody Room[] rooms) {
		Location superLocation = locationRepo.findById(rooms[0].getSuperLocation().getId()).get();
		Location superUmbra = locationRepo.findById(direzioniRepo.findUmbraByLocation(superLocation.getId())).get();
		for (Room r : rooms) {
			if (r.getLocation() != null) {
				locationRepo.save(r.getLocation());
				locationService.salvaStanza(superLocation, r.getLocation());
			}
			locationRepo.save(r.getLocationUmbra());
			locationService.salvaStanza(superUmbra, r.getLocationUmbra());
		}

		for (Room r : rooms) {
			if (r.getLocation() != null) {
				direzioniRepo.save(r.getDirezioni());
			}
			direzioniRepo.save(r.getDirezioniUmbra());
		}
		superLocation.setRoom(true);
		superUmbra.setRoom(true);
		locationRepo.save(superLocation);
		locationRepo.save(superUmbra);
	}

	@PostMapping(path = "/stanze", consumes = "application/json")
	@CrossOrigin
	public void creaStanza(@RequestBody LocationPOJO pojo, Optional<Location> superLocation, Direzione direzione) {
		String mappa = locationRepo.findMappa(pojo.getSuperLocation());
		pojo.getLocation().setMappa(mappa);
		superLocation = locationRepo.findById(pojo.getSuperLocation());
		Location location = pojo.getLocation();
		location.setMeteoGiorno(superLocation.get().getMeteoGiorno());
		location.setMeteoNotte(superLocation.get().getMeteoNotte());
		if (superLocation.get().getTipo().equalsIgnoreCase("Umbra")
				|| superLocation.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
			location.setUrlImgGiorno(pojo.getUmbra().getUrlImgGiorno());
			location.setUrlImgNotte(pojo.getUmbra().getUrlImgNotte());
			location.setTipo("Stanza Umbra");
		}
		location = locationRepo.save(pojo.getLocation());
		// STANZE
		Stanza stanza = new Stanza();
		stanza.setLocation(superLocation.get());
		stanza.setSubLocation(location);
		stanzeRepo.save(stanza);
		if (!superLocation.get().getTipo().equalsIgnoreCase("Umbra")
				&& !superLocation.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
			Location umbra = locationService.generaUmbra(pojo.getLocation(), pojo.getUmbra());
			umbra = locationRepo.save(umbra);
			Stanza stanzaUmbra = new Stanza();
			stanzaUmbra.setLocation(
					locationRepo.findById(direzioniRepo.findUmbraByLocation(superLocation.get().getId())).get());
			stanzaUmbra.setSubLocation(umbra);
			stanzeRepo.save(stanzaUmbra);
			locationService.salvaDirezioniUscita(location, umbra, pojo);
		} else {
			Location specchio = locationRepo.findById(direzioniRepo.findUmbraByLocation(superLocation.get().getId()))
					.orElseThrow();
			locationService.salvaDirezioniUscita(location, specchio, pojo);
		}

	}

	@GetMapping(path = "/stanze", produces = "application/json")
	@CrossOrigin
	public List<Stanza> getAllStanze() {
		return (List<Stanza>) stanzeRepo.findAll();
	}

	@PostMapping(path = "/update", consumes = "application/json")
	@CrossOrigin
	public void modificaLocation(@RequestBody LocationPOJO pojo) {
		Optional<Location> location = locationRepo.findById(pojo.getLocation().getId());
		locationService.setMeteo(pojo.getLocation(), pojo.getMeteoGiorno(), pojo.getMeteoNotte());
		locationService.modificaLocation(location.get(), pojo.getLocation());
	}

	@DeleteMapping(path = "/delete/{id}")
	@CrossOrigin
	public void cancellaEsterna(@PathVariable("id") Long id) {
		Location location = locationRepo.findById(id).get();
		Long idSpecchio = direzioniRepo.findUmbraByLocation(id);
		Location specchio = null;

		if (location != null) {
			
		}
		if (idSpecchio != null) {
			specchio = locationRepo.findById(idSpecchio).get();
		}

		List<Long> listaIdLocations = locationRepo.getAllIdLocations();
		
		//ELIMINAZIONE STANZE
		locationService.eliminaStanze(listaIdLocations, id);
		locationService.eliminaStanze(listaIdLocations, idSpecchio);
		
		//ELIMINAZIONE DIREZIONI

//		locationService.eliminaStanze(location);
//		locationService.eliminaStanze(specchio);
//		locationService.eliminaSottoLocation(location);
//		locationService.eliminaSottoLocation(specchio);
//		if (!location.getMappa().equalsIgnoreCase("Macro")) {
//			locationService.resettaLocationEsterna(location);
//			locationService.resettaLocationEsterna(specchio);
//		} else {
//			locationService.resettaLocationMacro(location);
//			locationService.resettaLocationMacro(specchio);
//		}
	}

//	@DeleteMapping(path = "/delete/{id}")   SCOMMENTA PER UTILIZZARLA PER CANCELLAZIONE SUB LOCATIONS
//	@CrossOrigin
//	public void cancellaLocation(Long idEsterna) {
//		// LOCATION DA ELIMINARE
//		Optional<Location> location = locationRepo.findById(idEsterna);
//		Long idUmbra = direzioniRepo.findUmbraByLocation(idEsterna);
//		if (idUmbra != null && !location.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
//			Optional<Location> umbra = locationRepo.findById(idUmbra);
//			if (location.get().getMappa().equalsIgnoreCase("Esterna")
//					|| location.get().getTipo().equalsIgnoreCase("Stanza")) {
//				locationService.cancellaLocation(location.get(), umbra.get());
//			}
//		} else {
//			if (location.get().getMappa().equalsIgnoreCase("Esterna")
//					|| location.get().getTipo().equalsIgnoreCase("Stanza")
//					|| location.get().getTipo().equalsIgnoreCase("Stanza Umbra")) {
//				locationService.cancellaLocation(location.get());
//			}
//		}
//	}

	@GetMapping(path = "/mappa/{id}", produces = "text/plain")
	@CrossOrigin
	public String trovaMappa(@PathVariable("id") Long id) {
		return locationRepo.findMappa(id);
	}

}
