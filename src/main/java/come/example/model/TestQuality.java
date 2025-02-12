package come.example.model;

import java.sql.Date;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_qualite")
public class TestQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Changed to an Integer id for proper generated value handling

    private Date dateDC;
    private Date dateDF;
    private String codeArticle;
    private String designation;
    private String numLot;
    private Double quantity;
    private String type;  // Indicates whether this record uses a predefined template ("TEMPLATE") or is custom ("CUSTOM")

    // One-to-many relationship with LigneTestDeQualite.
    // Note: mappedBy is now updated to "testQuality" (the field name in the child entity)
    @OneToMany(mappedBy = "testQuality", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneTestDeQualite> lignesTest;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
 
    public Date getDateDC() {
		return dateDC;
	}
	public void setDateDC(Date dateDC) {
		this.dateDC = dateDC;
	}
	public Date getDateDF() {
		return dateDF;
	}
	public void setDateDF(Date dateDF) {
		this.dateDF = dateDF;
	}
	public String getCodeArticle() {
        return codeArticle;
    }
    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getNumLot() {
        return numLot;
    }
    public void setNumLot(String numLot) {
        this.numLot = numLot;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<LigneTestDeQualite> getLignesTest() {
        return lignesTest;
    }
    public void setLignesTest(List<LigneTestDeQualite> lignesTest) {
        this.lignesTest = lignesTest;
    }
}
