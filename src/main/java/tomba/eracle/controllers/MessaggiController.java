package tomba.eracle.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Messaggio;
import tomba.eracle.entitites.Utente;
import tomba.eracle.repositories.MessaggiRepo;
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/messaggi")
public class MessaggiController {
	
	@Autowired
	private MessaggiRepo messaggiRepo;
	
	@Autowired
	private UtentiRepo utentiRepo;

	@PostMapping(consumes = "application/json")
	@CrossOrigin
	public void invia (@RequestBody Messaggio messaggio) {
		messaggiRepo.save(messaggio);
	}
	
	@DeleteMapping("/{idUtente}")
	@CrossOrigin
	public void eliminaConversazione (@PathVariable("idUtente") Long idUtente) {
		List<Messaggio> messaggi = messaggiRepo.getConversazione(idUtente);
		for (Messaggio m : messaggi) {
			messaggiRepo.delete(m);
		}
	}
	
	@GetMapping
	@CrossOrigin
	public List<Utente> getAllConversazioniAttive () {		
		List<Long> idUtentiConv = messaggiRepo.getAllConversazioniAttive();
		List<Utente> utentiConv = new ArrayList<Utente>();
		for (Long id : idUtentiConv) {
			utentiConv.add(utentiRepo.findById(id).get());
		}
		return utentiConv;
	}
	
	@GetMapping("/{utente}")
	@CrossOrigin
	public List<Messaggio> getConversazione (@PathVariable("utente")Long idUtente) {
		return messaggiRepo.getConversazione(idUtente);
	}
}
