package come.example.BonPreparation.reporting.template;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public abstract class AbstractReport {
    // Constants for consistent styling
    protected static final Color HEADER_BACKGROUND = Color.LIGHT_GRAY;
    protected static final int DEFAULT_PADDING = 10;

    // Styles
    protected static final StyleBuilder rootStyle = stl.style().setPadding(2);
    protected static final StyleBuilder boldStyle = stl.style(rootStyle).bold();
    protected static final StyleBuilder italicStyle = stl.style(rootStyle).italic();
    protected static final StyleBuilder boldCenteredStyle = stl.style(boldStyle)
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    protected static final StyleBuilder bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
    protected static final StyleBuilder bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
    protected static final StyleBuilder bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
    protected static final StyleBuilder columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
    public static final StyleBuilder columnTitleStyle = stl.style(columnStyle)
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(Color.LIGHT_GRAY)
            .bold();
    public static final StyleBuilder groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    public static final StyleBuilder subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

    // Template
    public static final ReportTemplateBuilder reportTemplate;

    // Components
    protected static final ComponentBuilder<?, ?> footerComponent;
    protected static final ComponentBuilder<?, ?> dynamicReportsComponent;

    static {
        // Configure the template
        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
                .setHeadingStyle(0, stl.style(rootStyle).bold());

        reportTemplate = template()
                .setLocale(Locale.FRENCH)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .setGroupStyle(groupStyle)
                .setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle)
                .highlightDetailEvenRows()
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(stl.style(columnTitleStyle))
                .setCrosstabGroupTotalStyle(stl.style(columnTitleStyle).setBackgroundColor(new Color(170, 170, 170)))
                .setCrosstabGrandTotalStyle(stl.style(columnTitleStyle).setBackgroundColor(new Color(140, 140, 140)))
                .setCrosstabCellStyle(stl.style(columnStyle).setBorder(stl.pen1Point()))
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);

        HyperLinkBuilder link = hyperLink("http://www.dynamicreports.org");
        dynamicReportsComponent = cmp.horizontalList(
                cmp.image(AbstractReport.class.getResource("images/dynamicreports.png")).setFixedDimension(60, 60),
                cmp.verticalList(
                        cmp.text("DynamicReports").setStyle(bold22CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                        cmp.text("http://www.dynamicreports.org").setStyle(italicStyle).setHyperLink(link)
                )).setFixedWidth(300);

        footerComponent = cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
    }

    // Abstract methods
    public abstract JasperReportBuilder build() throws DRException;

    protected abstract ComponentBuilder<?, ?> createTitleComponent();

    protected abstract ComponentBuilder<?, ?> createPageHeaderComponent();

    protected abstract ComponentBuilder<?, ?> createDetailComponent();

    // Common reusable components
    protected ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.horizontalList()
                .add(dynamicReportsComponent, cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(10));
    }

    protected DRDataSource createDummyDataSource() {
        DRDataSource dataSource = new DRDataSource("dummy");
        dataSource.add("dummy");
        return dataSource;
    }

    protected StyleBuilder createHeaderStyle() {
        return stl.style()
                .setBackgroundColor(HEADER_BACKGROUND)
                .setPadding(4);
    }

    protected StyleBuilder createBorderedStyle() {
        return stl.style()
                .setBorder(stl.pen1Point())
                .setPadding(DEFAULT_PADDING);
    }

    protected String formatCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()));
    }

    protected String generateDots(String prefix, String suffix) {
        int maxLineWidth = 100;
        int prefixLength = prefix.length();
        int suffixLength = suffix.length();
        int dotsLength = maxLineWidth - (prefixLength + suffixLength);
        if (dotsLength < 0) {
            dotsLength = 0;
        }
        return ".".repeat(dotsLength);
    }

    // Currency type
    public static class CurrencyType extends BigDecimalType {
        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "$ #,###.00";
        }
    }
}