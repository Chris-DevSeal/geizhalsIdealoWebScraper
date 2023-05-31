package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.IO.CSVWriterService;
import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductListing;
import eu.devseal.geizhalsscraper.exceptions.CustomFileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeizhalsWebService {
    private final GeizhalsScraperService geizhalsScraperService;
    private final IdealoScraperService idealoScraperService;
    private final CSVWriterService writerService;
    private final File file;

    public void refreshScrapeResults(FileWriter fileWriter) {
        try {
            Map<Product, List<ProductListing>> scrapedData = geizhalsScraperService.scrape();
            Map<Product, List<ProductListing>> secondProviderScrapedData = idealoScraperService.scrape();
            for (Map.Entry<Product, List<ProductListing>> productListEntry : scrapedData.entrySet()) {
                productListEntry.getValue().addAll(secondProviderScrapedData.get(productListEntry.getKey()));
            }
            writerService.writeScrapedDataToCsv(scrapedData, fileWriter);
        } catch (IOException e) {
            log.error("Error: " + e);
        }
    }

    public byte[] getCsvData(boolean reload) throws CustomFileNotFoundException {
        if (reload || !file.exists()) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                refreshScrapeResults(fileWriter);
            } catch (IOException e) {
                log.error("Controller Error: "+e);
            }
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Not able to read / create CSV file");
            throw new CustomFileNotFoundException();
        }

    }
}
