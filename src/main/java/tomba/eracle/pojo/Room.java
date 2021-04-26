package tomba.eracle.pojo;

import lombok.Data;
import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;

@Data
public class Room {
	private Location location;
	private Location locationUmbra;
	private Direzione direzioni;
	private Direzione direzioniUmbra;
	private Location superLocation;
}
