package eu.devseal.geizhalsscraper.IO;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.service.GeizhalsProductService;
import eu.devseal.geizhalsscraper.service.GeizhalsScraperService;
import eu.devseal.geizhalsscraper.service.ProductFormatter;
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
    private final GeizhalsScraperService scrapeService;
    private final ProductFormatter productFormatter;
    private final GeizhalsProductService productService;

    public void writeScrapedDataToCsv(Map<Product, List<GeizhalsProduct>> data, Writer writer) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            Object[] headlines = new String[]{"Maschinentyp", "Comat-Preis", "Comat-LK", "Comat-Gesamt", "Konkurrenz-Gesamt", "Preisdifferenz","Empfohlener Preis", "Kunkurrenz-Anbieter"};
            csvPrinter.printRecord(headlines);
            for (Map.Entry<Product, List<GeizhalsProduct>> productListEntry : data.entrySet()) {
                writeLine(csvPrinter, productListEntry);
            }
        } catch (IOException e) {
            log.error("Error while writing CSV ", e);
        }
    }

    private void writeLine(CSVPrinter csvPrinter, Map.Entry<Product, List<GeizhalsProduct>> productListEntry) throws IOException {
        String productName = productFormatter.format(productListEntry.getKey());
        List<GeizhalsProduct> productListings = productListEntry.getValue();
        GeizhalsProduct enemyProduct = scrapeService.findFirstProductWhereCompanyIsNot(productListings, COMPANY);
        GeizhalsProduct comatProduct = scrapeService.findProductListingByCompanyName(productListings, COMPANY);
        double diffComatAndCompetitor = areBothProductsExistent(enemyProduct, comatProduct) ? getDiffComatAndCompetitor(enemyProduct, comatProduct) : NO_LISTING_VALUE;
        double enemyTotalPrice = productService.getTotalPrice(enemyProduct);
        double comatPrice = comatProduct.getUnitPrice();
        double comatTotalPrice = productService.getTotalPrice(comatProduct);
        double comatShippingCost = productService.getSmallestShippingCost(comatProduct.getShippingCost());
        double recommendedPrice = productService.findOptimalPrice(comatProduct, enemyProduct);
        if (diffComatAndCompetitor == NO_LISTING_VALUE) {
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

    private boolean areBothProductsExistent(GeizhalsProduct product, GeizhalsProduct comatProduct) {
        return comatProduct.getOfferID() != NO_LISTING_ID && product.getOfferID() != NO_LISTING_ID;
    }

    private double getDiffComatAndCompetitor(GeizhalsProduct product, GeizhalsProduct comatProduct) {
        return productService.getTotalPrice(product) - productService.getTotalPrice(comatProduct);
    }
}
