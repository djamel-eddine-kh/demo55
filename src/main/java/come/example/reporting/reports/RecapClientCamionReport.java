package come.example.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;


import come.example.dto.CompanyInfoDto;
import come.example.dto.RecapClientDto;
import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class RecapClientCamionReport extends AbstractReport {
    private static final Logger LOGGER = Logger.getLogger(ClotureTourneeReport.class.getName());

    private final List<RecapClientDto> recapData;
    private final CompanyInfoDto companyInfo;
    protected  Color header_background = Color.decode("#BBDEFB");
    private Color tableTitleColor = Color.decode("#B3E5FC"); // Blue 200
    private String reportTitle = getResourceLabel("Recapclientducamion");

    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 9;

    // Column builders
  
    private TextColumnBuilder<Integer> rowNumberColumn;
    private TextColumnBuilder<String> nomColumn;
    private TextColumnBuilder<String> prenomColumn;
    private TextColumnBuilder<String> typePrixColumn;
    private TextColumnBuilder<String> typeActiviteColumn;
    private TextColumnBuilder<Double> soldeDebutJourneeColumn;
    private TextColumnBuilder<Double> totalVenteColumn;
    private TextColumnBuilder<Double> totalLivraisonColumn;
    private TextColumnBuilder<Double> totalRetourColumn;
    private TextColumnBuilder<Double> totalVersementColumn;
    private TextColumnBuilder<Double> totalVersementClotureColumn;
    private TextColumnBuilder<Double> soldeFinJourneeColumn;
    private TextColumnBuilder<Double> ecartSoldeColumn;

    // Subtotal builders
  
    private AggregationSubtotalBuilder<Double> soldeDebutJourneeSum;
    private AggregationSubtotalBuilder<Double> totalVenteSum;
    private AggregationSubtotalBuilder<Double> totalLivraisonSum;
    private AggregationSubtotalBuilder<Double> totalRetourSum;
    private AggregationSubtotalBuilder<Double> totalVersementSum;
    private AggregationSubtotalBuilder<Double> totalVersementClotureSum;
    private AggregationSubtotalBuilder<Double> soldeFinJourneeSum;
    private AggregationSubtotalBuilder<Double> ecartSoldeSum;
    
    // Total row count under "N°" column
    private AggregationSubtotalBuilder<Long> rowCountTotal;

    StyleBuilder columnTitleStyle;
    public RecapClientCamionReport(List<RecapClientDto> recapData, CompanyInfoDto companyInfo, List<Parameters> parameters) {
        this.companyInfo = companyInfo;
        if (recapData == null || recapData.isEmpty()) {
            throw new IllegalArgumentException("Recap data cannot be null or empty");
        }
        this.recapData = recapData;
        initializeConfigurations(parameters);
        initColumns();
        initSubtotals();
    }
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
                    case "headerColor" -> this.header_background = parseColor(value);
                    case "tableColumntable" -> this.header_background = parseColor(value);
                    default -> LOGGER.warning("Unknown configuration key: " + key);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error processing configuration: " + dto.getDescription(), e);
            }
        }
    }

    private void initColumns() {
        columnTitleStyle = stl.style()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .bold()
                .setBorder(stl.pen1Point())
                .setBackgroundColor(tableTitleColor)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setPadding(5)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        // Basic columns
    	 rowNumberColumn = Columns.reportRowNumberColumn("N°").setFixedColumns(6).setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        nomColumn = Columns.column("Nom", "nom", DataTypes.stringType()).setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        prenomColumn = Columns.column("Prenom", "prenom", DataTypes.stringType()).setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        typePrixColumn = Columns.column("Type Prix", "typePrix", DataTypes.stringType()).setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        typeActiviteColumn = Columns.column("Type d'activite client", "typeActivite", DataTypes.stringType()).setTitleStyle(columnTitleStyle).setStyle(columnStyle);

        // Numeric columns with common styling
        Function<String, TextColumnBuilder<Double>> numericColumn = this::createNumericColumn;
        
        soldeDebutJourneeColumn = numericColumn.apply("soldeDebutJournee").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        totalVenteColumn = numericColumn.apply("totalVente").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        totalLivraisonColumn = numericColumn.apply("totalLivraison").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        totalRetourColumn = numericColumn.apply("totalRetour").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        totalVersementColumn = numericColumn.apply("totalVersement").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        totalVersementClotureColumn = numericColumn.apply("totalVersementCloture").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        soldeFinJourneeColumn = numericColumn.apply("soldeFinJournee").setTitleStyle(columnTitleStyle).setStyle(columnStyle);
        ecartSoldeColumn = numericColumn.apply("ecartSolde").setTitleStyle(columnTitleStyle).setStyle(columnStyle);



    }

    private TextColumnBuilder<Double> createNumericColumn(String fieldName) {
        return Columns.column(getColumnTitle(fieldName), fieldName, DataTypes.doubleType());
    }

    private String getColumnTitle(String fieldName) {
        return fieldName.replaceAll("([A-Z])", " $1");
    }

    private void initSubtotals() {
        Function<TextColumnBuilder<Double>, AggregationSubtotalBuilder<Double>> sumSubtotal = 
            column -> sbt.sum(column);
        
        soldeDebutJourneeSum = sumSubtotal.apply(soldeDebutJourneeColumn);
        totalVenteSum = sumSubtotal.apply(totalVenteColumn);
        totalLivraisonSum = sumSubtotal.apply(totalLivraisonColumn);
        totalRetourSum = sumSubtotal.apply(totalRetourColumn);
        totalVersementSum = sumSubtotal.apply(totalVersementColumn);
        totalVersementClotureSum = sumSubtotal.apply(totalVersementClotureColumn);
        soldeFinJourneeSum = sumSubtotal.apply(soldeFinJourneeColumn);
        ecartSoldeSum = sumSubtotal.apply(ecartSoldeColumn);

        // Count total rows under "N°" column
        
        rowCountTotal = sbt.count(rowNumberColumn);
    }

    @Override
    public JasperReportBuilder build() {
        JasperReportBuilder report = report()
            .setTemplate(AbstractReport.reportTemplate)
                .setTextStyle(stl.style().setFontName(fontName))
            .title(createCompanyHeaderComponent(companyInfo),createTitleComponent())
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
            .pageHeader(createPageHeaderComponent())
            .columns(
                rowNumberColumn, 
                nomColumn,
                prenomColumn,
                typePrixColumn,
                typeActiviteColumn,
                soldeDebutJourneeColumn,
                totalVenteColumn,
                totalLivraisonColumn,
                totalRetourColumn,
                totalVersementColumn,
                totalVersementClotureColumn,
                soldeFinJourneeColumn,
                ecartSoldeColumn
            )
            .subtotalsAtSummary(
                rowCountTotal, // Total count under "N°" column
                soldeDebutJourneeSum,
                totalVenteSum,
                totalLivraisonSum,
                totalRetourSum,
                totalVersementSum,
                totalVersementClotureSum,
                soldeFinJourneeSum,
                ecartSoldeSum
            )
            .setDataSource(createDataSource());

        return report;
    }

    private DRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource(
            "nom", "prenom", "typePrix", "typeActivite",
            "soldeDebutJournee", "totalVente", "totalLivraison", "totalRetour",
            "totalVersement", "totalVersementCloture", "soldeFinJournee", "ecartSolde"
        );

        for (RecapClientDto dto : recapData) {
            dataSource.add(
                dto.getNom(),
                dto.getPrenom(),
                dto.getTypePrix(),
                dto.getTypeActiviteClient(),
                dto.getSoldeDebutJournee(),
                dto.getTotalVente(),
                dto.getTotalLivraison(),
                dto.getTotalRetour(),
                dto.getTotalVersement(),
                dto.getTotalVersement(),
                dto.getSoldeFinJournee(),
                dto.getEcartSolde()
            );
        }

        return dataSource;
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.verticalList(
            cmp.text(recapData.get(0).getNomClient())
               .setHorizontalAlignment(HorizontalAlignment.CENTER)
               .setStyle(stl.style().bold().setFontSize(titleFontSize).setTopPadding(8)),
            cmp.text(reportTitle +recapData.get(0).getCamionNumber())
               .setHorizontalAlignment(HorizontalAlignment.CENTER)
               .setStyle(stl.style().bold().setFontSize(titleFontSize).setTopPadding(15))
        ).add(cmp.verticalGap(20));
    }



    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return cmp.verticalList(
           
            cmp.horizontalList()
               .setStyle(createHeaderStyle(header_background))
               .add(preparationInfoComponent())
               .add(formattedDateComponent())
               .newRow().add(cmp.verticalGap(20))
        );
    }


    private ComponentBuilder<?, ?> preparationInfoComponent() {
        return cmp.text(getResourceLabel("Recapclientducamion") + recapData.get(0).getCamionNumber())
               .setStyle(stl.style().setFontSize(12));
    }

    private ComponentBuilder<?, ?> formattedDateComponent() {
        return cmp.text(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
               .setStyle(stl.style().setFontSize(12))
               .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
    }



    // Test method
  /*  public static void main(String[] args) {
        List<RecapClientDto> testData = List.of(
            new RecapClientDto(500, "EL Orabi El Hadi","Doe", "John", "Type1", "Activ1",
                1000.0, 500.0, 200.0, 50.0, 300.0, 100.0, 1450.0, 50.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Smith", "Jane", "Type2", "Activ2",
                1500.0, 600.0, 250.0, 75.0, 400.0, 150.0, 2125.0, 75.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0)
        );
        System.out.print(testData.get(0).getCamionNumber());
        List<reportValuesDto> reportValuesDto = Arrays.asList(
                new reportValuesDto(2, "titleFontSize", "14"),
                new reportValuesDto(4, "headerColor", "#B3E5FC"),
                new reportValuesDto(5, "totalValueSectionTitleColor", "#FFCDD2")
        );
        RecapClientCamionReport report = new RecapClientCamionReport(testData,reportValuesDto);
        try {
            JasperReportBuilder reportBuilder = report.build();
            reportBuilder.show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }*/

	@Override
	protected ComponentBuilder<?, ?> createDetailComponent() {
		return null;
	}
}