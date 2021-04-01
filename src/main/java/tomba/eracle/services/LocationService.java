package tomba.eracle.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;
import tomba.eracle.pojo.CreazioneLocation;
import tomba.eracle.pojo.Umbra;
import tomba.eracle.repositories.DirezioniRepo;
import tomba.eracle.repositories.LocationRepo;

@Service
public class LocationService {

	@Autowired
	private LocationRepo locationRepo;

	@Autowired
	private DirezioniRepo direzioniRepo;
	
	public void cancellaLocation (Location location,Location umbra) {
		// DIREZIONI DA ELIMINARE
					Direzione direzioniLocation = direzioniRepo.findByIdLocation(location.getId());
					Direzione direzioniUmbra = direzioniRepo.findByIdLocation(umbra.getId());
					// DIREZIONI RELATIVE DA AGGIORNARE
					List<Direzione> direzioniRelativeLocation = direzioniRepo.findDirezioniRelative(location.getId());
					List<Direzione> direzioniRelativeUmbra = direzioniRepo.findDirezioniRelative(umbra.getId());
					// AGGIORNO LE DIREZIONI RELATIVE
					aggiornaDirezioni(direzioniRelativeLocation, location.getId());
					aggiornaDirezioni(direzioniRelativeUmbra, umbra.getId());
					// ELIMINO LE DIREZIONI
					direzioniRepo.delete(direzioniLocation);
					direzioniRepo.delete(direzioniUmbra);
					// ELIMINO LE LOCATION
					locationRepo.delete(location);
					locationRepo.delete(umbra);
	}

	public void modificaLocation(Location location, Location mod) {
		if (!mod.getNome().isBlank()) {
			location.setNome(mod.getNome());
			Optional<Location> umbra = locationRepo.findById(direzioniRepo.findUmbraByLocation(mod.getId()));
			umbra.get().setNome(mod.getNome());
			locationRepo.save(umbra.get());
		}
		if (mod.getFasciaOraria().equalsIgnoreCase("ripristina reale")) {
			location.setFasciaOraria(null);
		} else if (!mod.getFasciaOraria().isBlank()) {
			location.setFasciaOraria(mod.getFasciaOraria());
		}
		if (mod.getMeteo().equalsIgnoreCase("ripristina reale")) {
			location.setMeteo(null);
		} else if (!mod.getMeteo().isBlank()) {
			location.setMeteo(mod.getMeteo());
		}
		if (!mod.getChiave().isBlank()) {
			location.setChiave(mod.getChiave());
		}
		if (!mod.getUrlImgGiorno().isBlank()) {
			location.setUrlImgGiorno(mod.getUrlImgGiorno());
		}
		if (!mod.getUrlImgNotte().isBlank()) {
			location.setUrlImgNotte(mod.getUrlImgNotte());
		}
		if (!mod.getUrlMinimappa().isBlank()) {
			location.setUrlMinimappa(mod.getUrlMinimappa());
		}
		if (!mod.getUrlAudio().isBlank()) {
			location.setUrlAudio(mod.getUrlAudio());
		}
		location.setData(mod.getData());
		locationRepo.save(location);
	}

	public void salvaDirezioni(Location location, Location umbra, CreazioneLocation cr) {
		// CREO LA DIREZIONE LOCATION REAME SU LOCATION REAME
		Direzione dirLocation = generaDirezione(location);
		setIngresso(location, dirLocation, cr.getIngresso(), false);
		dirLocation.setIdLocationSpecchio(umbra.getId());
		direzioniRepo.save(dirLocation);
		// CREO LA DIREZIONE UMBRA SU LOCATION UMBRA
		Direzione dirUmbra = generaDirezione(umbra);
		setIngresso(umbra, dirUmbra, cr.getIngresso(), true);
		dirUmbra.setIdLocationSpecchio(location.getId());
		direzioniRepo.save(dirUmbra);

	}

	private Direzione generaDirezione(Location location) {
		Direzione dir = new Direzione();
		dir.setIdLocation(location.getId());
		return dir;
	}

	private void setIngresso(Location loc, Direzione dir, String ingresso, boolean umbra) {
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
	
	public void aggiornaDirezioni(List<Direzione> direzioni, Long idLocation) {
		for (Direzione d : direzioni) {
			if (d.getIdLocationNord() != null && d.getIdLocationNord().equals(idLocation)) {
				d.setIdLocationNord(null);
			}
			if (d.getIdLocationEst() != null && d.getIdLocationEst().equals(idLocation)) {
				d.setIdLocationEst(null);
			}
			if (d.getIdLocationSud() != null && d.getIdLocationSud().equals(idLocation)) {
				d.setIdLocationSud(null);
			}
			if (d.getIdLocationOvest() != null && d.getIdLocationOvest().equals(idLocation)) {
				d.setIdLocationOvest(null);
			}
			direzioniRepo.save(d);
		}
	}
	
	public Location generaUmbra(Location location, Umbra u) {
		Location umbra = new Location();
		umbra.setNome(location.getNome());
		umbra.setTipo("Umbra");
		umbra.setAmbiente(location.getAmbiente());
		umbra.setUrlImgGiorno(u.getUrlImgGiorno());
		umbra.setUrlImgNotte(u.getUrlImgNotte());
//		umbra.setUrlMinimappa(u.getUrlImgMinimappa);
		umbra.setUrlAudio(u.getUrlAudio());
		umbra.setMappa("Esterna");
		umbra.setCreatore(location.getCreatore());
		return locationRepo.save(umbra);
	}

}
