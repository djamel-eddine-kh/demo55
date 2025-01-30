package come.example.controller;

import java.io.ByteArrayOutputStream;



import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import come.example.model.Data;
import come.example.service.DataServiceImpl;
import come.example.service.ReportService;
@Named
@ViewScoped
public class DataController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    private List<Data> dataList;
    private List<Object[]> list;
    private Data data;
    private String pdfUrl;
    
    private final DataServiceImpl dataService;
    private final ReportService reportService;
    private Data selectedData; 
    @Autowired
    public DataController(DataServiceImpl dataService, ReportService reportService) {
        this.dataService = dataService;
        this.reportService = reportService;
    }

    @PostConstruct
    public void init() {
        try {
        	selectedData = new Data();
            data = new Data();
           
            		dataList = dataService.dataList();
            logger.info("Controller initialized with {} records.", dataList.size());
        } catch (Exception e) {
            logger.error("Error initializing controller: {}", e.getMessage());
        }
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

        public void addData() {
            try {
                if (selectedData.getName() == null || selectedData.getName().trim().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Name is required."));
                    return;
                }
                dataService.addData(selectedData);
                dataList = dataService.dataList();
                selectedData = new Data(); // Reset for new entry
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Data added successfully."));
            } catch (Exception e) {
                logger.error("Error adding data: {}", e.getMessage());
            }
        }

        public void deleteData(Data data) {
            try {
                dataService.deleteData(data);
                dataList = dataService.dataList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Data deleted successfully."));
            } catch (Exception e) {
                logger.error("Error deleting data: {}", e.getMessage());
            }
        }

        public void updateData() {
            try {
                dataService.updateData(selectedData);
                dataList = dataService.dataList();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Data updated successfully."));
            } catch (Exception e) {
                logger.error("Error updating data: {}", e.getMessage());
            }
        }

        public void saveData() {
            try {
                if (selectedData.getId() == null) {
                    // Add new data
                    dataService.addData(selectedData);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Data added successfully."));
                } else {
                    // Update existing data
                    dataService.updateData(selectedData);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Data updated successfully."));
                }
                loadData(); // Reload the data list
                selectedData = new Data(); // Reset for new entry
            } catch (Exception e) {
                logger.error("Error saving data: {}", e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to save data."));
            }
        }


    // Getter and setter for selectedData
    public Data getSelectedData() {
        return selectedData;
    }

    public void setSelectedData(Data selectedData) {
        this.selectedData = selectedData;
    }

   
    public void generateReport(String reportType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try (ByteArrayOutputStream outputStream = reportService.generateReportToStream(dataList, reportType)) {
            if (outputStream != null) {
                // Prepare the response
            	if(reportType.equals("xls")) {
                String fileName = "report." + reportType;
                externalContext.responseReset();
                externalContext.setResponseContentType("application/vnd.ms-excel");
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                // Write file content
                externalContext.getResponseOutputStream().write(outputStream.toByteArray());
                externalContext.getResponseOutputStream().flush();
                facesContext.responseComplete();
            	}
            	else {
            	    pdfUrl = generatePdfResource(outputStream.toByteArray(),reportType);
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Report generated successfully."));
                    logger.info("PDF report generated. URL: {}", pdfUrl);
            	}
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not generate the report.");
            facesContext.addMessage(null, message);
            e.printStackTrace();
        }
    }

    private String generatePdfResource(byte[] pdfData,String reporType) {
        try {
            // Get the resources directory path
            String resourcesDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/temp");
            File tempDir = new File(resourcesDir);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Generate the temp file name
            String tempFileName = "temp-report-" + UUID.randomUUID() + "."+reporType;
            File tempFile = new File(tempDir, tempFileName);

            // Write the PDF to the temp file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(pdfData);
            }

            // Construct the base URL dynamically
            String baseUrl = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            return baseUrl + "/temp/" + tempFileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getPdfUrl() {
        return pdfUrl;
    }
    public void openNew() {
        this.selectedData = new Data();
    }
    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
    private void loadData() {
        dataList = dataService.dataList();
    }
}