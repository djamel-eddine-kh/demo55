package come.example.controller;

import come.example.model.TestQuality;
import come.example.model.LigneTestDeQualite;
import come.example.model.Reference;
import come.example.service.TestQualityService;
import come.example.service.LigneTestDeQualiteService;
import come.example.service.ReferenceService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Named
@ViewScoped
public class TestDeQualiteController implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<TestQuality> testQualityList;
	private TestQuality selectedTestQuality = new TestQuality();

	private LigneTestDeQualite selectedLigneTest = new LigneTestDeQualite();

	private List<Reference> referenceList;
	private Reference selectedReference = new Reference();

	@Autowired
	private TestQualityService testQualityService;
	@Autowired
	private LigneTestDeQualiteService ligneTestDeQualiteService;
	@Autowired
	private ReferenceService referenceService;
    
	
	public TestDeQualiteController(LigneTestDeQualiteService ligneTestDeQualiteService,TestQualityService testQualityService, ReferenceService reference) {
		this.testQualityService = testQualityService;
		this.ligneTestDeQualiteService = ligneTestDeQualiteService;
		this.referenceService = reference;
		
	}
	@PostConstruct
	public void init() {
		loadTestQualityList();
		loadReferenceList();
	}

	public void loadTestQualityList() {
		testQualityList = testQualityService.testQualityList();
	}

	public void loadReferenceList() {
		referenceList = referenceService.getReferenceList();
	}

	// TestQuality CRUD methods
	public void saveTestQuality() {
		try {
			if (selectedTestQuality.getId() == null) {
				testQualityService.addTest(selectedTestQuality);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Test Quality added successfully"));
			} else {
				testQualityService.updateTest(selectedTestQuality);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Test Quality updated successfully"));
			}
			loadTestQualityList();
			selectedTestQuality = new TestQuality();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not save Test Quality"));
			e.printStackTrace();
		}
	}

	public void deleteTestQuality(TestQuality quality) {
		try {
			testQualityService.deleteTestQuality(quality);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Test Quality deleted successfully"));
			loadTestQualityList();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not delete Test Quality"));
			e.printStackTrace();
		}
	}

	public void setSelectedTestQuality(TestQuality quality) {
		this.selectedTestQuality = quality;
	}

	// LigneTestDeQualite CRUD methods
	public void saveLigneTest() {
		try {
			if (selectedLigneTest.getId() == null) {
				// Make sure the test line is linked to the currently selected TestQuality
				selectedLigneTest.setTestQuality(selectedTestQuality);
				ligneTestDeQualiteService.addLigne(selectedLigneTest);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Test Line added successfully"));
			} else {
				ligneTestDeQualiteService.updateLigneTest(selectedLigneTest);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Test Line updated successfully"));
			}
			// Refresh the test line list (assuming TestQuality entity is refreshed)
			loadTestQualityList();
			selectedLigneTest = new LigneTestDeQualite();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not save Test Line"));
			e.printStackTrace();
		}
	}

	public void deleteLigneTest(LigneTestDeQualite ligne) {
		try {
			ligneTestDeQualiteService.deleteLigneTest(ligne);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Test Line deleted successfully"));
			loadTestQualityList();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not delete Test Line"));
			e.printStackTrace();
		}
	}

	// Reference CRUD methods
	public void addReference() {
	    System.out.println("Entering saveReference method" +selectedReference.toString()s);

	    try {
	        if (selectedReference.getId() == null) {
	            System.out.println("Saving new reference");
	            referenceService.addReference(selectedReference);
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Reference added successfully"));
	        } else {
	            System.out.println("Updating reference");
	            referenceService.updateReference(selectedReference);
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Reference updated successfully"));
	        }

	        loadReferenceList();
	        selectedReference = new Reference();
	    } catch (Exception e) {
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not save Reference"));
	        e.printStackTrace();
	    }
	}

	public void deleteReference(Reference ref) {
		try {
			referenceService.deleteReference(ref);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Reference deleted successfully"));
			loadReferenceList();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not delete Reference"));
			e.printStackTrace();
		}
	}

	// Getters and setters for properties
	public List<TestQuality> getTestQualityList() {
		return testQualityList;
	}

	public void setTestQualityList(List<TestQuality> testQualityList) {
		this.testQualityList = testQualityList;
	}

	public TestQuality getSelectedTestQuality() {
		return selectedTestQuality;
	}

	public void setSelectedTest(TestQuality selectedTestQuality) {
		this.selectedTestQuality = selectedTestQuality;
	}

	public LigneTestDeQualite getSelectedLigneTest() {
		return selectedLigneTest;
	}

	public void setSelectedLigneTest(LigneTestDeQualite selectedLigneTest) {
		this.selectedLigneTest = selectedLigneTest;
	}

	public List<Reference> getReferenceList() {
		return referenceList;
	}

	public void setReferenceList(List<Reference> referenceList) {
		this.referenceList = referenceList;
	}

	public Reference getSelectedReference() {
		return selectedReference;
	}

	public void setSelectedReference(Reference reference) {
	    this.selectedReference = reference;
	    System.out.println("Selected Reference: " + reference);
	}

}
