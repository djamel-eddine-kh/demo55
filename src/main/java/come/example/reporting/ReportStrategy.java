package come.example.reporting;

import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;

public interface ReportStrategy<T> {
    void generate(AbstractReport report, OutputStream stream) throws DRException;
}
