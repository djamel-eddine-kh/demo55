package come.example.dto;

public class ArticleDto {
    private String nomDepot;
    private String nomArticle;
    private String etatStock;
    private String etatSortie;
    private String etatEntree;

    public ArticleDto(String nomDepot, String nomArticle, String etatStock, String etatSortie, String etatEntree) {
        this.nomDepot = nomDepot;
        this.nomArticle = nomArticle;
        this.etatStock = etatStock;
        this.etatSortie = etatSortie;
        this.etatEntree = etatEntree;
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

    public String getEtatStock() {
        return etatStock;
    }

    public void setEtatStock(String etatStock) {
        this.etatStock = etatStock;
    }

    public String getEtatSortie() {
        return etatSortie;
    }

    public void setEtatSortie(String etatSortie) {
        this.etatSortie = etatSortie;
    }

    public String getEtatEntree() {
        return etatEntree;
    }

    public void setEtatEntree(String etatEntree) {
        this.etatEntree = etatEntree;
    }
}
