package tomba.eracle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Utente;
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/admin")
public class AdminREST {
	
	@Autowired
	private UtentiRepo utentiRepo;

	@PostMapping(path="/modificaTipo", consumes="application/json")
	@CrossOrigin
	public void modificaTipo (@RequestBody Utente utente) {
		utentiRepo.save(utente);
	}
}
