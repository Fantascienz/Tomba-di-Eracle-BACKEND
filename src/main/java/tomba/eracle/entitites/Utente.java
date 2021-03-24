package tomba.eracle.entitites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "utenti")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nominativo")
	private String nominativo;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "psw")
	private String psw;
	
	@Column(name = "tipo")
	private String tipo;
	
}
