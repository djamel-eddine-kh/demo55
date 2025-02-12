package come.example.dto;

public class RecapClientDto {


	private String nomClient;
	private String nom;
    private String prenom;
    private String typePrix;
    private String typeActiviteClient;
    private Double soldeDebutJournee;
    private Double totalVente;
    private Double totalLivraison;
    private Double totalRetour;
    private Double totalVersement;
    private Double totalVersementGlobal;
    private Double soldeFinJournee;
    private Double ecartSolde;

    public RecapClientDto(Integer nomCamion,String nomClient ,String nom, String prenom, String typePrix, String typeActiviteClient, Double soldeDebutJournee, Double totalVente, Double totalLivraison, Double totalRetour, Double totalVersement, Double totalVersementGlobal, Double soldeFinJournee, Double ecartSolde) {
        this.camionNumber=nomCamion;
        this.nomClient=nomClient;
        this.nom = nom;
        this.prenom = prenom;
        this.typePrix = typePrix;
        this.typeActiviteClient = typeActiviteClient;
        this.soldeDebutJournee = soldeDebutJournee;
        this.totalVente = totalVente;
        this.totalLivraison = totalLivraison;
        this.totalRetour = totalRetour;
        this.totalVersement = totalVersement;
        this.totalVersementGlobal = totalVersementGlobal;
        this.soldeFinJournee = soldeFinJournee;
        this.ecartSolde = ecartSolde;
    }
	private Integer camionNumber;
	public void setCamionNumber(Integer camionNumber) {
		this.camionNumber = camionNumber;
	}
    // Getters and Setters

    public Integer getCamionNumber() {
		return camionNumber;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

    public Double getTotalVersementGlobal() {
		return totalVersementGlobal;
	}

	public void setTotalVersementGlobal(Double totalVersementGlobal) {
		this.totalVersementGlobal = totalVersementGlobal;
	}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTypePrix() {
        return typePrix;
    }

    public void setTypePrix(String typePrix) {
        this.typePrix = typePrix;
    }

    public String getTypeActiviteClient() {
        return typeActiviteClient;
    }

    public void setTypeActiviteClient(String typeActiviteClient) {
        this.typeActiviteClient = typeActiviteClient;
    }

    public Double getSoldeDebutJournee() {
        return soldeDebutJournee;
    }

    public void setSoldeDebutJournee(Double soldeDebutJournee) {
        this.soldeDebutJournee = soldeDebutJournee;
    }

    public Double getTotalVente() {
        return totalVente;
    }

    public void setTotalVente(Double totalVente) {
        this.totalVente = totalVente;
    }

    public Double getTotalLivraison() {
        return totalLivraison;
    }

    public void setTotalLivraison(Double totalLivraison) {
        this.totalLivraison = totalLivraison;
    }

    public Double getTotalRetour() {
        return totalRetour;
    }

    public void setTotalRetour(Double totalRetour) {
        this.totalRetour = totalRetour;
    }

    public Double getTotalVersement() {
        return totalVersement;
    }

    public void setTotalVersement(Double totalVersement) {
        this.totalVersement = totalVersement;
    }


    public Double getSoldeFinJournee() {
        return soldeFinJournee;
    }

    public void setSoldeFinJournee(Double soldeFinJournee) {
        this.soldeFinJournee = soldeFinJournee;
    }

    public Double getEcartSolde() {
        return ecartSolde;
    }

    public void setEcartSolde(Double ecartSolde) {
        this.ecartSolde = ecartSolde;
    }
}
