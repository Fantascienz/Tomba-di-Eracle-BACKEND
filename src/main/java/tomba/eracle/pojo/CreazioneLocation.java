package tomba.eracle.pojo;

import lombok.Data;
import tomba.eracle.entitites.Location;

@Data
public class CreazioneLocation {

	private Location location;
	
	private String ingresso;
	
	private Long idLocationIngresso;
	
	private String direzioneIngresso;
	
	private Umbra umbra;
}
