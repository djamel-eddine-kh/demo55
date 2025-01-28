package come.example.service;

import come.example.model.Data;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Service
public class ReportService implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ReportType {
        PDF, XLS
    }

    public ByteArrayOutputStream generateReportToStream(List<Data> data, String reportType) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Validate report type
            ReportType type = validateReportType(reportType);

            // Styles
            StyleBuilder boldStyle = DynamicReports.stl.style().bold();
            StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
            StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
                    .setBorder(DynamicReports.stl.pen1Point())
                    .setBackgroundColor(Color.LIGHT_GRAY);

            // Columns
            TextColumnBuilder<String> nameColumn = DynamicReports.col.column("Name", "name", DynamicReports.type.stringType()).setStyle(boldStyle);
            TextColumnBuilder<Integer> ageColumn = DynamicReports.col.column("Age", "age", DynamicReports.type.integerType());

            // Report builder
            var reportBuilder = DynamicReports.report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(nameColumn, ageColumn)
                    .setDataSource(data)
                    .title(DynamicReports.cmp.text("Employee Report").setStyle(boldCenteredStyle))
                    .pageFooter(DynamicReports.cmp.pageXofY().setStyle(boldCenteredStyle));

            // Generate based on report type
            if (type == ReportType.PDF) {
                reportBuilder.toPdf(outputStream);
            } else if (type == ReportType.XLS) {
                reportBuilder.toXls(outputStream);
            }

            return outputStream;
        } catch (DRException e) {
            throw new IOException("Error generating report", e);
        }
    }

    private ReportType validateReportType(String reportType) {
        try {
            return ReportType.valueOf(reportType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid report type: " + reportType + ". Valid types are 'pdf' and 'xls'.", e);
        }
    }
}
