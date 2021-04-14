package tomba.eracle.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tomba.eracle.entitites.Utente;
import tomba.eracle.pojo.ModificaUtente;
import tomba.eracle.repositories.PersonaggiRepo;
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/utenti")
public class UtentiREST {

	@Autowired
	private UtentiRepo utentiRepo;
	
	@Autowired
	private PersonaggiRepo personaggiRepo;

	@GetMapping(produces = "application/json")
	@CrossOrigin
	public List<Utente> getAll() {
		List<Utente> lista = (List<Utente>) utentiRepo.findAll();
		for (Utente u : lista) {
			setNumeroPg(u);
		}
		return lista;
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> registrazione(@RequestBody Utente utente) {
		try {
			codificaPassword(utente);
			utente.setTipo("standard");
			utente.setMaxUmani(1);
			utente.setMaxGarou(0);
			utente.setMaxPng(0);
			utente.setDataRegistrazione(LocalDate.now());
			utente = utentiRepo.save(utente);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		return ResponseEntity.ok(utente);
	}

	@PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> login(@RequestBody Utente utente) {
		codificaPassword(utente);
		utente = utentiRepo.findByEmailAndPsw(utente.getEmail(), utente.getPsw());
		if (utente == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		utente.setContatoreUmani(personaggiRepo.countUmanoByUtente(utente.getId()));
		utente.setContatoreHomid(personaggiRepo.countHomidByUtente(utente.getId()));
		utente.setContatoreLupus(personaggiRepo.countLupusByUtente(utente.getId()));
		utente.setContatoreMetis(personaggiRepo.countMetisByUtente(utente.getId()));
		return ResponseEntity.ok(utente);

	}

	@PostMapping(path = "/modifica", consumes = "application/json", produces = "application/json")
	@CrossOrigin
	public ResponseEntity<Utente> modificaUtente(@RequestBody ModificaUtente mod) {
		System.out.println(mod.getUtente());
		System.out.println(mod.getUtente().getPsw());
		try {
			String password = utentiRepo.findPasswordByUtente(mod.getUtente().getId());
			if (password.equals(codificaPassword(mod.getVecchiaPsw()))) {
				codificaPassword(mod.getUtente());
				Optional<Utente> utente = utentiRepo.findById(mod.getUtente().getId());
				if (!mod.getUtente().getNominativo().isBlank()) {
					utente.get().setNominativo(mod.getUtente().getNominativo());
				}
				if (!mod.getUtente().getEmail().isBlank()) {
					utente.get().setEmail(mod.getUtente().getEmail());
				}
				if (!password.equals(codificaPassword(mod.getUtente().getPsw()))) {
					utente.get().setPsw(mod.getUtente().getPsw());
				}
				return ResponseEntity.ok(utentiRepo.save(utente.get()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	}

	@PostMapping(path = "/massimali", consumes = "application/json")
	@CrossOrigin
	public void modificaMassimaliPg(@RequestBody Utente u) {
		utentiRepo.save(u);
	}

	@GetMapping(path = "/findAllTipoUtente", produces = "application/json")
	@CrossOrigin
	public ResponseEntity<List<String>> findAllTipoUtente() {
		List<String> models = utentiRepo.findAllTipoUtente();
		return ResponseEntity.ok(models);
	}

	@PostMapping(path = "/findAllByTipoUtente", consumes = "application/json", produces = "application/json")
	@CrossOrigin
	public ResponseEntity<List<Utente>> findAllByTipoUtente(@RequestBody Utente model) {
		List<Utente> models = utentiRepo.findAllByTipoUtente(model.getTipo());
		return ResponseEntity.ok(models);
	}
	
	@PostMapping(path = "/findByNominativo", consumes  = "application/json", produces = "application/json")
	@CrossOrigin
	public ResponseEntity<List<Utente>> findByNominativo(@RequestBody Utente model) {
		System.out.println(model.getNominativo());
		List<Utente> models = utentiRepo.findByNominativo(model.getNominativo());
		return ResponseEntity.ok(models);
	}
	
	@PostMapping(path = "/findByNominativoAndTipo", consumes = "application/json", produces = "application/json")
	@CrossOrigin
	public ResponseEntity<List<Utente>> findByNominativoAndTipo(@RequestBody Utente model) {
		List<Utente> models = utentiRepo.findByNominativoAndTipo(model.getNominativo(), model.getTipo());
		return ResponseEntity.ok(models);
	}
	
	private void codificaPassword(Utente u) {
		String password = u.getPsw();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(password.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String codPass = number.toString(16);
			u.setPsw(codPass);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private String codificaPassword(String psw) {
		String password = psw;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(password.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String codPass = number.toString(16);
			return codPass;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setNumeroPg(Utente u) {
		u.setNumeroPersonaggi(utentiRepo.findNumeroPgUtente(u.getId()));
	}

}
