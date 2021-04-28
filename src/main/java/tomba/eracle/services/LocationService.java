package tomba.eracle.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;
import tomba.eracle.entitites.Meteo;
import tomba.eracle.entitites.Stanza;
import tomba.eracle.entitites.Utente;
import tomba.eracle.pojo.LocationPOJO;
import tomba.eracle.pojo.Umbra;
import tomba.eracle.repositories.DirezioniRepo;
import tomba.eracle.repositories.LocationRepo;
import tomba.eracle.repositories.MeteoRepo;
import tomba.eracle.repositories.StanzeRepo;

@Service
public class LocationService {

	@Autowired
	private LocationRepo locationRepo;

	@Autowired
	private DirezioniRepo direzioniRepo;

	@Autowired
	private StanzeRepo stanzeRepo;

	@Autowired
	private MeteoRepo meteoRepo;

	public void salvaStanza(Location location, Location subLocation) {
		Stanza stanza = new Stanza();
		stanza.setLocation(location);
		stanza.setSubLocation(subLocation);
		stanzeRepo.save(stanza);
	}

	public void eliminaStanze(List<Long> listaIdLocations, Long idLocation) {
		if (idLocation != null) {
			for (int i = 0; i < listaIdLocations.size(); i++) {
				if (listaIdLocations.get(i) % 1000 == idLocation || listaIdLocations.get(i) % 10000 == idLocation
						|| listaIdLocations.get(i) % 100000 == idLocation || listaIdLocations.get(i) % 1000000 == idLocation) {
					Stanza stanza = stanzeRepo.findStanzaBySubLocation(listaIdLocations.get(i));
					if (stanza != null) {
						System.out.println("cancello " + stanza.getId() + " id location " + idLocation);
					}
				}
			}
		}
	}
	
	public void eliminaDirezioni(List<Long> listaIdLocations, Long idLocation) {
		if (idLocation != null) {
			for (int i = 0; i < listaIdLocations.size(); i++) {
				if (listaIdLocations.get(i) % 1000 == idLocation || listaIdLocations.get(i) % 10000 == idLocation
						|| listaIdLocations.get(i) % 100000 == idLocation || listaIdLocations.get(i) % 1000000 == idLocation) {
					Stanza stanza = stanzeRepo.findStanzaBySubLocation(listaIdLocations.get(i));
					if (stanza != null) {
						System.out.println("cancello " + stanza.getId() + " id location " + idLocation);
					}
				}
			}
		}
	}

	public void cancellaLocation(Location location, Location umbra) {
		// LOCATION STANZE DA ELIMINARE
		List<Location> stanze = locationRepo.findStanzeByLocation(location.getId());
		List<Location> stanzeUmbra = locationRepo.findStanzeByLocation(umbra.getId());
		eliminaStanze(stanze, stanzeUmbra);
		// DIREZIONI DA ELIMINARE
		Direzione direzioniLocation = direzioniRepo.findByIdLocation(location.getId());
		Direzione direzioniUmbra = direzioniRepo.findByIdLocation(umbra.getId());
		// STANZE DA ELIMINARE
		Stanza stanza = stanzeRepo.findStanzaBySubLocation(location.getId());
		Stanza stanzaUmbra = stanzeRepo.findStanzaBySubLocation(umbra.getId());
		// DIREZIONI RELATIVE DA AGGIORNARE
		List<Direzione> direzioniRelativeLocation = direzioniRepo.findDirezioniRelative(location.getId());
		List<Direzione> direzioniRelativeUmbra = direzioniRepo.findDirezioniRelative(umbra.getId());
		// AGGIORNO LE DIREZIONI RELATIVE
		aggiornaDirezioni(direzioniRelativeLocation, location.getId());
		aggiornaDirezioni(direzioniRelativeUmbra, umbra.getId());
		// ELIMINO LE STANZE
		if (stanza != null && stanzaUmbra != null) {
			stanzeRepo.delete(stanza);
			stanzeRepo.delete(stanzaUmbra);
		}
		// ELIMINO LE DIREZIONI
		if (direzioniLocation != null && direzioniUmbra != null) {
			direzioniRepo.delete(direzioniLocation);
			direzioniRepo.delete(direzioniUmbra);
		}
		// ELIMINO LE LOCATION
		locationRepo.delete(location);
		locationRepo.delete(umbra);
	}

	public void cancellaLocation(Location location) {
		// LOCATION STANZE DA ELIMINARE
		List<Location> stanze = locationRepo.findStanzeByLocation(location.getId());
		eliminaStanze(stanze, null);
		// DIREZIONI DA ELIMINARE
		Direzione direzioniLocation = direzioniRepo.findByIdLocation(location.getId());
		// STANZE DA ELIMINARE
		Stanza stanza = stanzeRepo.findStanzaBySubLocation(location.getId());
		// DIREZIONI RELATIVE DA AGGIORNARE
		List<Direzione> direzioniRelativeLocation = direzioniRepo.findDirezioniRelative(location.getId());
		// AGGIORNO LE DIREZIONI RELATIVE
		aggiornaDirezioni(direzioniRelativeLocation, location.getId());
		// ELIMINO LE STANZE
		if (stanza != null) {
			stanzeRepo.delete(stanza);
		}
		// ELIMINO LE DIREZIONI
		if (direzioniLocation != null) {
			direzioniRepo.delete(direzioniLocation);
		}
		// ELIMINO LE LOCATION
		locationRepo.delete(location);
	}

	public void modificaLocation(Location location, Location mod) {
		Optional<Location> umbra = locationRepo.findById(direzioniRepo.findUmbraByLocation(mod.getId()));
		if (!mod.getNome().isBlank()) {
			location.setNome(mod.getNome());
			if (!location.getTipo().equalsIgnoreCase("Stanza Umbra")) {
				umbra.get().setNome(mod.getNome());
				locationRepo.save(umbra.get());
			}
		}

		if (!mod.getAmbiente().isBlank()) {
			location.setAmbiente(mod.getAmbiente());
		}
		if (mod.getFasciaOraria() != null) {
			if (mod.getFasciaOraria().equalsIgnoreCase("ripristina reale")) {
				location.setFasciaOraria(null);
			} else if (!mod.getFasciaOraria().isBlank()) {
				location.setFasciaOraria(mod.getFasciaOraria());
			}
		}
		if (mod.getChiave() != null) {
			if (!mod.getChiave().isBlank()) {
				location.setChiave(mod.getChiave());
			}
		}
		if (mod.getUrlImgGiorno() != null) {
			if (!mod.getUrlImgGiorno().isBlank()) {
				location.setUrlImgGiorno(mod.getUrlImgGiorno());
			}
		}
		if (mod.getUrlImgNotte() != null) {
			if (!mod.getUrlImgNotte().isBlank()) {
				location.setUrlImgNotte(mod.getUrlImgNotte());
			}
		}
		if (mod.getUrlMinimappa() != null) {
			if (!mod.getUrlMinimappa().isBlank()) {
				location.setUrlMinimappa(mod.getUrlMinimappa());
			}
		}
		if (mod.getUrlAudio() != null) {
			if (!mod.getUrlAudio().isBlank()) {
				location.setUrlAudio(mod.getUrlAudio());
			}
		}
		if (mod.getMeteoGiorno() != null && location.getMeteoGiorno() != mod.getMeteoGiorno()) {
			location.setMeteoGiorno(mod.getMeteoGiorno());
			umbra.get().setMeteoGiorno(mod.getMeteoGiorno());
			locationRepo.save(umbra.get());
			if (location.getMappa().equalsIgnoreCase("Macro") || umbra.get().getMappa().equalsIgnoreCase("Mappa")) {
				setMeteoMacroMappa(mod.getMeteoGiorno(), null);
			}
		}
		if (mod.getMeteoNotte() != null && location.getMeteoNotte() != mod.getMeteoNotte()) {
			location.setMeteoNotte(mod.getMeteoNotte());
			umbra.get().setMeteoNotte(mod.getMeteoNotte());
			locationRepo.save(umbra.get());
			if (location.getMappa().equalsIgnoreCase("Macro") || umbra.get().getMappa().equalsIgnoreCase("Mappa")) {
				setMeteoMacroMappa(null, mod.getMeteoNotte());
			}
		}
		location.setData(mod.getData());
		locationRepo.save(location);
	}

	public void salvaDirezioniIngresso(Location location, Location umbra, LocationPOJO cr) {
		// CREO LA DIREZIONE LOCATION REAME SU LOCATION REAME
		Direzione dirLocation = generaDirezione(location);
		setIngresso(location, dirLocation, cr.getDirezioneIngresso(), cr.getIdLocationIngresso(), false);
		dirLocation.setIdLocationSpecchio(umbra.getId());
		direzioniRepo.save(dirLocation);
		// CREO LA DIREZIONE UMBRA SU LOCATION UMBRA
		Direzione dirUmbra = generaDirezione(umbra);
		setIngresso(umbra, dirUmbra, cr.getDirezioneIngresso(), cr.getIdLocationIngresso(), true);
		dirUmbra.setIdLocationSpecchio(location.getId());
		direzioniRepo.save(dirUmbra);
	}

	public void salvaDirezioniUscita(Location location, Location umbra, LocationPOJO cr) {
		// CREO LA DIREZIONE LOCATION REAME SU LOCATION REAME
		Direzione dirLocation = generaDirezione(location);
		setUscita(dirLocation, cr.getDirezioneUscita(), cr.getSuperLocation(), false);
		if (umbra != null) {
			dirLocation.setIdLocationSpecchio(umbra.getId());
		}
		direzioniRepo.save(dirLocation);
		// CREO LA DIREZIONE UMBRA SU LOCATION UMBRA
		if (umbra != null) {
			if (!location.getTipo().equalsIgnoreCase("Stanza Umbra")) {
				Direzione dirUmbra = generaDirezione(umbra);
				setUscita(dirUmbra, cr.getDirezioneUscita(), cr.getSuperLocation(), true);
				dirUmbra.setIdLocationSpecchio(location.getId());
				direzioniRepo.save(dirUmbra);
			}
		}
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
		if (location.getTipo().equalsIgnoreCase("Stanza")) {
			umbra.setTipo("Stanza Umbra");
		} else {
			umbra.setTipo("Umbra");
		}
		umbra.setAmbiente(location.getAmbiente());
		umbra.setUrlImgGiorno(u.getUrlImgGiorno());
		umbra.setUrlImgNotte(u.getUrlImgNotte());
//		umbra.setUrlMinimappa(u.getUrlImgMinimappa);
		umbra.setMeteoGiorno(location.getMeteoGiorno());
		umbra.setMeteoNotte(location.getMeteoNotte());
		umbra.setUrlAudio(u.getUrlAudio());
		umbra.setMappa(location.getMappa());
		umbra.setCreatore(location.getCreatore());
		return locationRepo.save(umbra);
	}

	public void setDirezioni(List<Location> lista) {
		for (Location location : lista) {
			location.setDirezioni(direzioniRepo.findByIdLocation(location.getId()));
			setNomiDirezioni(location.getDirezioni());
		}
	}

	public void setDirezioni(Location location) {
		location.setDirezioni(direzioniRepo.findByIdLocation(location.getId()));
		setNomiDirezioni(location.getDirezioni());
	}

	public void setNumeroStanze(List<Location> lista) {
		for (Location location : lista) {
			location.setNumeroStanze(stanzeRepo.findNumeroStanzeByLocation(location.getId()));
		}
	}

	public void setMeteo(Location location, Long idMeteoGiorno, Long idMeteoNotte) {
		if (idMeteoGiorno != 0) {
			location.setMeteoGiorno(meteoRepo.findById(idMeteoGiorno).get());
		}
		if (idMeteoNotte != 0) {
			location.setMeteoNotte(meteoRepo.findById(idMeteoNotte).get());
		}
	}

	private Direzione generaDirezione(Location location) {
		Direzione dir = new Direzione();
		dir.setIdLocation(location.getId());
		return dir;
	}

	private void setIngresso(Location loc, Direzione dir, String direzione, Long idLocation, boolean umbra) {
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

	private void setUscita(Direzione dirLocation, String direzione, Long superLocation, boolean umbra) {
		if (umbra) {
			superLocation = direzioniRepo.findUmbraByLocation(superLocation);
		}
		switch (direzione) {
		case "nord":
			dirLocation.setIdLocationNord(superLocation);
			break;
		case "est":
			dirLocation.setIdLocationEst(superLocation);
			break;
		case "sud":
			dirLocation.setIdLocationSud(superLocation);
			break;
		case "ovest":
			dirLocation.setIdLocationOvest(superLocation);
			break;
		}
	}

	private void eliminaStanze(List<Location> stanze, List<Location> stanzeUmbra) {
		if (stanzeUmbra != null) {
			if ((stanze.size() == stanzeUmbra.size()) && (stanze != null && stanzeUmbra != null)
					&& stanze.size() != 0) {
				for (int i = 0; i < stanze.size(); i++) {
					Location location = stanze.get(i);
					Location umbra = stanzeUmbra.get(i);
					cancellaLocation(location, umbra);
				}
			}
		} else {
			for (int i = 0; i < stanze.size(); i++) {
				Location location = stanze.get(i);
				cancellaLocation(location);
			}
		}
	}

	private void setNomiDirezioni(Direzione direzioni) {
//		if (direzioni != null) {
//			if (direzioni.getIdLocationNord() != null) {
//				direzioni.setNomeLocationNord(locationRepo.findById(direzioni.getIdLocationNord()).get().getNome());
//			}
//			if (direzioni.getIdLocationEst() != null) {
//				direzioni.setNomeLocationEst(locationRepo.findById(direzioni.getIdLocationEst()).get().getNome());
//			}
//			if (direzioni.getIdLocationSud() != null) {
//				direzioni.setNomeLocationSud(locationRepo.findById(direzioni.getIdLocationSud()).get().getNome());
//			}
//			if (direzioni.getIdLocationOvest() != null) {
//				direzioni.setNomeLocationOvest(locationRepo.findById(direzioni.getIdLocationOvest()).get().getNome());
//			}
//		}
	}

	private void setMeteoMacroMappa(Meteo giorno, Meteo notte) {
		List<Location> listaMacro = locationRepo.findMacroLocations();
		for (Location location : listaMacro) {
			if (giorno != null) {
				location.setMeteoGiorno(giorno);
			}
			if (notte != null) {
				location.setMeteoNotte(notte);
			}
			locationRepo.save(location);
			List<Location> stanze = locationRepo.findStanzeByLocation(location.getId());
			for (Location stanza : stanze) {
				if (giorno != null) {
					stanza.setMeteoGiorno(giorno);
				}
				if (notte != null) {
					stanza.setMeteoNotte(notte);
				}
				locationRepo.save(stanza);
			}
		}

	}

	public void setUmbra(Location location, Location specchio, Umbra umbra) {
		location.setTipo("Umbra");
		location.setNome(specchio.getNome());
		location.setAmbiente(specchio.getAmbiente());
		if (specchio.getMappa().equalsIgnoreCase("Esterna")) {
			location.setChiave(specchio.getChiave());
		}

		location.setUrlImgGiorno(umbra.getUrlImgGiorno());
		location.setUrlImgNotte(umbra.getUrlImgNotte());
		location.setUrlAudio(umbra.getUrlAudio());
		location.setMeteoGiorno(specchio.getMeteoGiorno());
		location.setMeteoNotte(specchio.getMeteoNotte());
		location.setMappa("Esterna");
		location.setCreatore(specchio.getCreatore());

	}

	public void resettaLocationEsterna(Location location) {
		location.setNome("/");
		location.setAmbiente("/");
		location.setUrlImgGiorno("/");
		location.setUrlImgNotte(null);
		location.setUrlAudio(null);
		location.setUrlMinimappa(null);
		location.setChiave(null);
		location.setCreatore(new Utente((long) 999));
		location.setRoom(false);
		locationRepo.save(location);
	}

	public void resettaLocationMacro(Location location) {
		location.setRoom(false);
		locationRepo.save(location);
	}

//	public void eliminaSottoLocation(Location superLocation) {
//		List<Location> midLocations;
//		List<Location> innerLocations;
//		List<Location> stanzeLocations;
//		for (int i = 1; i <= 9; i++) {
//			if (superLocation.getMappa().equalsIgnoreCase("Macro")) {
//				midLocations = locationRepo.findMidLocationsBySuperLocationMacro(superLocation.getId(), i);
//			} else {
//				midLocations = locationRepo.findMidLocationsBySuperLocationEsterna(superLocation.getId(), i);
//			}
//			if (!midLocations.isEmpty()) {
//				for (Location l : midLocations) {
//					eliminaSottoLocation(l);
//					eliminaStanze(l);
//					direzioniRepo.delete(direzioniRepo.findByLocation(l.getId()));
//					eliminaStanze(l);
//					locationRepo.delete(l);
//				}
//			}
//		}
//		for (int i = 1; i <= 4; i++) {
//			if (superLocation.getMappa().equalsIgnoreCase("Macro")) {
//				innerLocations = locationRepo.findInnerLocationsBySuperLocationMacro(superLocation.getId(), i);
//			} else {
//				innerLocations = locationRepo.findInnerLocationsBySuperLocationEsterna(superLocation.getId(), i);
//			}
//			if (!innerLocations.isEmpty()) {
//				for (Location l : innerLocations) {
//					eliminaSottoLocation(l);
//					direzioniRepo.delete(direzioniRepo.findByLocation(l.getId()));
//					eliminaStanze(l);
//					locationRepo.delete(l);
//				}
//			}
//		}
//		if (superLocation.getMappa().equalsIgnoreCase("Macro")) {
//			stanzeLocations = locationRepo.findStanzeLocationsBySuperLocationMacro(superLocation.getId());
//		} else {
//			stanzeLocations = locationRepo.findStanzeLocationsBySuperLocationEsterna(superLocation.getId());
//		}
//		if (!stanzeLocations.isEmpty()) {
//			for (Location l : stanzeLocations) {
//				eliminaSottoLocation(l);
//				direzioniRepo.delete(direzioniRepo.findByLocation(l.getId()));
//				eliminaStanze(l);
//				locationRepo.delete(l);
//			}
//		}
//	}

}
