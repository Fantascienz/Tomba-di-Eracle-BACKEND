package tomba.eracle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.ChiaveLocation;
import tomba.eracle.repositories.ChiaviRepo;

@RestController
@RequestMapping("/chiavi")
public class ChiaviController {

	@Autowired
	private ChiaviRepo chiaviRepo;

	@GetMapping("/{idLocation}/{chiave}")
	@CrossOrigin
	public boolean isChiaveCorretta(@PathVariable("idLocation") Long idLocation,
			@PathVariable("chiave") String chiave) {
		ChiaveLocation chiaveLoc = chiaviRepo.findByLocation(idLocation).get();
		return chiaveLoc.getChiave().equals(chiave);
	}
}
