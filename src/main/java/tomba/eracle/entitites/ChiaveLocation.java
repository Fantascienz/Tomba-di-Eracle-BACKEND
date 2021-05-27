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
@Table(name = "chiavi_location")
public class ChiaveLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_location")
	private Location location;

	@Column(name = "chiave")
	private String chiave;

	public ChiaveLocation(Location location, String chiave) {
		this.location = location;
		this.chiave = chiave;
	}

	public ChiaveLocation() {

	}

}
