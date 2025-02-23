package come.example.reporting;

import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;

public class PdfReportStrategy implements ReportStrategy {
    @Override
    public void generate(AbstractReport report, OutputStream stream) throws DRException {
        report.build().toPdf(stream);
    }
}