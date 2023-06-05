package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductListing;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ScraperService {
    Map<Product, List<ProductListing>> scrape() throws IOException;
}
