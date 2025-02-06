package come.example.reports.dto;

public class BonPreparationDto {

    private String libelleDepot;
    private String nomChauffeur;
    private Integer codePiece;
    private String famillePiece;
    private String nomPiece;
    private Integer codeClient;
    private String nomClient;
    private Double quantite;

    public BonPreparationDto(String libelleDepot, String nomChauffeur, Integer codePiece, String famillePiece, String nomPiece, Integer codeClient, String nomClient ,Double quantite) {
        this.libelleDepot = libelleDepot;
        this.nomChauffeur = nomChauffeur;
        this.codePiece = codePiece;
        this.famillePiece = famillePiece;
        this.nomPiece = nomPiece;
        this.codeClient = codeClient;
        this.quantite = quantite;
        this.nomClient = nomClient;
    }

    public String getFamillePiece() {
        return famillePiece;
    }

    public void setFamillePiece(String famillePiece) {
        this.famillePiece = famillePiece;
    }

    public String getLibelleDepot() {
        return libelleDepot;
    }

    public void setLibelleDepot(String libelleDepot) {
        this.libelleDepot = libelleDepot;
    }

    public String getNomChauffeur() {
        return nomChauffeur;
    }

    public void setNomChauffeur(String nomChauffeur) {
        this.nomChauffeur = nomChauffeur;
    }

    public Integer getCodePiece() {
        return codePiece;
    }

    public void setCodePiece(Integer codePiece) {
        this.codePiece = codePiece;
    }

    public String getNomPiece() {
        return nomPiece;
    }

    public void setNomPiece(String nomPiece) {
        this.nomPiece = nomPiece;
    }

    public Integer getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(Integer codeClient) {
        this.codeClient = codeClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }
}
