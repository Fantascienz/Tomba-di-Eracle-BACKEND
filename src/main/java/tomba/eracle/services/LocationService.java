package tomba.eracle.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tomba.eracle.entitites.ChiaveLocation;
import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;
import tomba.eracle.entitites.Meteo;
import tomba.eracle.entitites.Stanza;
import tomba.eracle.entitites.Utente;
import tomba.eracle.pojo.Umbra;
import tomba.eracle.repositories.ChiaviRepo;
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
	private ChiaviRepo chiaviRepo;

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
						|| listaIdLocations.get(i) % 100000 == idLocation
						|| listaIdLocations.get(i) % 1000000 == idLocation) {
					Stanza stanza = stanzeRepo.findStanzaBySubLocation(listaIdLocations.get(i));
					if (stanza != null) {
						stanzeRepo.delete(stanza);
					}
				}
			}
		}
	}

	public void eliminaDirezioni(List<Long> listaIdLocations, Long idLocation) {
		if (idLocation != null) {
			for (int i = 0; i < listaIdLocations.size(); i++) {
				if (listaIdLocations.get(i) % 1000 == idLocation || listaIdLocations.get(i) % 10000 == idLocation
						|| listaIdLocations.get(i) % 100000 == idLocation
						|| listaIdLocations.get(i) % 1000000 == idLocation) {
					List<Direzione> direzioni = direzioniRepo.findDirezioniRelative(idLocation);
					if (!direzioni.isEmpty()) {
						for (Direzione d : direzioni) {
							if (d.getIdLocation() > 384) {
								direzioniRepo.delete(d);
							}
						}
					}
					Direzione direzioneCentrale = direzioniRepo.findByLocation(listaIdLocations.get(i));
					if (direzioneCentrale != null) {
						if (direzioneCentrale.getId() > 348) {
							direzioniRepo.delete(direzioneCentrale);
						}
					}
				}
			}
		}
	}

	public void eliminaSottoLocations(List<Long> listaIdLocations, Location location) {
		if (location != null) {
			for (int i = 0; i < listaIdLocations.size(); i++) {
				if (listaIdLocations.get(i) % 1000 == location.getId()
						|| listaIdLocations.get(i) % 10000 == location.getId()
						|| listaIdLocations.get(i) % 100000 == location.getId()
						|| listaIdLocations.get(i) % 1000000 == location.getId()) {
					if ((long) listaIdLocations.get(i) == location.getId()) {
						if (location.getMappa().equalsIgnoreCase("Esterna")) {
							resettaLocationEsterna(location);
						} else if (location.getMappa().equalsIgnoreCase("Stanza")) {
							locationRepo.delete(location);
						} else {
							resettaLocationMacroMidInner(location);
						}
					} else {
						Location locationDelete = locationRepo.findById(listaIdLocations.get(i)).get();
						eliminaChiavi(locationDelete);
						locationRepo.delete(locationDelete);
						location.setRoom(false);
						locationRepo.save(location);
					}
				}
			}
		}
	}

	private void eliminaChiavi(Location location) {
		Optional<ChiaveLocation> chiave = chiaviRepo.findByLocation(location.getId());
		if (chiave.isPresent()) {
			chiaviRepo.delete(chiave.get());
		}
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

	private void setNomiDirezioni(Direzione direzioni) {
		if (direzioni != null) {
			if (direzioni.getIdLocationNord() != null) {
				direzioni.setNomeLocationNord(locationRepo.findById(direzioni.getIdLocationNord()).get().getNome());
			}
			if (direzioni.getIdLocationEst() != null) {
				direzioni.setNomeLocationEst(locationRepo.findById(direzioni.getIdLocationEst()).get().getNome());
			}
			if (direzioni.getIdLocationSud() != null) {
				direzioni.setNomeLocationSud(locationRepo.findById(direzioni.getIdLocationSud()).get().getNome());
			}
			if (direzioni.getIdLocationOvest() != null) {
				direzioni.setNomeLocationOvest(locationRepo.findById(direzioni.getIdLocationOvest()).get().getNome());
			}
		}
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

	public void resettaLocationMacroMidInner(Location location) {
		location.setRoom(false);
		locationRepo.save(location);
	}
}
