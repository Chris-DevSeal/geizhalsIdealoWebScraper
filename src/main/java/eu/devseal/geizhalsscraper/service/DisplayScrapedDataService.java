package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static eu.devseal.geizhalsscraper.data.StaticConstants.*;
import static eu.devseal.geizhalsscraper.data.StaticConstants.NO_LISTING_ID;

@Service
@RequiredArgsConstructor
public class DisplayScrapedDataService {
    private final GeizhalsScraperService scraperService;
    private final GeizhalsProductService productService;
    private final ProductFormatter productFormatter;

    public void displayData(Map<Product, List<GeizhalsProduct>> data) {
        for (Map.Entry<Product, List<GeizhalsProduct>> productEntry : data.entrySet()) {
            GeizhalsProduct comatListing = scraperService.findProductListingByCompanyName(productEntry.getValue(), COMPANY);
            GeizhalsProduct afterComatListing = scraperService.findFirstProductWhereCompanyIsNot(productEntry.getValue(), COMPANY);
            printToConsole(productEntry, comatListing, afterComatListing);
        }
    }

    private void printToConsole(Map.Entry<Product, List<GeizhalsProduct>> entry, GeizhalsProduct comatListing, GeizhalsProduct afterComatListing) {
        System.out.printf("%s:%n", productFormatter.format(entry.getKey()));
        displayComatListing(comatListing);
        displayCompetitorListing(afterComatListing);
    }

    private void displayCompetitorListing(GeizhalsProduct afterComatListing) {
        if (afterComatListing.getOfferID() == NO_LISTING_ID) {
            System.out.println("     Best not Comat Listing: No other listing");
        } else {
            System.out.printf("     Best not Comat Listing: %s - Total Price: %,.2f %n", afterComatListing, productService.getTotalPrice(afterComatListing));
        }
    }

    private void displayComatListing(GeizhalsProduct comatListing) {
        if (comatListing.getOfferID() == NO_LISTING_ID) {
            System.out.println("     Comat Listing: No Comat listing");
        } else {
            System.out.printf("     Comat Listing: %s - Total Price: %,.2f %n", comatListing, productService.getTotalPrice(comatListing));
        }
    }


}
