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
@Table(name = "locations")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;	
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "ambiente")
	private String ambiente;
	
	@Column(name = "url_img_giorno")
	private String urlImgGiorno;
	
	@Column(name = "url_img_notte")
	private String urlImgNotte;
	
	@Column(name = "url_img_minimappa")
	private String urlMinimappa;
	
	@Column(name = "url_audio")
	private String urlAudio;
	
	@Column(name = "mappa")
	private String mappa;
	
	@Column(name = "chiave")
	private String chiave;
	
	@ManyToOne
	@JoinColumn(name = "id_creatore")
	private Utente creatore; 
	
	
	}