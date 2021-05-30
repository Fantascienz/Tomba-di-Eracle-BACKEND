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

import tomba.eracle.entitites.ChiaveLocation;
import tomba.eracle.entitites.Location;
import tomba.eracle.entitites.Stanza;
import tomba.eracle.pojo.LocationPOJO;
import tomba.eracle.pojo.Room;
import tomba.eracle.repositories.ChiaviRepo;
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
	private ChiaviRepo chiaviRepo;

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
		Long idSuperUmbra = direzioniRepo.findUmbraByLocation(superLocation.getId());
		Location superUmbra = null;
		if (idSuperUmbra != null) {
			superUmbra = locationRepo.findById(idSuperUmbra).get();
		}
		for (Room r : rooms) {
			if (r.getLocation() != null) {
				if (r.getLocation().getChiave() != null && !r.getLocation().getChiave().isBlank()) {
					r.getLocation().setHasChiave(true);
				}
				locationRepo.save(r.getLocation());
				locationService.salvaStanza(superLocation, r.getLocation());
				if (r.getLocation().getChiave() != null && !r.getLocation().getChiave().isBlank()) {
					chiaviRepo.save(new ChiaveLocation(r.getLocation(), r.getLocation().getChiave()));
				}
			}
			if (r.getLocationUmbra().getChiave() != null && !r.getLocationUmbra().getChiave().isBlank()) {
				r.getLocationUmbra().setHasChiave(true);
			}
			locationRepo.save(r.getLocationUmbra());
			if (r.getLocationUmbra().getChiave() != null && !r.getLocationUmbra().getChiave().isBlank()) {
				chiaviRepo.save(new ChiaveLocation(r.getLocationUmbra(), r.getLocationUmbra().getChiave()));
			}
			if (r.getLocation() != null) {
				locationService.salvaStanza(superUmbra, r.getLocationUmbra());
			} else
				locationService.salvaStanza(superLocation, r.getLocationUmbra());
		}

		for (Room r : rooms) {
			if (r.getLocation() != null) {
				direzioniRepo.save(r.getDirezioni());
			}
			direzioniRepo.save(r.getDirezioniUmbra());
		}
		superLocation.setRoom(true);
		locationRepo.save(superLocation);
		if (superUmbra != null) {
			superUmbra.setRoom(true);
			locationRepo.save(superUmbra);
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

		// ELIMINAZIONE STANZE
		locationService.eliminaStanze(listaIdLocations, id);
		locationService.eliminaStanze(listaIdLocations, idSpecchio);

		// ELIMINAZIONE DIREZIONI
		locationService.eliminaDirezioni(listaIdLocations, id);
		locationService.eliminaDirezioni(listaIdLocations, idSpecchio);

		// ELIMINAZIONE SOTTO-LOCATIONS
		locationService.eliminaSottoLocations(listaIdLocations, location);
		locationService.eliminaSottoLocations(listaIdLocations, specchio);
	}

	@GetMapping(path = "/mappa/{id}", produces = "text/plain")
	@CrossOrigin
	public String trovaMappa(@PathVariable("id") Long id) {
		return locationRepo.findMappa(id);
	}

}
