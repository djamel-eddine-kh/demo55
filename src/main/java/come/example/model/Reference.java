package come.example.model;

import javax.persistence.*;

@Entity
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String libelle;
    private String designation1;
    private String designation2;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDesignation1() { return designation1; }
    public void setDesignation1(String designation1) { this.designation1 = designation1; }

    public String getDesignation2() { return designation2; }
    public void setDesignation2(String designation2) { this.designation2 = designation2; }
}
