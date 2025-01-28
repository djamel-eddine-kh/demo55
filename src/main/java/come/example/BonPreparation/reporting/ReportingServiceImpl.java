package come.example.BonPreparation.reporting;


import come.example.BonPreparation.dto.BonPreparationDto;
import come.example.BonPreparation.reporting.template.AbstractReport;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReportingServiceImpl extends AbstractReport {

    private final List<BonPreparationDto> bonPreparationList;
    private final boolean isExcelFormat;

    public ReportingServiceImpl(Object bonPreparationList, boolean isExcelFormat) {
        this.bonPreparationList = (List<BonPreparationDto>) bonPreparationList;
        this.isExcelFormat = isExcelFormat;
    }

    @Override
    public JasperReportBuilder build() throws DRException {
        JasperReportBuilder reportBuilder = report()
                .setTemplate(AbstractReport.reportTemplate)
                .title(createTitleComponent())
                .pageHeader(createPageHeaderComponent())
                .detail(createDetailComponent())
                .setDataSource(createDummyDataSource());

        // Customize for Excel
        if (isExcelFormat) {

        }

        return reportBuilder;
    }
    @Override
    protected ComponentBuilder<?, ?> createTitleComponent() {
        return cmp.verticalList(
                cmp.text(getResourceLabel("depot") + bonPreparationList.get(0).getLibelleDepot())
                        .setStyle(stl.style().bold().setFontSize(18)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)),
                cmp.verticalGap(10));
    }

    @Override
    protected ComponentBuilder<?, ?> createPageHeaderComponent() {
        return cmp.verticalList(
                cmp.horizontalList()
                        .setStyle(createHeaderStyle())
                        .add(preparationInfoComponent())
                        .add(formattedDateComponent(formatCurrentTimestamp()))
                        .newRow().add(cmp.verticalGap(5)),
                marqueInfoComponent()
        );
    }

    @Override
    protected ComponentBuilder<?, ?> createDetailComponent() {
        VerticalListBuilder detailComponent = cmp.verticalList();
        groupProductsByFamily().forEach((family, products) -> {
            VerticalListBuilder productGroup = createProductGroup(family);

            groupProductsByPieceName(products).forEach((pieceName, pieceVariants) -> {
                productGroup.add(createProductVariant(pieceName, pieceVariants)).add(cmp.verticalGap(5));
            });

            detailComponent.add(productGroup);
        });
        return detailComponent;
    }

    // Grouping by famillePiece and then by nomPiece
    private Map<String, List<BonPreparationDto>> groupProductsByPieceName(List<BonPreparationDto> products) {
        return products.stream()
                .collect(Collectors.groupingBy(BonPreparationDto::getNomPiece));
    }


    private Map<String, List<BonPreparationDto>> groupProductsByFamily() {
        return bonPreparationList.stream()
                 // Filter out null values
                .collect(Collectors.groupingBy(BonPreparationDto::getFamillePiece));
    }

    private VerticalListBuilder createProductGroup(String family) {
        return cmp.verticalList()
                .setStyle(stl.style().setPadding(5))
                .add(cmp.text(family)
                        .setStyle(stl.style().bold().setPadding(5)));
    }

    private ComponentBuilder<?, ?> createProductVariant(String pieceName, List<BonPreparationDto> pieceVariants) {
        VerticalListBuilder categoryBox = cmp.verticalList()
                .setStyle(createBorderedStyle())
                .add(categoryHeaderComponent(pieceName)); // Single header for the piece name

        // Add each variant (e.g., client details, quantity)
        pieceVariants.forEach(product -> categoryBox.add(createOrderLine(product)));

        // Calculate the total quantity for the current piece group
        double totalQuantity = pieceVariants.stream()
                .mapToDouble(BonPreparationDto::getQuantite)
                .sum();

        // Get the piece code from the first variant (assuming all variants have the same codePiece)
        int pieceCode = pieceVariants.get(0).getCodePiece();

        // Add a total component at the end of the group
        categoryBox.add(
                cmp.text(String.format(getResourceLabel("TotalProduit") + " %d - %s  %.2f", pieceCode, pieceName, totalQuantity))
                        .setStyle(stl.style().bold().setFontSize(10).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setPadding(2)));

        return categoryBox;
    }



    private ComponentBuilder<?, ?> createOrderLine(BonPreparationDto product) {
        // Define the maximum line width (in characters)
        int maxLineWidth = 75; // Adjust based on your layout

        // Create the prefix and suffix
        String prefix = getResourceLabel("BL") + product.getCodeClient() + "    " + product.getNomClient() + "  ";
        String suffix = "     " + product.getQuantite();

        // Calculate the number of dots needed
        int dotsLength = maxLineWidth - (prefix.length() + suffix.length());

        // Ensure dotsLength is not negative
        if (dotsLength < 0) {
            dotsLength = 0;
        }

        // Generate the dots
        String dots = ".".repeat(dotsLength);

        // Combine the prefix, dots, and suffix
        String line = prefix + dots + " \u25A1" + suffix;

        return cmp.text(line)
                .setStyle(stl.style()
                        .setPadding(3)
                        .setFontSize(11));
    }

    private ComponentBuilder<?, ?> preparationInfoComponent() {
        return cmp.text(getResourceLabel("BondePreparation") + bonPreparationList.get(0).getNomChauffeur())
                .setStyle(stl.style().setFontSize(12));
    }

    private ComponentBuilder<?, ?> formattedDateComponent(String date) {
        return cmp.text(date)
                .setStyle(stl.style().setFontSize(12))
                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
    }

    private ComponentBuilder<?, ?> marqueInfoComponent() {
        return cmp.text(getResourceLabel("marque") + bonPreparationList.get(0).getLibelleDepot())
                .setStyle(stl.style().setFontSize(10)
                        .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT));
    }

    private ComponentBuilder<?, ?> categoryHeaderComponent(String category) {
        return cmp.text(category)
                .setStyle(stl.style().bold().setPadding(5));
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