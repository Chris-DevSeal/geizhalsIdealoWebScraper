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

import static eu.devseal.geizhalsscraper.data.Configuration.COMPANY;
import static eu.devseal.geizhalsscraper.data.Configuration.NO_LISTING_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CSVWriterService {
    private final GeizhalsScraperService scrapeService;
    private final ProductFormatter productFormatter;
    private final GeizhalsProductService productService;

    public void writeScrapedDataToCsv(Writer writer) throws IOException {
        Map<Product, List<GeizhalsProduct>> data = scrapeService.scrape();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            Object[] headlines = new String[]{"Maschinentyp", "Maschinenpreis", "Lieferkosten", "Gesamt", "Anbieter", "Differenz Comat Preis"};
            csvPrinter.printRecord(headlines);
            for (Map.Entry<Product, List<GeizhalsProduct>> productListEntry : data.entrySet()) {
                writeLine(csvPrinter, productListEntry);
            }
        } catch (IOException e) {
            log.error("Error while writing CSV ", e);
        }
    }

    private void writeLine(CSVPrinter csvPrinter, Map.Entry<Product, List<GeizhalsProduct>> productListEntry) throws IOException {
        String name = productFormatter.format(productListEntry.getKey());
        List<GeizhalsProduct> productListings = productListEntry.getValue();
        GeizhalsProduct product = scrapeService.findFirstProductWhereCompanyIsNot(productListings, COMPANY);
        GeizhalsProduct comatProduct = scrapeService.findProductListingByCompanyName(productListings, COMPANY);
        double diffComatAndCompetitor = isOneProductNonExistent(product, comatProduct) ? getDiffComatAndCompetitor(product, comatProduct) : 999_999_999;
        double totalPrice = productService.getTotalPrice(product);
        csvPrinter.printRecord
                (
                        name,
                        product.getUnitPrice(),
                        productService.getSmallestShippingCost(product.getShippingCost()),
                        productService.formatDoubleToString(totalPrice), product.getCompany(),
                        productService.formatDoubleToString(diffComatAndCompetitor)
                );
    }

    private boolean isOneProductNonExistent(GeizhalsProduct product, GeizhalsProduct comatProduct) {
        return comatProduct.getOfferID() != NO_LISTING_ID && product.getOfferID() != NO_LISTING_ID;
    }

    private double getDiffComatAndCompetitor(GeizhalsProduct product, GeizhalsProduct comatProduct) {
        return productService.getTotalPrice(product) - productService.getTotalPrice(comatProduct);
    }
}
