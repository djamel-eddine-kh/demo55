package come.example.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;


import come.example.dto.CompanyInfoDto;
import come.example.dto.Depotdto;
import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class DepotArticleReport extends AbstractReport {
    private final List<Depotdto> depotData;
    private final CompanyInfoDto companyInfo;
    protected Color headerBackground = Color.decode("#BBDEFB");
    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 11;

    private TextColumnBuilder<String> locationColumn;
    private TextColumnBuilder<String> nomArticleColumn;
    private TextColumnBuilder<String> numeroSerieColumn;
    private TextColumnBuilder<Double> articleQuantiteColumn;
    private TextColumnBuilder<Integer> indexColumn;
    private AggregationSubtotalBuilder<Double> articleQuantiteSum;

    public DepotArticleReport(List<Depotdto> depotData, CompanyInfoDto companyInfoDto, List<Parameters> parameters) {
        if (depotData == null || depotData.isEmpty()) {
            throw new IllegalArgumentException("Depot data cannot be null or empty");
        }
        this.companyInfo = companyInfoDto;
        this.depotData = depotData;
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
                    case "fontName" -> this.fontName = value;
                    case "titleFontSize" -> this.titleFontSize = Integer.parseInt(value);
                    case "columnFontSize" -> this.columnFontSize = Integer.parseInt(value);
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }
    private void initColumns() {
        StyleBuilder columnTitleStyle = stl.style()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .bold()
                .setBorder(stl.pen1Point())
                .setBackgroundColor(headerBackground)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setPadding(7)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Adding index column
        indexColumn = Columns.reportRowNumberColumn(getResourceLabel("numero"))
                .setFixedColumns(4)
                .setTitleStyle(columnTitleStyle)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        locationColumn=  Columns.column("Location", "locationArticle", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
        nomArticleColumn = Columns.column("Article", "nomArticle", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
        numeroSerieColumn = Columns.column("Numéro Série", "numeroSerie", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
        articleQuantiteColumn = Columns.column("Quantité", "articleQuantite", DataTypes.doubleType()).setTitleStyle(columnTitleStyle);
    }

    private void initSubtotals() {
        articleQuantiteSum = sbt.sum(articleQuantiteColumn);
    }

    @Override
    public JasperReportBuilder build() {
        return report()
                .setTemplate(AbstractReport.reportTemplate)
                .setTextStyle(stl.style().setFontName(fontName))
                .title(
                        createCompanyHeaderComponent(companyInfo),
                        createTitleComponent()
                )
                .columns(indexColumn,locationColumn,nomArticleColumn, numeroSerieColumn, articleQuantiteColumn)
                .subtotalsAtSummary(articleQuantiteSum)
                .setDataSource(createDataSource());
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
       return cmp.verticalList(
                cmp.text(getResourceLabel("depot") + " " +depotData.get(0).getNomDepot())
                        .setStyle(stl.style().bold().setFontSize(titleFontSize)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)),
                cmp.verticalGap(15));
    }

    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return null;
    }

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        return null;
    }

    public DRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("nomDepot", "locationArticle","nomArticle", "numeroSerie", "articleQuantite");

        for (Depotdto dto : depotData) {
            dataSource.add(dto.getNomDepot(),dto.getLocationArticle(), dto.getNomArticle(), dto.getNumeroSerie(), dto.getArticleQuantite());
        }

        return dataSource;
    }


}