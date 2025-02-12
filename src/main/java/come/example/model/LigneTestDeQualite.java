package come.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ligne_test_de_qualite")
public class LigneTestDeQualite {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String plage_reference;
    private String valeur_mesure;

    // Many-to-one relationship to TestQuality
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_quality_id")
    private TestQuality testQuality;

    // Many-to-one relationship with Reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Reference reference;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPlage_reference() {
        return plage_reference;
    }
    public void setPlage_reference(String plage_reference) {
        this.plage_reference = plage_reference;
    }
    public String getValeur_mesure() {
        return valeur_mesure;
    }
    public void setValeur_mesure(String valeur_mesure) {
        this.valeur_mesure = valeur_mesure;
    }
    public TestQuality getTestQuality() {
        return testQuality;
    }
    public void setTestQuality(TestQuality testQuality) {
        this.testQuality = testQuality;
    }
    public Reference getReference() {
        return reference;
    }
    public void setReference(Reference reference) {
        this.reference = reference;
    }
}
