package come.example.BonPreparation.reporting;

import net.sf.dynamicreports.report.exception.DRException;
import org.apache.poi.ss.formula.functions.T;

import come.example.BonPreparation.reporting.template.AbstractReport;

import java.util.List;

public class ReportGenerationContext<T> {
    private ReportGenerationType reportGenerationstrategy;

    public void setStrategy(ReportGenerationType<T> strategy) {
        this.reportGenerationstrategy = strategy;
    }
    public void generateReport(AbstractReport abstractReport) throws DRException {
        if (reportGenerationstrategy == null) {
            throw new IllegalStateException("Report generation strategy is not set!");
        }
        reportGenerationstrategy.generateReport(abstractReport);
    }

}
