package tomba.eracle.entitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@Transient
	private String nomeLocationNord;
	
	@Column(name = "id_location_est")
	private Long idLocationEst;
	
	@Transient
	private String nomeLocationEst;
	
	@Column(name = "id_location_sud")
	private Long idLocationSud;
	
	@Transient
	private String nomeLocationSud;
	
	@Column(name = "id_location_ovest")
	private Long idLocationOvest;
	
	@Transient
	private String nomeLocationOvest;
	
	@Column(name = "id_location_specchio")
	private Long idLocationSpecchio;
	
	@Transient
	private String nomeLocationSpecchio;
	
}
