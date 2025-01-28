package come.example.BonPreparation.reporting;

import net.sf.dynamicreports.report.exception.DRException;
import org.apache.poi.ss.formula.functions.T;

import come.example.BonPreparation.reporting.template.AbstractReport;

import java.util.List;

public interface ReportGenerationType<T> {
    void generateReport(AbstractReport abstractReport) throws DRException;
}
