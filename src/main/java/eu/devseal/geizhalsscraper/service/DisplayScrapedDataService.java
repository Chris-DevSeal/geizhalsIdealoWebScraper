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

    public void printToConsole(Map<Product, List<GeizhalsProduct>> data) {
        for (Map.Entry<Product, List<GeizhalsProduct>> entry : data.entrySet()) {
            List<GeizhalsProduct> bestPerformingCompanies = scraperService.findBestPerformingCompanies(entry.getValue());
            System.out.printf("%s:%n", formatProduct(entry.getKey()));
            System.out.printf("     Best Performing company: %s - Total Price: %,.2f %n", bestPerformingCompanies.get(0).getCompany(), productService.getTotalPrice(bestPerformingCompanies.get(0)));
            System.out.printf("     Second best Performing company: %s - Total Price: %,.2f %n", bestPerformingCompanies.get(1).getCompany(), productService.getTotalPrice(bestPerformingCompanies.get(1)));
        }
    }

    private String formatProduct(Product product) {
        String[] prodArr = product.name().toLowerCase().split("_");
        for (int i = 0; i < prodArr.length; i++) {
            prodArr[i] = prodArr[i].substring(0, 1).toUpperCase() + prodArr[i].substring(1);
        }
        return String.format("Saeco %s",String.join(" ", prodArr));
    }
}
