package come.example.reporting.template;


import come.example.dto.CompanyInfoDto;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public abstract class AbstractReport {
    // Constants for consistent styling
    private static final Logger LOGGER = Logger.getLogger(AbstractReport.class.getName());

    protected static final Color HEADER_BACKGROUND = Color.decode("#BBDEFB");
    protected static final Color Product_Card_BACKGROUND = Color.decode("#BBDEFB");

    protected static final int DEFAULT_PADDING = 10;

    // Styles
    protected static final StyleBuilder rootStyle = stl.style().setPadding(2);
    protected static final StyleBuilder boldStyle = stl.style(rootStyle).bold();
    protected static final StyleBuilder italicStyle = stl.style(rootStyle).italic();
    public static final StyleBuilder boldCenteredStyle = stl.style(boldStyle)
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    protected static final StyleBuilder bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
    protected static final StyleBuilder bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
    protected static final StyleBuilder bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
    protected static final StyleBuilder columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setFontSize(9).setPadding(8);
    public static final StyleBuilder columnTitleStyle = stl.style(columnStyle)
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(HEADER_BACKGROUND)
            .bold();
    public static final StyleBuilder groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    public static final StyleBuilder subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point()).setBackgroundColor(HEADER_BACKGROUND);

    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 11;
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
                .setCrosstabGroupTotalStyle(stl.style(columnTitleStyle).setBackgroundColor( Color.decode("#B2EBF2")))
                .setCrosstabGrandTotalStyle(stl.style(columnTitleStyle).setBackgroundColor(Color.decode("#B2DFDB")))
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
    protected ComponentBuilder<?, ?> createTitleComponent(String title,String resourcelbael) {
        return cmp.verticalList(
            cmp.text(title)
               .setHorizontalAlignment(HorizontalAlignment.CENTER)
               .setStyle(stl.style().bold().setFontSize(16)),
            cmp.text(getResourceLabel("Recapclientducamion") + title)
               .setHorizontalAlignment(HorizontalAlignment.CENTER)
               .setStyle(stl.style().bold().setFontSize(14))
    
        );
    }

    protected DRDataSource createDummyDataSource() {
        DRDataSource dataSource = new DRDataSource("dummy");
        dataSource.add("dummy");
        return dataSource;
    }

    protected StyleBuilder createHeaderStyle() {
        return stl.style()
                .setBackgroundColor(HEADER_BACKGROUND)
                .setPadding(10).setRadius(15);
    }
    protected StyleBuilder createHeaderStyle(Color color) {
        return stl.style()
                .setBackgroundColor(color)
                .setPadding(10).setRadius(15);
    }
    protected StyleBuilder createBorderedStyle(Color color) {
        return stl.style()
                .setBorder(stl.pen1Point())
                .setPadding(DEFAULT_PADDING)
                .setBackgroundColor(color);
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
    /**
     * Parses a color string into a Color object.
     */
    protected ComponentBuilder<?, ?> createCompanyHeaderComponent(CompanyInfoDto companyInfo) {
        if (companyInfo == null) {
            return cmp.text("").setFixedHeight(0); // Return empty component if no data
        }

        StyleBuilder labelStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        StyleBuilder valueStyle = stl.style().bold().setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

        return cmp.horizontalList(
                companyInfo.getLogoPath() != null ?
                        cmp.image(companyInfo.getLogoPath())
                                .setFixedDimension(120, 50) :
                        cmp.text("").setStyle(labelStyle),
                // Company Details
                cmp.verticalList(

                        cmp.text(getResourceLabel("NIC") + companyInfo.getNic()).setStyle(valueStyle),
                        cmp.text(getResourceLabel("NIF") + companyInfo.getNif()).setStyle(valueStyle),
                        cmp.text(getResourceLabel("RC") + companyInfo.getRc()).setStyle(valueStyle),
                        cmp.text(getResourceLabel("Address") + companyInfo.getAddress()).setStyle(valueStyle)
                ).setStyle(stl.style().setPadding(6))
        )
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(6));
    }


    protected Color parseColor(String colorString) {
        try {
            return colorString.startsWith("#")
                    ? Color.decode(colorString)
                    : (Color) Color.class.getField(colorString.toUpperCase()).get(null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Invalid color: {0}", colorString);
            return Color.CYAN;
        }
    }
    public String getResourceLabel(String key) {
        if (key == null || key.trim().isEmpty()) {
            return ""; // Return empty if key is null or blank
        }
        try {
           // FacesContext context = FacesContext.getCurrentInstance();

            ResourceBundle bundle = ResourceBundle.getBundle("keywords");
            return bundle.getString(key.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Return the key if an exception

        }

    }

}