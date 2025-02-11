package come.example;

import java.util.ResourceBundle;


import jakarta.faces.context.FacesContext;




public class test {

	public static void main(String[ ] args) {
		System.out.println("test" +test.getResourceLabel("produit"));
	}
	public static String getResourceLabel(String key) {
		if (key == null || key.trim().isEmpty()) {
	        return ""; // Return empty if key is null or blank
	    }
		try {
			FacesContext context = FacesContext.getCurrentInstance();

			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "var");
			return bundle.getString(key.trim());
		} catch (Exception e) {
			
			return "";
		}

	}
}
