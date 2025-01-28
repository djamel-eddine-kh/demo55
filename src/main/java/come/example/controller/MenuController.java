package come.example.controller;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.enterprise.context.SessionScoped;
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
            default:
                logger.info(value + " clicked - no action defined");
                break;
        }
    }
}
