package come.example.reports.dto;

public class reportValuesDto {
	private Integer cle;
	private String value;
	private String description;

	public reportValuesDto(int i, String description, String value) {
		this.cle = i;
		this.value = value;
		this.description = description ;
	}

	public Integer getCle() {
		return cle;
	}
	public void setCle(Integer cle) {
		this.cle = cle;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String  value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
