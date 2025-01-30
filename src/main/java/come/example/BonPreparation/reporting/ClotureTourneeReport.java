package come.example.BonPreparation.reporting;

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
import net.sf.dynamicreports.report.constant.LineSpacing;
import net.sf.dynamicreports.report.constant.Position;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import come.example.BonPreparation.dto.CloturetourneeDto;
import come.example.BonPreparation.dto.RecapClientDto;
import come.example.BonPreparation.dto.TotalClotureTourneeDto;
import come.example.BonPreparation.reporting.template.AbstractReport;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ClotureTourneeReport extends AbstractReport {
    private final List<CloturetourneeDto> lignes;
    private final TotalClotureTourneeDto totaux;

    // Colonnes
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

    public ClotureTourneeReport(List<CloturetourneeDto> lignes, TotalClotureTourneeDto totaux) {
        this.lignes = lignes;
        this.totaux = totaux;
        initColumns();
    }

    private void initColumns() {
      StyleBuilder numericStyle  = stl.style()
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPattern("#,##0.00");

        numeroColumn = Columns.reportRowNumberColumn("N°")
                .setFixedColumns(4);

        produitColumn = Columns.column("Produit", "produit", DataTypes.stringType())
                .setFixedWidth(100);

        stockDepartColumn = Columns.column("Stock Départ", "stockDepart", DataTypes.doubleType())
                .setStyle(numericStyle);

        chargementColumn = Columns.column("Chargement", "chargement", DataTypes.doubleType())
                .setStyle(numericStyle);

        venteColumn = Columns.column("Vente", "vente", DataTypes.doubleType())
                .setStyle(numericStyle);

        livraisonColumn = Columns.column("Livraison", "livraison", DataTypes.doubleType())
                .setStyle(numericStyle);

        retourColumn = Columns.column("Retour", "retour", DataTypes.doubleType())
                .setStyle(numericStyle);

        retourStockColumn = Columns.column("Retour Stock", "retourStock", DataTypes.doubleType())
                .setStyle(numericStyle);

        stockFinJourneeColumn = Columns.column("Stock Fin", "stockFinJournee", DataTypes.doubleType())
                .setStyle(numericStyle);

        ecartStockColumn = Columns.column("Écart Stock", "ecartStock", DataTypes.doubleType())
                .setStyle(numericStyle);
    }

    @Override
    public JasperReportBuilder build() {
        return report()
                .setTemplate(reportTemplate)
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
                .setDataSource(createDataSource());
    }

    private ComponentBuilder<?, ?> createTotalValueSection() {
        // Define styles
        StyleBuilder headerStyle = stl.style()
                .bold()
                .setFontSize(13)
                .setForegroundColor(Color.WHITE)
                .setBackgroundColor(Color.DARK_GRAY)
                .setPadding(5)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        StyleBuilder evenRowStyle = stl.style()
                .setBackgroundColor(Color.LIGHT_GRAY)
                .setPadding(3)
                .setBorder(stl.pen1Point());

        StyleBuilder oddRowStyle = stl.style()
                .setBackgroundColor(Color.WHITE)
                .setPadding(3)
                .setBorder(stl.pen1Point());

        // Define Labels (Left Column)
        ComponentBuilder<?, ?> labelsColumn = cmp.verticalList(
                createStyledText("Total stock départ:", 0),
                createStyledText("Total chargement:", 1),
                createStyledText("Total vente:", 0),
                createStyledText("Total livraison:", 1),
                createStyledText("Total retour:", 0),
                createStyledText("Total retour stock:", 1),
                createStyledText("Total stock fin journée:", 0),
                createStyledText("Total écart stock:", 1),
                createStyledText("Total versement client:", 0),
                createStyledText("Total versement crédit:", 1),
                createStyledText("Total crédit début:", 0),
                createStyledText("Total écart crédit:", 1),
                createStyledText("Total dépenses:", 0),
                createStyledText("Total versement:", 1),
                createStyledText("Total écart versement:", 0),
                createStyledText("Net à payer:", 1, Color.RED),
                createStyledText("Écart du jour:", 0),
                createStyledText("Valeur stock crédit:", 1)
        );

        // Define Values (Right Column)
        ComponentBuilder<?, ?> valuesColumn = cmp.verticalList(
                createStyledText(totaux.getTotalstockDepart().toString(), 0),
                createStyledText(totaux.getTotalchargement().toString(), 1),
                createStyledText(totaux.getTotalVente().toString(), 0),
                createStyledText(totaux.getTotalLivrasion().toString(), 1),
                createStyledText(totaux.getTotalRetour().toString(), 0),
                createStyledText(totaux.getTotalRetourStock().toString(), 1),
                createStyledText(totaux.getTotalstockFinJournee().toString(), 0),
                createStyledText(totaux.getTotalecartStock().toString(), 1),
                createStyledText(totaux.getTotalVersementClient().toString(), 0),
                createStyledText(totaux.getTotalVersementCredit().toString(), 1),
                createStyledText(totaux.getTotalCreditDebut().toString(), 0),
                createStyledText(totaux.getTotalEcartCredit().toString(), 1),
                createStyledText(totaux.getTotalDepenses().toString(), 0),
                createStyledText(totaux.getTotalVersement().toString(), 1),
                createStyledText(totaux.getTotalEcartVersement().toString(), 0),
                createStyledText(totaux.getNetApayer().toString(), 1, Color.BLUE),
                createStyledText(totaux.getEcartDuJour().toString(), 0),
                createStyledText(totaux.getValeurStockCredit().toString(), 1)
        );

        // Return the bordered table
        return cmp.verticalList(
                cmp.text("VALEUR TOTALE").setStyle(headerStyle),
                cmp.horizontalList(labelsColumn, valuesColumn)
        );
    }

    // Utility method to create alternating row styles
    private ComponentBuilder<?, ?> createStyledText(String text, int rowIndex) {
        return cmp.text(text)
                .setStyle((rowIndex % 2 == 0 ? stl.style().setBackgroundColor(Color.LIGHT_GRAY).setBorder(stl.pen1Point()) 
                                            : stl.style().setBackgroundColor(Color.WHITE).setBorder(stl.pen1Point()))
                		.setAlignment(HorizontalAlignment.CENTER,VerticalAlignment.MIDDLE)
                .setPadding(10));
    }

    // Overloaded method for colored text
    private ComponentBuilder<?, ?> createStyledText(String text, int rowIndex, Color color) {
        return cmp.text(text)
                .setStyle((rowIndex % 2 == 0 ? stl.style().setBackgroundColor(Color.LIGHT_GRAY) 
                                             : stl.style().setBackgroundColor(Color.WHITE))
                		.setAlignment(HorizontalAlignment.CENTER,VerticalAlignment.MIDDLE)
                		.setPadding(10)
                        .setForegroundColor(color)
                        .setBorder(stl.pen1Point()));
    }



    private ComponentBuilder<?, ?> createTotalTable() {
        return cmp.horizontalList(
                // Column headers (aligned with data columns)
                cmp.text("Total").setStyle(columnTitleStyle.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setBorder(stl.pen1Point())).setFixedWidth(120),
                cmp.text(totaux.getTotalstockDepart().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalchargement().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalVente().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalLivrasion().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalRetour().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalRetourStock().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalstockFinJournee().toString()).setStyle(columnTitleStyle),
                cmp.text(totaux.getTotalecartStock().toString()).setStyle(columnTitleStyle)
        ).setStyle(stl.style()
                .setBorder(stl.pen1Point()) // Add border
                .setPadding(5) // Add spacing
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER) // Align text center
        );
    }




    private DRDataSource createDataSource() {
        DRDataSource ds = new DRDataSource(
                "produit", "stockDepart", "chargement", 
                "vente", "livraison", "retour",
                "retourStock", "stockFinJournee", "ecartStock"
        );

        for (CloturetourneeDto dto : lignes) {
            ds.add(
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
        return ds;
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.text("Rapport de Clôture de Tournée")
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setStyle(stl.style().bold().setFontSize(13));
    }

    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return cmp.horizontalList()
                .add(cmp.text("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .newRow()
                .add(cmp.verticalGap(10));
    }

    // Méthode main pour test
    public static void main(String[] args) {
        List<CloturetourneeDto> lignes = List.of(
                new CloturetourneeDto("AARWA 703 Cocktail", 0.0, 500.0, 10.0, 0.0,0.0, 0.0, 1.0, 0.0),
                new CloturetourneeDto("RL Blue/Size:10 kg", 0.0,  500.0,1.0, 1.0, 0.0, 0.0, 0.0, 0.0)
        );

        TotalClotureTourneeDto totaux = new TotalClotureTourneeDto(
        	    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 
        	    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
        	);
        totaux.setTotalchargement(11.0);
        totaux.setTotalLivrasion(1.0);
        
        totaux.setTotalVersementClient(110255.10);
        totaux.setTotalVersementCredit(104965.99);
        totaux.setTotalEcartCredit(5090.0);
        totaux.setNetApayer(110255.10);

        ClotureTourneeReport report = new ClotureTourneeReport(lignes, totaux);
        try {
            report.build().show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() { return null; }
}