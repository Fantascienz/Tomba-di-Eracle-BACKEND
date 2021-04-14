package tomba.eracle.entitites;

import java.time.LocalDate;

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
@Table(name = "messaggi")
@Data
public class Messaggio {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_admin")
	private Utente admin;
	
	@ManyToOne
	@JoinColumn(name = "id_utente")
	private Utente utente;
	
	@Column(name = "testo")
	private String testo;
	
	@Column(name = "inviato_da")
	private String inviatoDa;
	
	@Column(name = "inviato_alle")
	private LocalDate inviatoAlle;
	

}
