package tomba.eracle.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
import tomba.eracle.repositories.UtentiRepo;

@RestController
@RequestMapping("/utenti")
public class UtentiREST {

	@Autowired
	private UtentiRepo utentiRepo;

	@GetMapping(produces = "application/json")
	public List<Utente> getAll() {
		return (List<Utente>) utentiRepo.findAll();
	}

	@PostMapping(consumes = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> registrazione(@RequestBody Utente utente) {
		try {
			codificaPassword(utente);
			utente.setTipo("standard");
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
		return ResponseEntity.ok(utente);

	}

	@PostMapping(path = "/modifica", consumes = "application/json", produces = "application/json")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Utente> modificaUtente(@RequestBody ModificaUtente mod) {
		System.out.println(mod.getUtente().toString());
		System.out.println(mod.getVecchiaPsw());
		try {
			String password = utentiRepo.findPasswordByUtente(mod.getUtente().getId());
			if (password.equals(codificaPassword(mod.getVecchiaPsw()))) {
				mod.getUtente().setPsw(codificaPassword(mod.getUtente().getPsw()));
				return ResponseEntity.ok(utentiRepo.save(mod.getUtente()));
			}
		} catch (Exception e) {
			
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
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

}
