package come.example.reporting.reports;


import come.example.reporting.ExcelReportStrategy;
import come.example.reporting.PdfReportStrategy;
import come.example.reporting.ReportStrategy;
import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.report.exception.DRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class Reportservice {
    private final Map<String, ReportStrategy> strategies = Map.of(
            "PDF", new PdfReportStrategy(),
            "Excel", new ExcelReportStrategy()
    );

    public StreamedContent generateReport(String format, AbstractReport report, String filePath) throws DRException, IOException {
        validateFormat(format);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        strategies.get(format).generate(report, outputStream);

        String extension = format.equalsIgnoreCase("Excel") ? "xls" : format.toLowerCase();
        String fileName = "report." + extension;
        File directory = new File(filePath);

        // Ensure directory exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save file
        File outputFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(outputStream.toByteArray());
        }

        // Return file as a downloadable stream
        return DefaultStreamedContent.builder()
                .name(fileName)
                .contentType(getMimeType(format))
                .stream(() -> {
                    try {
                        return new FileInputStream(outputFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException("Error opening file stream", e);
                    }
                })
                .build();
    }



    private void validateFormat(String format) {
        if (!strategies.containsKey(format)) {
            throw new IllegalArgumentException("Unsupported report format: " + format);
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
