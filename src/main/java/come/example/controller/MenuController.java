package come.example.controller;

import java.io.Serializable;


import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import come.example.test;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;


@Named
@ViewScoped
public class MenuController implements Serializable {
    private static final long serialVersionUID = 1L;

    private String activePage = "/ListComponent.xhtml";  // Default page
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    public String getActivePage() {
        return activePage;
    }

    public void setActivePage(String page) {
        this.activePage = page;
    }

    // Update the activePage without returning a navigation outcome
    public void getWay(int value){
        switch (value) {
            case 1:
                logger.info(value + " clicked - navigating to ListComponent");
                this.activePage = "/ListComponent.xhtml";
                break;
            case 2:
                logger.info(value + " clicked - navigating to Welcome");
                this.activePage = "/welcomeComp.xhtml";
                break;
            case 3:
                logger.info(value + " clicked - navigating to reference");
                this.activePage = "/ui/Reference.xhtml";
                break;

            default:
                logger.info(value + " clicked - no action defined");
                break;
        }
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
