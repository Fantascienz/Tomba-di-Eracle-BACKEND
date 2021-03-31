package tomba.eracle.controllers;

import java.util.ArrayList;
import java.util.List;

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
import tomba.eracle.pojo.CreazioneLocation;
import tomba.eracle.repositories.DirezioniRepo;
import tomba.eracle.repositories.LocationRepo;

@RestController
@RequestMapping("/locations")
public class LocationREST {

	@Autowired
	private LocationRepo locationRepo;

	@Autowired
	private DirezioniRepo direzioniRepo;

	@GetMapping(path = "/macro", produces = "application/json")
	@CrossOrigin
	public List<Location> getAllMacroLocations() {
		return (List<Location>) locationRepo.findByMappa("Macro");
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void creaLocation(@RequestBody CreazioneLocation pojo) {
		pojo.getLocation().setTipo("Reame");
		pojo.getLocation().setMappa("Esterna");
		Location location = locationRepo.save(pojo.getLocation());
		Location umbra = generaUmbra(location);
		salvaDirezioni(location, umbra, pojo);

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

	private Location generaUmbra(Location location/* ,Umbra u */) {
		Location umbra = new Location();
		umbra.setNome(location.getNome());
		umbra.setTipo("Umbra");
		umbra.setAmbiente(location.getAmbiente());
//		umbra.setUrlImgGiorno(u.getUrlImgGiorno);
//		umbra.setUrlImgNotte(u.getUrlImgNotte);
//		umbra.setUrlMinimappa(u.getUrlImgMinimappa);
//		umbra.setUrlAudio(u.getUrlAudio);
		umbra.setMappa("Esterna");
		umbra.setCreatore(location.getCreatore());
		return locationRepo.save(umbra);
	}

	private void salvaDirezioni(Location location, Location umbra, CreazioneLocation cr) {
		// CREO LA DIREZIONE LOCATION REAME SU LOCATION REAME
		Direzione dirLocation = generaDirezione(location);
		setIngresso(location,dirLocation, cr.getIngresso(), false);
		dirLocation.setIdLocationSpecchio(umbra.getId());
		direzioniRepo.save(dirLocation);
		// CREO LA DIREZIONE UMBRA SU LOCATION UMBRA
		Direzione dirUmbra = generaDirezione(umbra);
		setIngresso(umbra,dirUmbra, cr.getIngresso(), true);
		dirUmbra.setIdLocationSpecchio(location.getId());
		direzioniRepo.save(dirUmbra);

	}

	private Direzione generaDirezione(Location location) {
		Direzione dir = new Direzione();
		dir.setIdLocation(location.getId());
		return dir;
	}

	private void setIngresso(Location loc,Direzione dir, String ingresso, boolean umbra) {
		String direzione = "";
		String locationInvertita = "";
		String location = "";
		long idLocation = 0;
		for (int i = 0; i < ingresso.length(); i++) {
			if (ingresso.charAt(i) != 32) {
				direzione += ingresso.charAt(i);
			} else {
				break;
			}
		}
		for (int i = ingresso.length() - 1; i >= 0; i--) {
			if (ingresso.charAt(i) != 32) {
				locationInvertita += ingresso.charAt(i);
			} else {
				for (int k = locationInvertita.length() - 1; k >= 0; k--) {
					location += locationInvertita.charAt(k);
				}
				idLocation = Long.parseLong(location);
				break;
			}
		}

		if (umbra) {
			idLocation = direzioniRepo.findUmbraByLocation(idLocation);
		}
		
		Direzione dirMacro = direzioniRepo.findByIdLocation(idLocation);
		
		switch (direzione) {
		case "nord":
			dir.setIdLocationSud(idLocation);
			dirMacro.setIdLocationNord(loc.getId());
			break;
		case "est":
			dir.setIdLocationOvest(idLocation);
			dirMacro.setIdLocationEst(loc.getId());
			break;
		case "sud":
			dir.setIdLocationNord(idLocation);
			dirMacro.setIdLocationSud(loc.getId());
			break;
		case "ovest":
			dir.setIdLocationEst(idLocation);
			dirMacro.setIdLocationOvest(loc.getId());
			break;
		}
		
		direzioniRepo.save(dirMacro);
	}
}
