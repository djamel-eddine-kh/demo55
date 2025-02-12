package come.example.reporting.reports;

import com.protid.commerciale.business.model.bo.Parameters;

import come.example.dto.CompanyInfoDto;
import come.example.reporting.template.AbstractReport;
import come.example.dto.BonSortieDto;
import come.example.dto.DetailFinancierDto;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractComplexExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ProduitVenteReport extends AbstractReport {
    private static final Color primaryColor = Color.decode("#B3E5FC");
    private static final Color secondaryColor = Color.decode("#B2EBF2");
    private StyleBuilder totalHeaderStyle;
    private Set<String> dynamicCategories = new HashSet<>();


    private List<come.example.dto.BonSortieDto> dataList;
    private come.example.dto.DetailFinancierDto secondTableData;
    private final CompanyInfoDto companyInfo;

    public ProduitVenteReport(List<BonSortieDto> dataList, DetailFinancierDto secondTableData, CompanyInfoDto companyInfoDto, List<Parameters> parameters) {
        initializeConfigurations(parameters);
        this.companyInfo = companyInfoDto;
        this.dataList = dataList;
        this.secondTableData = secondTableData;
    }
    private void initializeConfigurations(List<Parameters> configs) {
        for (Parameters dto : configs) {
            try {
                String key = dto.getClef();
                String value = dto.getValeur();


                switch (key) {

                }
            } catch (Exception e) {
            }
        }
    }
    @Override
    public JasperReportBuilder build() {
        StyleBuilder columnStyle = stl.style()
                .setBorder(stl.pen1Point())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        StyleBuilder headerStyle = stl.style(columnStyle)
                .bold()
                .setBackgroundColor(primaryColor);

        // Group products dynamically
        CrosstabRowGroupBuilder<String> productGroup = ctab.rowGroup("product", String.class)
                .setHeaderStyle(headerStyle)
                .setHeaderWidth(180)
                .setTotalHeaderHeight(100)
                .setShowTotal(false);

        // Column groups for categories, subCategories, and measureTypes
        List<String> customOrder = Arrays.asList("Sortie", "Entree", "Vente", "Promotion");

        CrosstabColumnGroupBuilder<String> categoryGroup = ctab.columnGroup("category", String.class)
                .setHeaderStyle(headerStyle)
                .setShowTotal(false)
                .orderBy(new AbstractComplexExpression<Comparable<?>>() {
                    @Override
                    public Comparable<?> evaluate(List<?> values, ReportParameters reportParameters) {
                        // We expect the values list to contain the grouped values for this column.
                        // Typically, you can pick the first element as the representative value.
                        if (values == null || values.isEmpty()) {
                            // If no value exists, push it to the end
                            return Integer.MAX_VALUE;
                        }
                        String category = (String) values.get(0).toString();
                        int index = customOrder.indexOf(category);
                        // If not found in the custom list, assign a default high index so it comes last.
                        return index >= 0 ? index : customOrder.size();
                    }
                });



        CrosstabColumnGroupBuilder<String> subCategoryGroup = ctab.columnGroup("subCategory", String.class)
                .setHeaderStyle(headerStyle)
                .setShowTotal(false);

        CrosstabColumnGroupBuilder<String> measureTypeGroup = ctab.columnGroup("measureType", String.class)
                .setHeaderStyle(headerStyle)
                .setShowTotal(false);

        // Measure (actual values)
        CrosstabMeasureBuilder<Object> valueMeasure = ctab.measure("value", Double.class, Calculation.SUM)
                .setStyle(columnStyle);

        // Create the crosstab
        CrosstabBuilder crosstab = ctab.crosstab()
                .setCellWidth(50)
                .setCellHeight(18)
                .headerCell(cmp.text(getResourceLabel("produitArabe")).setStyle(headerStyle))
                .rowGroups(productGroup)
                .columnGroups(categoryGroup, subCategoryGroup, measureTypeGroup)
                .measures(valueMeasure);

        return report()
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .setTemplate(AbstractReport.reportTemplate)
                .title(createCompanyHeaderComponent(companyInfo),createTitleComponent())
                .setColumnDirection(RunDirection.RIGHT_TO_LEFT)
                .summary(cmp.horizontalList(crosstab)
                ,ecartComponent(),
                        secondTableTitleComponent(),
                        createSecondTableComponent())
                .setDataSource(createDataSource());
    }

    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.verticalList(
                cmp.horizontalList(
                        cmp.verticalList(
                                cmp.text(dataList.get(0).getNomClient())
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                        .setStyle(stl.style().bold().setFontSize(11).setPadding(5)),
                                cmp.verticalGap(25),
                                cmp.text(dataList.get(0).getDescriptionClient())
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                        .setStyle(stl.style().bold().setFontSize(11))
                        ).setStyle(stl.style().setPadding(20).setBackgroundColor(primaryColor)),
                        cmp.horizontalGap(10),
                        cmp.horizontalList(
                                cmp.verticalList(
                                        cmp.text(getResourceLabel("date") + formatCurrentTimestamp())
                                                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                                .setStyle(stl.style().bold().setFontSize(10)),
                                        cmp.verticalGap(5),
                                        cmp.text(getResourceLabel("Vendeur")+" ..................................")

                                                .setStyle(stl.style().bold().setFontSize(10)),
                                        cmp.verticalGap(10),
                                        cmp.text(getResourceLabel("Secteur")+"..................................")
                                                .setStyle(stl.style().bold().setFontSize(10))
                                ),
                                cmp.text("BON SORTIE")
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                                        .setStyle(stl.style().bold().setFontSize(11).setVerticalTextAlignment(VerticalTextAlignment.BOTTOM))
                        ).setStyle(stl.style().setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE)
                                .setBackgroundColor(primaryColor).setPadding(10))
                ),
                cmp.verticalGap(15)
        );
    }
    protected ComponentBuilder<?, ?> ecartComponent() {
        totalHeaderStyle = stl.style()

                .bold()

                .setForegroundColor(Color.BLACK)
                .setPadding(6)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        return  cmp.verticalList(
                cmp.verticalGap(5),
                cmp.horizontalList(
                cmp.verticalGap(3),
                cmp.text("Ecart")
                        .setStyle(totalHeaderStyle
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                .setBorder(stl.pen1Point()))
                        .setFixedWidth(170),
                cmp.text("888.000").setStyle(columnTitleStyle)

        ).setStyle(stl.style()
                .setBorder(stl.pen1Point())
                .setPadding(5)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
        ).setFixedWidth(700));
    }
    protected ComponentBuilder<?, ?> secondTableTitleComponent() {
        return cmp.verticalList(
                cmp.verticalGap(10),
                cmp.text("Recette").setStyle(stl.style().bold().setBackgroundColor(primaryColor).setPadding(8)).setFixedWidth(700).setFixedHeight(25)
             ,
                cmp.verticalGap(10)
        );
    }
    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return null;
    }

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        return null;
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("product", "category", "subCategory", "measureType", "value");

        for (BonSortieDto dto : dataList) {
            for (BonSortieDto.CategoryData categoryData : dto.getCategories()) {
                dataSource.add(
                        dto.getNomProduit(),
                        categoryData.getCategory(),
                        categoryData.getSubCategory(),
                        categoryData.getMeasureType(),
                        categoryData.getValue()
                );
            }
        }
        return dataSource;
    }
    protected ComponentBuilder<?, ?> createSecondTableComponent() {
        StyleBuilder headerStyle = stl.style()
                .bold()
                .setBackgroundColor(primaryColor)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        // Base cell style
        StyleBuilder columnStyle = stl.style()
                .setBorder(stl.pen1Point())
                .setBold(true)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
        // Separate style for cells that need a colored background
        StyleBuilder coloredCellStyle = stl.style(columnStyle)
                .setBackgroundColor(primaryColor);

        return cmp.verticalList(
                // Row 1: "Montant"
                cmp.horizontalList(
                        cmp.text(getResourceLabel("montant")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(25),
                        cmp.text(String.valueOf(secondTableData.getMontant())).setStyle(columnStyle).setFixedWidth(500).setFixedHeight(25)
                ),
                // Row 2: "Total des dépenses"
                cmp.horizontalList(
                        cmp.text(getResourceLabel("totalDepenses")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(25),
                        cmp.text(String.valueOf(secondTableData.getTotalDepenses())).setStyle(columnStyle).setFixedWidth(500)
                ),
                // Row 3: "Total en caisse"
                cmp.horizontalList(
                        cmp.text(getResourceLabel("totalEncaisse")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(25),
                        cmp.text(String.valueOf(secondTableData.getTotalEncaisse())).setStyle(columnStyle).setFixedWidth(500)
                ),
                // Row 4: "La caisse" – 5 pairs; left texts are static labels (keep currencies as is)
                cmp.horizontalList(
                        cmp.text(getResourceLabel("laCaisse")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(38),
                        cmp.verticalList(
                                cmp.text("2 000,00 €").setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getArgent1())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text("1 000,00 €").setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getArgent2())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text("500,00 €").setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getArgent3())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text("200,00 €").setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getArgent4())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text("100,00 €").setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getArgent5())).setStyle(columnStyle).setFixedWidth(100)
                        )
                ),
                // Row 5: "Les dépenses" – 6 pairs
                cmp.horizontalList(
                        cmp.text(getResourceLabel("depenses")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(38),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("essence")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense1())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("lavage")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense2())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("reparation")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense3())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("autre")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense4())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("avance01")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense5())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("avance02")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getDepense6())).setStyle(columnStyle).setFixedWidth(100)
                        )
                ),
                // Row 6: "Crédit externe" – 6 pairs
                cmp.horizontalList(
                        cmp.text(getResourceLabel("creditExterne")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(38),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne1())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne2())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne3())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne4())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne5())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditExterne6())).setStyle(columnStyle).setFixedWidth(100)
                        )
                ),
                // Row 7: "Crédit interne" – 6 pairs
                cmp.horizontalList(
                        cmp.text(getResourceLabel("creditInterne")).setStyle(columnTitleStyle).setFixedWidth(100).setFixedHeight(38),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne1())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne2())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne3())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne4())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("client")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne5())).setStyle(columnStyle).setFixedWidth(100)
                        ),
                        cmp.verticalList(
                                cmp.text(getResourceLabel("montantCredit")).setStyle(coloredCellStyle).setFixedWidth(100),
                                cmp.text(String.valueOf(secondTableData.getCreditInterne6())).setStyle(columnStyle).setFixedWidth(100)
                        )
                )
        );
    }

}