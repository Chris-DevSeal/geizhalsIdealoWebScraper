package eu.devseal.geizhalsscraper.runner;

import eu.devseal.geizhalsscraper.IO.CSVWriterService;
import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.service.DisplayScrapedDataService;
import eu.devseal.geizhalsscraper.service.GeizhalsScraperService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class ScraperRunner {
    @Bean
    ApplicationRunner runScraper(GeizhalsScraperService scraperService, DisplayScrapedDataService display) {
        return args -> {
            Map<Product, List<GeizhalsProduct>> products = scraperService.scrape();
            display.displayData(products);
        };
    }

    @Bean
    @Order(1)
    ApplicationRunner writeToCSV(GeizhalsScraperService scraperService, CSVWriterService writerService, FileWriter fileWriter) {
        return args -> {
            Map<Product, List<GeizhalsProduct>> data = scraperService.scrape();
            writerService.writeScrapedDataToCsv(data, fileWriter);
        };
    }
    @Bean
    FileWriter getFileWriter() throws IOException {
        File file = new File("src/main/resources/output/tmp.csv");
        return new FileWriter(file);
    }

}
