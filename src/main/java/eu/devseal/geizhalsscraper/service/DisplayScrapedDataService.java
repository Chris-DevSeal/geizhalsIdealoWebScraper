package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DisplayScrapedDataService {
    private final GeizhalsScraperService scraperService;
    private final GeizhalsProductService productService;

    public void displayData(Map<Product, List<GeizhalsProduct>> data) {
        final String companyName = "Comat";
        for (Map.Entry<Product, List<GeizhalsProduct>> productEntry : data.entrySet()) {
            GeizhalsProduct comatListing = scraperService.findProductListingByCompanyName(productEntry.getValue(), companyName)
                    .orElse(productNotListed());
            GeizhalsProduct afterComatListing = scraperService.findFirstProductWhereCompanyIsNot(productEntry.getValue(), companyName)
                    .orElse(productNotListed());
            printToConsole(productEntry, comatListing, afterComatListing);
        }
    }

    private void printToConsole(Map.Entry<Product, List<GeizhalsProduct>> entry, GeizhalsProduct comatListing, GeizhalsProduct afterComatListing) {
        System.out.printf("%s:%n", formatProduct(entry.getKey()));
        displayComatListing(comatListing);
        displayCompetitorListing(afterComatListing);
    }

    private void displayCompetitorListing(GeizhalsProduct afterComatListing) {
        if (afterComatListing.getOfferID() == 999) {
            System.out.println("     Best not Comat Listing: No other listing");
        } else {
            System.out.printf("     Best not Comat Listing: %s - Total Price: %,.2f %n", afterComatListing, productService.getTotalPrice(afterComatListing));
        }
    }

    private void displayComatListing(GeizhalsProduct comatListing) {
        if (comatListing.getOfferID() == 999) {
            System.out.println("     Comat Listing: No Comat listing");
        } else {
            System.out.printf("     Comat Listing: %s - Total Price: %,.2f %n", comatListing, productService.getTotalPrice(comatListing));
        }
    }

    private GeizhalsProduct productNotListed() {
        return GeizhalsProduct.builder()
                .offerID(999)
                .build();
    }

    private String formatProduct(Product product) {
        String[] prodArr = product.name().toLowerCase().split("_");
        for (int i = 0; i < prodArr.length; i++) {
            prodArr[i] = prodArr[i].substring(0, 1).toUpperCase() + prodArr[i].substring(1);
        }
        return String.format("Saeco %s", String.join(" ", prodArr));
    }
}
