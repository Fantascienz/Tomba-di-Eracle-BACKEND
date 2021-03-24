package tomba.eracle.pojo;

import lombok.Data;
import tomba.eracle.entitites.Utente;

@Data
public class ModificaUtente {

	private Utente utente;
	
	private String vecchiaPsw;
}
