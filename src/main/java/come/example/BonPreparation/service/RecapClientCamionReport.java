package come.example.BonPreparation.service;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import come.example.BonPreparation.dto.RecapClientDto;
import come.example.BonPreparation.reporting.template.AbstractReport;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class RecapClientCamionReport extends AbstractReport {
    private final List<RecapClientDto> recapData;


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
    public RecapClientCamionReport(List<RecapClientDto> recapData) {
        if (recapData == null || recapData.isEmpty()) {
            throw new IllegalArgumentException("Recap data cannot be null or empty");
        }
        this.recapData = recapData;
        initColumns();
        initSubtotals();
    }

    private void initColumns() {
    	columnTitleStyle = stl.style()
                .setFontSize(6)  // Set smaller font size
                .bold(); 
        // Basic columns
    	 rowNumberColumn = Columns.reportRowNumberColumn("N°").setFixedColumns(6);
        nomColumn = Columns.column("Nom", "nom", DataTypes.stringType());
        prenomColumn = Columns.column("Prenom", "prenom", DataTypes.stringType());
        typePrixColumn = Columns.column("Type Prix", "typePrix", DataTypes.stringType());
        typeActiviteColumn = Columns.column("Type d'activite client", "typeActivite", DataTypes.stringType());

        // Numeric columns with common styling
        Function<String, TextColumnBuilder<Double>> numericColumn = this::createNumericColumn;
        
        soldeDebutJourneeColumn = numericColumn.apply("soldeDebutJournee");
        totalVenteColumn = numericColumn.apply("totalVente");
        totalLivraisonColumn = numericColumn.apply("totalLivraison");
        totalRetourColumn = numericColumn.apply("totalRetour");
        totalVersementColumn = numericColumn.apply("totalVersement");
        totalVersementClotureColumn = numericColumn.apply("totalVersementCloture");
        soldeFinJourneeColumn = numericColumn.apply("soldeFinJournee");
        ecartSoldeColumn = numericColumn.apply("ecartSolde");
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
            .title(createTitleComponent())
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
               .setStyle(stl.style().bold().setFontSize(16).setTopPadding(20)),
            cmp.text(getResourceLabel("Recapclientducamion") +recapData.get(0).getCamionNumber())
               .setHorizontalAlignment(HorizontalAlignment.CENTER)
               .setStyle(stl.style().bold().setFontSize(14).setTopPadding(20))
        ).add(cmp.verticalGap(20));
    }



    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return cmp.verticalList(
           
            cmp.horizontalList()
               .setStyle(createHeaderStyle())
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
    public static void main(String[] args) {
        List<RecapClientDto> testData = List.of(
            new RecapClientDto(500, "EL Orabi El Hadi","Doe", "John", "Type1", "Activ1",
                1000.0, 500.0, 200.0, 50.0, 300.0, 100.0, 1450.0, 50.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Smith", "Jane", "Type2", "Activ2",
                1500.0, 600.0, 250.0, 75.0, 400.0, 150.0, 2125.0, 75.0),
            new RecapClientDto(500,"EL Orabi El Hadi", "Brown", "Chris", "Type3", "Activ3",
                2000.0, 700.0, 300.0, 100.0, 500.0, 200.0, 2800.0, 100.0)
        );
        System.out.print(testData.get(0).getCamionNumber());
        RecapClientCamionReport report = new RecapClientCamionReport(testData);
        try {
            JasperReportBuilder reportBuilder = report.build();
            reportBuilder.show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected ComponentBuilder<?, ?> createDetailComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}