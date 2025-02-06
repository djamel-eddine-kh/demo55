package come.example.reports.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;
import come.example.reports.dto.ArticleDto;
import come.example.reports.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ArticleReport extends AbstractReport {
    private final List<ArticleDto> articleData;

    protected Color headerBackground = Color.decode("#BBDEFB");
    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 11;

    private TextColumnBuilder<Integer> indexColumn;
    private TextColumnBuilder<String> etatStockColumn;
    private TextColumnBuilder<String> etatSortieColumn;
    private TextColumnBuilder<String> etatEntreeColumn;

    public ArticleReport(List<ArticleDto> articleData,List<Parameters> parameters) {
        if (articleData == null || articleData.isEmpty()) {
            throw new IllegalArgumentException("Article data cannot be null or empty");
        }
        initializeConfigurations(parameters);
        this.articleData = articleData;
        initColumns();
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

        // Adding row number column
        indexColumn = Columns.reportRowNumberColumn(getResourceLabel("numero"))
                .setFixedColumns(4)
                .setTitleStyle(columnTitleStyle)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        etatStockColumn = Columns.column("État Stock", "etatStock", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
        etatSortieColumn = Columns.column("État Sortie", "etatSortie", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
        etatEntreeColumn = Columns.column("État Entrée", "etatEntree", DataTypes.stringType()).setTitleStyle(columnTitleStyle);
    }

    @Override
    public JasperReportBuilder build() {
        return report()
                .setTemplate(AbstractReport.reportTemplate)
                .setTextStyle(stl.style().setFontName(fontName))
                .title(createTitleComponent(),createPageHeaderComponent())
                .columns(indexColumn, etatStockColumn, etatSortieColumn, etatEntreeColumn)
                .setDataSource(createDataSource());
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.verticalList(
                cmp.text(getResourceLabel("depot") + " " + articleData.get(0).getNomDepot() )
                        .setStyle(stl.style().bold().setFontSize(titleFontSize)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER).setTopPadding(20)),
                cmp.verticalGap(15)
        );
    }

    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return cmp.horizontalList()
                        .setStyle(createHeaderStyle(headerBackground))
                        .add(cmp.text(getResourceLabel("article")+ " " + articleData.get(0).getNomArticle()).setStyle(stl.style().bold()))
                        .newRow().add(cmp.verticalGap(13));
    }

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        return null;
    }

    private DRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("etatStock", "etatSortie", "etatEntree");

        for (ArticleDto dto : articleData) {
            dataSource.add(dto.getEtatStock(), dto.getEtatSortie(), dto.getEtatEntree());
        }

        return dataSource;
    }


}

