package tomba.eracle.pojo;

import lombok.Data;
import tomba.eracle.entitites.Direzione;
import tomba.eracle.entitites.Location;

@Data
public class Room {
	private Location location; //1300
	private Location locationUmbra; // 1348
	private Direzione direzioni;
	private Direzione direzioniUmbra;
	private Location superLocation; //300
	                                // super umbra = 348
}
