package come.example.dto;

public class Depotdto {
    private String nomDepot;
    private String locationArticle;
    private String nomArticle;
    private String numeroSerie;

    public Depotdto() {

    }

    public String getLocationArticle() {
        return locationArticle;
    }

    public void setLocationArticle(String locationArticle) {
        this.locationArticle = locationArticle;
    }

    private Double articleQuantite;

    public Depotdto(String nomDepot, String locationArticle ,String nomArticle, String numeroSerie, Double articleQuantite) {
        this.nomDepot = nomDepot;
        this.locationArticle = locationArticle;
        this.nomArticle = nomArticle;
        this.numeroSerie = numeroSerie;
        this.articleQuantite = articleQuantite;
    }

    public String getNomDepot() {
        return nomDepot;
    }

    public void setNomDepot(String nomDepot) {
        this.nomDepot = nomDepot;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Double getArticleQuantite() {
        return articleQuantite;
    }

    public void setArticleQuantite(Double articleQuantite) {
        this.articleQuantite = articleQuantite;
    }
}
