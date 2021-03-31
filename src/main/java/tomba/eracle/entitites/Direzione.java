package tomba.eracle.entitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "direzioni")
public class Direzione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;	
	
	@Column(name = "id_location")
	private Long idLocation;
	
	@Column(name = "id_location_nord")
	private Long idLocationNord;
	
	@Column(name = "id_location_est")
	private Long idLocationEst;
	
	@Column(name = "id_location_sud")
	private Long idLocationSud;
	
	@Column(name = "id_location_ovest")
	private Long idLocationOvest;
	
	@Column(name = "id_location_specchio")
	private Long idLocationSpecchio;
	
}
