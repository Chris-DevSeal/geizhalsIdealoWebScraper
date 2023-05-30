package eu.devseal.geizhalsscraper.IO;

import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductListing;
import eu.devseal.geizhalsscraper.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static eu.devseal.geizhalsscraper.data.StaticConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CSVWriterService {
    private final ProductService productService;

    public void writeScrapedDataToCsv(Map<Product, List<ProductListing>> data, Writer writer) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            Object[] headlines = new String[]{"Maschinentyp", "Comat-Preis", "Comat-LK", "Comat-Gesamt", "Konkurrenz-Gesamt", "Preisdifferenz","Empfohlener Preis", "Konkurrenz-Anbieter"};
            csvPrinter.printRecord(headlines);
            for (Map.Entry<Product, List<ProductListing>> productListEntry : data.entrySet()) {
                writeLine(csvPrinter, productListEntry);
            }
        } catch (IOException e) {
            log.error("Error while writing CSV ", e);
        }
    }

    private void writeLine(CSVPrinter csvPrinter, Map.Entry<Product, List<ProductListing>> productListEntry) throws IOException {
        String productName = productListEntry.getKey().getName();
        List<ProductListing> productListings = productListEntry.getValue();
        ProductListing enemyProduct = productService.findFirstProductWhereCompanyIsNot(productListings, COMPANIES);
        ProductListing comatProduct = productService.findProductListingByCompanyName(productListings, COMPANIES);
        double diffComatAndCompetitor = areBothProductsExistent(enemyProduct, comatProduct) ? getDiffComatAndCompetitor(enemyProduct, comatProduct) : NO_LISTING_VALUE;
        double enemyTotalPrice = productService.getTotalPrice(enemyProduct);
        double comatPrice = comatProduct.getUnitPrice();
        double comatTotalPrice = productService.getTotalPrice(comatProduct);
        double comatShippingCost = productService.getSmallestShippingCost(comatProduct.getShippingCost());
        double recommendedPrice = productService.findOptimalPrice(comatProduct, enemyProduct);
        if (!isAListing(diffComatAndCompetitor)) {
            csvPrinter.printRecord(
                    productName
            );
            return;
        }
        csvPrinter.printRecord
                (
                        productName,
                        productService.formatDoubleToString(comatPrice),
                        productService.formatDoubleToString(comatShippingCost),
                        productService.formatDoubleToString(comatTotalPrice),
                        productService.formatDoubleToString(enemyTotalPrice),
                        productService.formatDoubleToString(diffComatAndCompetitor),
                        productService.formatDoubleToString(recommendedPrice),
                        enemyProduct.getCompany()
                );
    }

    private boolean isAListing(double diffComatAndCompetitor) {
        return diffComatAndCompetitor != NO_LISTING_VALUE;
    }

    private boolean areBothProductsExistent(ProductListing product, ProductListing comatProduct) {
        return comatProduct.getOfferID() != NO_LISTING_ID && product.getOfferID() != NO_LISTING_ID;
    }

    private double getDiffComatAndCompetitor(ProductListing product, ProductListing comatProduct) {
        return productService.getTotalPrice(product) - productService.getTotalPrice(comatProduct);
    }
}
