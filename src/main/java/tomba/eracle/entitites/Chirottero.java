package tomba.eracle.entitites;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "chirotteri")
@Data
public class Chirottero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_mittente")
	private Personaggio mittente;
	
	@ManyToOne
	@JoinColumn(name = "id_destinatario")
	private Personaggio destinatario;
	
	@Column(name = "testo")
	private String testo;
	
	@Column(name = "data_invio")
	private LocalDate dataInvio;
	
	@Column(name = "letto")
	private boolean letto = false;
	
	@Column(name = "data_lettura")
	private LocalDateTime dataLettura;
	
	
}
