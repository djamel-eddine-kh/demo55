package come.example.dto;

public class CompanyInfoDto {
    private String logoPath;
    private String nic;
    private String nif;
    private String rc;
    private String address;

    // Constructor
    public CompanyInfoDto(String logoPath, String nic, String nif, String rc, String address) {
        this.logoPath = logoPath;
        this.nic = nic;
        this.nif = nif;
        this.rc = rc;
        this.address = address;
    }

    public CompanyInfoDto() {

    }

    // Getters and Setters
    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
