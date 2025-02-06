package come.example.reports.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;
import come.example.reports.dto.LigneCloture;
import come.example.reports.dto.TotalClotureTourneeDto;
import come.example.reports.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Map.entry;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ClotureTourneeReport extends AbstractReport {
    private static final Logger LOGGER = Logger.getLogger(ClotureTourneeReport.class.getName());
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("keywords");

    // Input Data
    private final List<LigneCloture> lignes;
    private final TotalClotureTourneeDto totaux;

    // Configurable Properties (default values)
    private String reportTitle = "rapportClotureTournee";
    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 10;
    private Color firstTableColumnTitleColor = Color.decode("#B3E5FC"); // Blue 200
    private Color totalValueSectionTitleColor = Color.decode("#B3E5FC"); // Blue 200 90CAF9

    // Style Builders
    private StyleBuilder columnTitleStyle;
    private StyleBuilder numericStyle;
    private StyleBuilder totalHeaderStyle;

    // Column Definitions
    private TextColumnBuilder<Integer> numeroColumn;
    private TextColumnBuilder<String> produitColumn;
    private TextColumnBuilder<Double> stockDepartColumn;
    private TextColumnBuilder<Double> chargementColumn;
    private TextColumnBuilder<Double> venteColumn;
    private TextColumnBuilder<Double> livraisonColumn;
    private TextColumnBuilder<Double> retourColumn;
    private TextColumnBuilder<Double> retourStockColumn;
    private TextColumnBuilder<Double> stockFinJourneeColumn;
    private TextColumnBuilder<Double> ecartStockColumn;

    // Default row colors
    // Default row colors with distinct, non-gradient order
    private static final Map<String, Color> DEFAULT_ROW_COLORS = Map.ofEntries(
            entry("totalStockDepart", Color.decode("#B3E5FC")),        // Light blue
            entry("totalVente", Color.decode("#FFCDD2")),             // Pale pink
            entry("totalStockFinJournée", Color.decode("#FFE0B2")),   // Light purple
            entry("totalLivraison", Color.decode("#E0E0E0")),         // Soft lavender
            entry("totalDepenses", Color.decode("#DCEDC8")),          // Light lime
            entry("totalRetourStock", Color.decode("#B2EBF2")),       // Pale cyan
            entry("totalRetour", Color.decode("#FFF9C4")),            // Pale yellow
            entry("totalVersementCredit", Color.decode("#F8BBD0")),   // Soft pink
            entry("totalVersementClient", Color.decode("#BBDEFB")),   // Very light blue
            entry("totalEcartCredit", Color.decode("#FFCDD2")),       // Pale pink
            entry("totalCreditDebut", Color.decode("#B2DFDB")),       // Pale teal
            entry("netAPayer", Color.decode("#FFCCBC")),              // Soft coral
            entry("totalVersement", Color.decode("#E1BEE7")),         // Light orange
            entry("totalChargement", Color.decode("#C8E6C9")),        // Light green
            entry("totalEcartStock", Color.decode("#F5F5F5")),        // White smoke
            entry("totalEcartVersement", Color.decode("#D1C4E9")),    // Soft grey
            entry("valeurStockCredit", Color.decode("#E0F7FA")),       // Pale cyan
            entry("ecartDuJour", Color.decode("#C8E6C9"))            // Light green
    );


    public ClotureTourneeReport(List<LigneCloture> lignes, TotalClotureTourneeDto totaux, List<Parameters> configs) {
        this.lignes = lignes;
        this.totaux = totaux;
        initializeConfigurations(configs);
        initializeStyles();
        initializeColumns();
    }

    /**
     * Initializes configurations from the provided DTOs.
     */

    private void initializeConfigurations(List<Parameters> configs) {
        for (Parameters dto : configs) {
            try {
                String key = dto.getClef();
                String value = dto.getValeur();

                switch (key) {
                    case "reportTitle" -> this.reportTitle = value;
                    case "fontName" -> this.fontName = value;
                    case "titleFontSize" -> this.titleFontSize = Integer.parseInt(value);
                    case "columnFontSize" -> this.columnFontSize = Integer.parseInt(value);
                    case "tableColumntable" -> this.firstTableColumnTitleColor = parseColor(value);
                    case "totalValueSectionColor" -> this.totalValueSectionTitleColor = parseColor(value);
                    default -> LOGGER.warning("Unknown configuration key: " + key);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error processing configuration: " + dto.getDescription(), e);
            }
        }
    }

    /**
     * Initializes all style builders for the report.
     */
    private void initializeStyles() {
        columnTitleStyle = stl.style()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .bold()
                .setBorder(stl.pen1Point())
                .setBackgroundColor(firstTableColumnTitleColor)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setPadding(5)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Custom pattern for numeric values
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Use space as the grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        String pattern = decimalFormat.toPattern();

        numericStyle = stl.style()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(4)
                .setPattern(pattern);

        totalHeaderStyle = stl.style()
                .setFontName(fontName)
                .bold()
                .setFontSize(columnFontSize)
                .setForegroundColor(Color.BLACK)
                .setPadding(6)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    }

    /**
     * Initializes all column definitions used in the report.
     */
    private void initializeColumns() {
        numeroColumn = Columns.reportRowNumberColumn(getResourceLabel("numero"))
                .setFixedColumns(4)
                .setTitleStyle(columnTitleStyle)
                .setStyle(stl.style().setFontName(fontName)
                .setFontSize(columnFontSize)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setPadding(3)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        produitColumn = createStyledStringColumn("produit").setFixedWidth(150);
        stockDepartColumn = createStyledDoubleColumn("stockDepart");
        chargementColumn = createStyledDoubleColumn("chargement");
        venteColumn = createStyledDoubleColumn("vente");
        livraisonColumn = createStyledDoubleColumn("livraison");
        retourColumn = createStyledDoubleColumn("retour");
        retourStockColumn = createStyledDoubleColumn("retourStock");
        stockFinJourneeColumn = createStyledDoubleColumn("stockFinJournee");
        ecartStockColumn = createStyledDoubleColumn("ecartStock");
    }

    private TextColumnBuilder<Double> createStyledDoubleColumn(String columnKey) {
        return Columns.column(getResourceLabel(columnKey), columnKey, DataTypes.doubleType())
                .setTitleStyle(columnTitleStyle)
                .setStyle(numericStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    private TextColumnBuilder<String> createStyledStringColumn(String columnKey) {
        return Columns.column(getResourceLabel(columnKey), columnKey, DataTypes.stringType())
                .setTitleStyle(columnTitleStyle)
                .setStyle(numericStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
    }

    /**
     * Builds the report with the title, header, columns, summary, and data source.
     */
    @Override
    public JasperReportBuilder build() {
        return report()
                .setTemplate(reportTemplate)
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .setTextStyle(stl.style().setFontName(fontName))
                .title(createTitleComponent())
                .pageHeader(createPageHeaderComponent())
                .columns(
                        numeroColumn,
                        produitColumn,
                        stockDepartColumn,
                        chargementColumn,
                        venteColumn,
                        livraisonColumn,
                        retourColumn,
                        retourStockColumn,
                        stockFinJourneeColumn,
                        ecartStockColumn
                )
                .summary(
                        createTotalTable(),
                        createTotalValueSection()
                )
                .setIgnorePagination(true) // Allows table to span multiple pages
                .setDataSource(createDataSource());
    }

    /**
     * Creates the title component of the report.
     */
    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.text(getResourceLabel(reportTitle))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setStyle(stl.style().bold().setFontSize(titleFontSize));
    }

    /**
     * Creates the page header component showing the current date.
     */
    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return cmp.verticalList(
                cmp.horizontalList(cmp.text(getResourceLabel("date") + " " + currentDate)),
                cmp.verticalGap(10)
        );
    }

    /**
     * Creates the data source for the report from the list of DTOs.
     */
    private DRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource(
                "produit", "stockDepart", "chargement",
                "vente", "livraison", "retour",
                "retourStock", "stockFinJournee", "ecartStock"
        );
        for (LigneCloture dto : lignes) {
            dataSource.add(
                    dto.getProduit(),
                    dto.getStockDepart(),
                    dto.getChargement(),
                    dto.getVente(),
                    dto.getLivraison(),
                    dto.getRetour(),
                    dto.getRetourStock(),
                    dto.getStockFinJournee(),
                    dto.getEcartStock()
            );
        }
        return dataSource;
    }

    /**
     * Creates the horizontal total table shown in the summary section.
     */
    private ComponentBuilder<?, ?> createTotalTable() {
        return cmp.horizontalList(
                cmp.verticalGap(3),
                cmp.text(getResourceLabel("total"))
                        .setStyle(totalHeaderStyle
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                .setBorder(stl.pen1Point()))
                        .setFixedWidth(170),
                cmp.text(formatNumber(totaux.getTotalstockDepart())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalchargement())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalVente())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalLivrasion())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalRetour())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalRetourStock())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalstockFinJournee())).setStyle(columnTitleStyle),
                cmp.text(formatNumber(totaux.getTotalecartStock())).setStyle(columnTitleStyle)
        ).setStyle(stl.style()
                .setBorder(stl.pen1Point())
                .setPadding(5)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
        );
    }

    private ComponentBuilder<?, ?> createTotalValueSection() {
        List<ComponentBuilder<?, ?>> rows = new ArrayList<>();
        String[][] totals = {
                {"totalStockDepart", formatNumber(totaux.getTotalstockDepart())},
                {"totalChargement", formatNumber(totaux.getTotalchargement())},
                {"totalVente", formatNumber(totaux.getTotalVente())},
                {"totalLivraison", formatNumber(totaux.getTotalLivrasion())},
                {"totalRetour", formatNumber(totaux.getTotalRetour())},
                {"totalRetourStock", formatNumber(totaux.getTotalRetourStock())},
                {"totalStockFinJournée", formatNumber(totaux.getTotalstockFinJournee())},
                {"totalEcartStock", formatNumber(totaux.getTotalecartStock())},
                {"totalVersementClient", formatNumber(totaux.getTotalVersementClient())},
                {"totalVersementCredit", formatNumber(totaux.getTotalVersementCredit())},
                {"totalCreditDebut", formatNumber(totaux.getTotalCreditDebut())},
                {"totalEcartCredit", formatNumber(totaux.getTotalEcartCredit())},
                {"totalDepenses", formatNumber(totaux.getTotalDepenses())},
                {"totalVersement", formatNumber(totaux.getTotalVersement())},
                {"totalEcartVersement", formatNumber(totaux.getTotalEcartVersement())},
                {"netAPayer", formatNumber(totaux.getNetApayer())},
                {"ecartDuJour", formatNumber(totaux.getEcartDuJour())},
                {"valeurStockCredit", formatNumber(totaux.getValeurStockCredit())}
        };

        // Loop in increments of 3 (three totals per row)
        for (int i = 0; i < totals.length; i += 3) {
            List<ComponentBuilder<?, ?>> rowItems = new ArrayList<>();

            // For each group of three
            for (int j = i; j < i + 3; j++) {
                if (j < totals.length) {
                    // Retrieve background color for this key, falling back to CYAN if not defined.
                    Color bgColor = DEFAULT_ROW_COLORS.getOrDefault(totals[j][0], Color.CYAN);
                    // Add title cell (left-aligned) and value cell (right-aligned)
                    rowItems.add(createStyledCell(getResourceLabel(totals[j][0]), bgColor, HorizontalTextAlignment.LEFT)); // Left alignment for titles
                    rowItems.add(createStyledCell(totals[j][1], bgColor, HorizontalTextAlignment.RIGHT)); // Right alignment for results
                }
                // Add a horizontal gap between groups (but not after the last group)
                if (j < i + 2) {
                    rowItems.add(cmp.horizontalGap(12));
                }
            }
            // Combine the group items into a horizontal row.
            rows.add(cmp.horizontalList(rowItems.toArray(new ComponentBuilder[0])));
        }

        // Combine the header text with the rows in a vertical list.
        return cmp.verticalList(
                cmp.text(getResourceLabel("valeurTotale")).setStyle(totalHeaderStyle),
                cmp.verticalList(rows.toArray(new ComponentBuilder[0]))
        );
    }

    /**
     * Creates a styled cell with the given text, background color, and horizontal alignment.
     */
    private ComponentBuilder<?, ?> createStyledCell(String text, Color bgColor, HorizontalTextAlignment alignment) {
        return cmp.text(text)
                .setStyle(stl.style()
                        .setBackgroundColor(bgColor)
                        .setFontName(fontName)
                        .setFontSize(columnFontSize)
                        .setPadding(7)
                        .setBold(true)
                        .setBorder(stl.pen1Point())
                        .setHorizontalTextAlignment(alignment) // Set alignment dynamically
                        .setVerticalAlignment(VerticalAlignment.MIDDLE))
                .setFixedWidth(133);
    }


    private String formatNumber(Double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Use space as the grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(number);
    }



    /**
     * Retrieves a label from the resource bundle for the given key.
     */
    public String getResourceLabel(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key.trim());
        } catch (MissingResourceException e) {
            LOGGER.log(Level.WARNING, "Missing resource for key: {0}", key);
            return key;
        }
    }

    /**
     * Parses a color string into a Color object.
     */

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        return null; // Not used
    }

}