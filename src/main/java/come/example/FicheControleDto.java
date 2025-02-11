package come.example;

public class FicheControleDto {
    private String dateDC;
    private String dateDF;
    private String codeArticle;
    private String designation;
    private String numLot;
    private Double quantity;

    public FicheControleDto(String dateDC, String dateDF, String codeArticle, String designation, String numLot, Double quantity) {
        this.dateDC = dateDC;
        this.dateDF = dateDF;
        this.codeArticle = codeArticle;
        this.designation = designation;
        this.numLot = numLot;
        this.quantity = quantity;
    }

    public String getDateDC() {
        return dateDC;
    }

    public void setDateDC(String dateDC) {
        this.dateDC = dateDC;
    }

    public String getDateDF() {
        return dateDF;
    }

    public void setDateDF(String dateDF) {
        this.dateDF = dateDF;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getNumLot() {
        return numLot;
    }

    public void setNumLot(String numLot) {
        this.numLot = numLot;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}