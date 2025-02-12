package come.example.reporting;

import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;

public class ExcelReportStrategy implements ReportStrategy {


    @Override
    public void generate(AbstractReport report, OutputStream stream) throws DRException {
        report.build().toXls(stream);
    }
}
