package tomba.eracle.entitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "personaggi")
public class Personaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nominativo")
	private String nominativo;
	
	@Column(name = "sesso")
	private String sesso;
	
	@Column(name = "razza")
	private String razza;
	
	@Column(name = "rango")
	private Integer rango;
	
	@Column(name = "url_immagine")
	private String urlImmagine;
	
	@Column(name = "nome_garou")
	private String nomeGarou;
	
	@Column(name = "auspicio")
	private String auspicio;
	
	@Column(name = "tribu")
	private String tribu;
	
	@Column(name = "branco")
	private String branco;
	
	@Column(name = "sept")
	private String sept;
	
	@Column(name = "url_img_lupo")
	private String urlLupo;
	
	@Column(name = "url_img_crinos")
	private String urlCrinos;
	
	@Column(name = "umbra")
	private boolean umbra;
	
	@Column(name = "chirottero")
	private boolean chirottero;
	
	@ManyToOne
	@JoinColumn(name = "id_utente")
	private Utente utente;
}
