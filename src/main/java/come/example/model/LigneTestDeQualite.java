package come.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LigneTestDeQualite {

	@Id
	@GeneratedValue
	private Integer id ;
	private String plage_reference;
	private String valeur_mesure;
	

}
