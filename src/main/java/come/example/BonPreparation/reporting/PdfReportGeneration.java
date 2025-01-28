package come.example.BonPreparation.reporting;

import come.example.BonPreparation.dto.BonPreparationDto;
import come.example.BonPreparation.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PdfReportGeneration<T> implements ReportGenerationType<T> {

    private final OutputStream outputStream;
    private AbstractReport abstractReport;

    public PdfReportGeneration(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @Override
    public void generateReport(AbstractReport abstractReport) throws DRException {
        JasperReportBuilder reportBuilder = abstractReport.build();
        reportBuilder.toPdf(outputStream);
    }
}
