package come.example.reports.dto;

import java.util.List;

public class BonSortieDto {
    private String nomClient;
    private String DescriptionClient;
    private String nomProduit;
    private Double ecart;

    public Double getEcart() {
        return ecart;
    }

    public void setEcart(Double ecart) {
        this.ecart = ecart;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescriptionClient() {
        return DescriptionClient;
    }

    public void setDescriptionClient(String descriptionClient) {
        DescriptionClient = descriptionClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setCategories(List<CategoryData> categories) {
        this.categories = categories;
    }

    private List<CategoryData> categories;


    public BonSortieDto(String nomClient, String descriptionClient, String nomProduit, Double ecart ,List<CategoryData> categories) {
        this.ecart = ecart;
        this.categories = categories;
        this.nomClient = nomClient;
        this.nomProduit = nomProduit;
        DescriptionClient = descriptionClient;
    }

    public BonSortieDto(String nomClient, String descriptionClient, String nomProduit, List<CategoryData> categories) {
        this.nomClient = nomClient;
        DescriptionClient = descriptionClient;
        this.nomProduit = nomProduit;
        this.categories = categories;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public static class CategoryData {
        private String category;
        private String subCategory;
        private String measureType;
        private Double value;

        public CategoryData(String category, String subCategory, String measureType, Double value) {
            this.category = category;
            this.subCategory = subCategory;
            this.measureType = measureType;
            this.value = value;
        }

        public String getCategory() { return category; }
        public String getSubCategory() { return subCategory; }
        public String getMeasureType() { return measureType; }
        public Double getValue() { return value; }
    }
}
