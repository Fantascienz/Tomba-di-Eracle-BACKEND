package tomba.eracle.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Chirottero;
import tomba.eracle.entitites.Personaggio;
import tomba.eracle.repositories.ChirotteriRepo;
import tomba.eracle.repositories.PersonaggiRepo;

@RestController
@RequestMapping("/chirotteri")
public class ChirotteriREST {

	@Autowired
	private ChirotteriRepo chirotteriRepo;

	@Autowired
	private PersonaggiRepo personaggiRepo;

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void invia(@RequestBody Chirottero chirottero) {
		chirottero.setDataInvio(LocalDate.now());
		chirotteriRepo.save(chirottero);
	}

	@GetMapping(path = "/ricevuti/{idPersonaggio}", produces = "application/json")
	@CrossOrigin
	public List<Chirottero> getChirotteriRicevutiPg(@PathVariable("idPersonaggio") Long idPersonaggio) {
		return chirotteriRepo.getRicevuti(idPersonaggio);
	}

	@GetMapping(path = "/abilitati", produces = "application/json")
	@CrossOrigin
	public List<Personaggio> getAbilitatiChirotteri() {
		return personaggiRepo.getAbilitatiChirottero();
	}

	@PatchMapping("/{id}")
	@CrossOrigin
	public void segnaLetto(@PathVariable("id") Long id) {
		Chirottero chirottero = chirotteriRepo.findById(id).orElseThrow();
		if (!chirottero.isLetto()) {
			chirottero.setLetto(true);
			chirottero.setDataLettura(LocalDateTime.now());
			chirotteriRepo.save(chirottero);
		}
	}
	
	@DeleteMapping
	@CrossOrigin
	public void eliminaChirotteri () {
		List<Chirottero> chirotteri = (List<Chirottero>) chirotteriRepo.findAll();
		for (Chirottero c : chirotteri) {
			LocalDateTime diff = LocalDateTime.now().minusHours(24); // 16/04 15:00 --> 15/04 15:02
			if (c.getDataLettura().isBefore(diff) ) {  //15/04 15:01
				chirotteriRepo.delete(c);
			}
		}
	}
}
