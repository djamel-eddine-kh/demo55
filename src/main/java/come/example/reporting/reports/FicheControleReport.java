package come.example.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;

import come.example.dto.CompanyInfoDto;
import come.example.dto.FicheControleDto;
import come.example.dto.FicheControleParametreDto;
import come.example.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;

import java.awt.*;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class FicheControleReport extends AbstractReport {
    private final List<FicheControleParametreDto> ficheControleParametreDto;
    private final CompanyInfoDto companyInfo;
    private FicheControleDto ficheControleDto;
    private TextColumnBuilder<String> parametreColumn;
    private TextColumnBuilder<String> methodColumn;
    private TextColumnBuilder<String> uniteColumn;
    private TextColumnBuilder<String> plageColumn;
    private TextColumnBuilder<String> valeurColumn;
    private TextColumnBuilder<Integer> indexColumn;
    protected Color headerBackground = Color.decode("#B3E5FC");
    private Color greentext=Color.decode("#008000");
    private String fontName = "DejaVuSansMono";
    private int titleFontSize = 16;
    private int columnFontSize = 11;

    public FicheControleReport(FicheControleDto ficheControleDto,
                               List<FicheControleParametreDto> ficheParametreDtolist, CompanyInfoDto companyInfoDto,
                               List<Parameters> parameters) {
        this.companyInfo = companyInfoDto;
        this.ficheControleParametreDto = ficheParametreDtolist;
        this.ficheControleDto = ficheControleDto;
        initializeConfigurations(parameters);
        initColumns();
    }

    private void initializeConfigurations(List<Parameters> configs) {
        for (Parameters dto : configs) {
            try {
                String key = dto.getClef();
                String value = dto.getValeur();

                switch (key) {
                    case "tableColor"-> this.headerBackground = Color.decode(value);
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
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        indexColumn = Columns.reportRowNumberColumn(getResourceLabel("numero"))
                .setFixedColumns(4)
                .setTitleStyle(columnTitleStyle)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        parametreColumn = Columns.column(getResourceLabel("parametre_de_verification"),
                        "verificationParamters", DataTypes.stringType())
                .setTitleStyle(columnTitleStyle);

        methodColumn = Columns.column(getResourceLabel("method"),
                        "methode", DataTypes.stringType())
                .setTitleStyle(columnTitleStyle);

        uniteColumn = Columns.column(getResourceLabel("unite"),
                        "unite", DataTypes.stringType())
                .setTitleStyle(columnTitleStyle);

        plageColumn = Columns.column(getResourceLabel("plage_de_reference"),
                        "plageReference", DataTypes.stringType())
                .setTitleStyle(columnTitleStyle);

        valeurColumn = Columns.column(getResourceLabel("valeur_mesure"),
                        "valeurMesure", DataTypes.stringType())
                .setTitleStyle(columnTitleStyle);
    }

    @Override
    public JasperReportBuilder build() {
        return report()
                .setTemplate(AbstractReport.reportTemplate)
                .setTextStyle(stl.style().setFontName(fontName))
                .title(createCompanyHeaderComponent(companyInfo),createTitleComponent())
                .pageHeader(createPageHeaderComponent())
                .columns(indexColumn, parametreColumn, methodColumn, uniteColumn, plageColumn, valeurColumn)
                .summary(createDetailComponent())
                .setDataSource(createDataSource());
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.verticalList(
                cmp.text(getResourceLabel("Le") + " " + ficheControleDto.getDateDC())
                        .setStyle(stl.style()
                                .bold()
                                .setFontSize(columnFontSize+2)
                                .setForegroundColor(greentext)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                                .setTopPadding(6)),
                cmp.text(getResourceLabel("fiche_de_control"))
                        .setStyle(stl.style()
                                .bold()
                                .setFontSize(titleFontSize)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                .setTopPadding(15)),
                cmp.verticalGap(15));
    }
    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        StyleBuilder columnStyle = stl.style()
                .setBorder(stl.pen1Point().setLineColor(Color.red))
                .setBold(true)
                .setPadding(12)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
        StyleBuilder greenStyle = stl.style()
                .bold()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .setForegroundColor(Color.decode("#008000"))
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setPadding(0);
        StyleBuilder textStyle = stl.style()
                .bold()
                .setFontName(fontName)
                .setFontSize(columnFontSize)
                .setForegroundColor(Color.black)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        return  cmp.verticalList(
                cmp.verticalList(
                        cmp.horizontalList(
                                cmp.text(getResourceLabel("DateDF")).setStyle(textStyle).setFixedWidth(60),
                                cmp.text(ficheControleDto.getDateDF()).setStyle(greenStyle).setFixedWidth(80),
                                cmp.text(getResourceLabel("CodeArticle")).setStyle(textStyle).setFixedWidth(120),
                                cmp.text(ficheControleDto.getCodeArticle()).setStyle(greenStyle),
                                cmp.text(getResourceLabel("Désignation")).setStyle(textStyle).setFixedWidth(85),
                                cmp.text(ficheControleDto.getDesignation()).setStyle(greenStyle)
                        ).setStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)),
                        cmp.verticalGap(5),
                        cmp.horizontalList(
                                cmp.text(getResourceLabel("DateDC")).setStyle(textStyle).setFixedWidth(60),
                                cmp.text(ficheControleDto.getDateDF()).setStyle(greenStyle),
                                cmp.text(getResourceLabel("NuméroLot")).setStyle(textStyle).setFixedWidth(90),
                                cmp.text(ficheControleDto.getNumLot()).setStyle(greenStyle),
                                cmp.text(getResourceLabel("Quantité")).setStyle(textStyle).setFixedWidth(80),
                                cmp.text(ficheControleDto.getQuantity().toString()).setStyle(greenStyle)
                        ).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT))
                ).setStyle(columnStyle),    cmp.verticalGap(20))
                ;
    }
    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        StyleBuilder columnStyle1 = stl.style()
                .setBorder(stl.pen1Point())
                .setBold(true);

        StyleBuilder textStyle = stl.style()
                .bold()
                .setForegroundColor(Color.black);

        return cmp.horizontalList(
                cmp.horizontalGap(200),
                cmp.verticalList(
                        cmp.verticalGap(20),
                        cmp.text(getResourceLabel("ingenieur_controle_qualite"))
                                .setStyle(textStyle),
                        cmp.verticalGap(10),
                        cmp.text(" ")
                                .setStyle(columnStyle1)
                                .setFixedHeight(30)
                                .setFixedWidth(170)
                ),
                cmp.horizontalGap(30),
                cmp.verticalList(
                        cmp.verticalGap(20),
                        cmp.text(getResourceLabel("responsable"))
                                .setStyle(textStyle),
                        cmp.verticalGap(10),
                        cmp.text(" ")
                                .setStyle(columnStyle1)
                                .setFixedHeight(30)
                                .setFixedWidth(170)
                )
        ).setStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT));
    }



    private DRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource(
                "verificationParamters",
                "methode",
                "unite",
                "plageReference",
                "valeurMesure"
        );

        for (FicheControleParametreDto dto : ficheControleParametreDto) {
            dataSource.add(
                    dto.getVerificationParamters(),
                    dto.getMethode(),
                    dto.getUnite(),
                    dto.getPlageReference(),
                    dto.getValeurMesure()
            );
        }

        return dataSource;
    }

}