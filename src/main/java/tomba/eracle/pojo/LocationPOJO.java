package tomba.eracle.pojo;

import lombok.Data;
import tomba.eracle.entitites.Location;

@Data
public class LocationPOJO {

	private Location location;
	
	private String ingresso;
	
	private Long idLocationIngresso;
	
	private String direzioneIngresso;
	
	private String direzioneUscita;
	
	private Umbra umbra;
	
	private Long superLocation;
	
	private Long meteoGiorno;
	
	private Long meteoNotte;
}
