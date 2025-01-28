package come.example.BonPreparation.controller;


import java.util.List;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import come.example.BonPreparation.dto.BonPreparationDto;
import come.example.BonPreparation.service.ArticleService;
import come.example.BonPreparation.service.BonPreparationParDepot;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ReportDownloadBean {

    private final BonPreparationParDepot reportGenerator;
    
 
    private final ArticleService articleService;
    
    private StreamedContent pdfFile;
    private StreamedContent excelFile;
    @Autowired
 public ReportDownloadBean(BonPreparationParDepot reportGenerator,ArticleService articleService) {
	 this.articleService=articleService;
	 this.reportGenerator=reportGenerator;
	 
 }
    public void downloadPdf() {
        List<BonPreparationDto> data = articleService.getInvoice();
        pdfFile = reportGenerator.generateReport("PDF", data);
    }

    public void downloadExcel() {
        List<BonPreparationDto> data = articleService.getInvoice();
        excelFile = reportGenerator.generateReport("Excel", data);
    }

    // Getters
    public StreamedContent getPdfFile() { return pdfFile; }
    public StreamedContent getExcelFile() { return excelFile; }
}