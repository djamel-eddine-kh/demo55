package come.example.BonPreparation.service;


import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import come.example.BonPreparation.dto.BonPreparationDto;
import come.example.BonPreparation.reporting.PdfReportGeneration;
import come.example.BonPreparation.reporting.ReportGenerationContext;
import come.example.BonPreparation.reporting.ReportGenerationType;
import come.example.BonPreparation.reporting.ReportingServiceImpl;
import come.example.BonPreparation.reporting.XlsReportGeneration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class BonPreparationParDepot {

    private final ReportGenerationContext<List<BonPreparationDto>> reportGenerationContext;

    public BonPreparationParDepot() {
        this.reportGenerationContext = new ReportGenerationContext<>();
    }

    public StreamedContent generateReport(String reportFormat, List<BonPreparationDto> data) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // Select the appropriate generation strategy
            ReportGenerationType<List<BonPreparationDto>> strategy = switch (reportFormat) {
                case "PDF" -> new PdfReportGeneration(outputStream);
                case "Excel" -> new XlsReportGeneration(outputStream);
                default -> throw new IllegalArgumentException("Invalid format");
            };

            // Set the strategy and generate the report
            reportGenerationContext.setStrategy(strategy);
            reportGenerationContext.generateReport(new ReportingServiceImpl(data, false));

            // Adjust the file extension for Excel
            String fileName = "report." + (reportFormat.equals("Excel") ? "xls" : reportFormat.toLowerCase());

            return DefaultStreamedContent.builder()
                .name(fileName)
                .contentType(getMimeType(reportFormat))
                .stream(() -> new ByteArrayInputStream(outputStream.toByteArray()))
                .build();

        } catch (Exception e) {
            throw new RuntimeException("Report generation failed", e);
        }
    }


    private String getMimeType(String format) {
        return switch (format) {
            case "PDF" -> "application/pdf";
            case "Excel" -> "application/vnd.ms-excel";
            default -> "application/octet-stream";
        };
    }
}