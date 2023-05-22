package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.IO.CSVWriterService;
import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeizhalsWebService {
    private final GeizhalsScraperService scraperService;
    private final CSVWriterService writerService;

    public void refreshScrapeResults(FileWriter fileWriter) {
        try {
            Map<Product, List<GeizhalsProduct>> data = scraperService.scrape();
            writerService.writeScrapedDataToCsv(data, fileWriter);
        } catch (IOException e) {
            log.error("Error: " + e);
        }
    }

}
