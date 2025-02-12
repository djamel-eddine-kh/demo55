package come.example.dto;

public class FicheControleParametreDto {
    private String verificationParamters;
    private String methode;
    private String unite;
    private String plageReference;
    private String valeurMesure;

    public FicheControleParametreDto(String verificationParamters, String methode, String unite, String plageReference, String valeurMesure) {
        this.verificationParamters = verificationParamters;
        this.methode = methode;
        this.unite = unite;
        this.plageReference = plageReference;
        this.valeurMesure = valeurMesure;
    }

    public String getVerificationParamters() {
        return verificationParamters;
    }

    public void setVerificationParamters(String verificationParamters) {
        this.verificationParamters = verificationParamters;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getPlageReference() {
        return plageReference;
    }

    public void setPlageReference(String plageReference) {
        this.plageReference = plageReference;
    }

    public String getValeurMesure() {
        return valeurMesure;
    }

    public void setValeurMesure(String valeurMesure) {
        this.valeurMesure = valeurMesure;
    }
}
