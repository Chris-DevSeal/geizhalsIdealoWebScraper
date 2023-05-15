package eu.devseal.geizhalsscraper.runner;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.service.DisplayScrapedDataService;
import eu.devseal.geizhalsscraper.service.GeizhalsScraperService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class ScraperRunner {
    @Bean
    ApplicationRunner runScraper(GeizhalsScraperService scraperService, DisplayScrapedDataService display) {
        return args -> {
            Map<Product, List<GeizhalsProduct>> products = scraperService.scrape();
            display.printToConsole(products);
        };
    }

}
