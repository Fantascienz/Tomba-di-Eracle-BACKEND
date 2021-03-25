package tomba.eracle.entitites;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@OneToMany(mappedBy = "utente")
	@JsonIgnore
	private List<Personaggio> personaggi;
	
	@Transient
	private int numeroPersonaggi;
	
}
