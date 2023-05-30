package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.ProductListing;
import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static eu.devseal.geizhalsscraper.data.GeizhalsCssQuery.*;
import static eu.devseal.geizhalsscraper.data.StaticConstants.NO_LISTING_ID;
import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class GeizhalsScraperService {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public Map<Product, List<ProductListing>> scrape() throws IOException {
        Map<Product, List<ProductListing>> scrapedProducts = new LinkedHashMap<>();

        for (Product product : productRepository.findAll()) {
            Document doc = getDocument(product);
            List<ProductListing> productList = getProducts(doc);
            scrapedProducts.put(product, productList);
        }
        return scrapedProducts;
    }


    private Document getDocument(Product product) throws IOException {
        return Jsoup.connect(product.getGeizhalsURL())
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Accept-Language", "*")
                .get();
    }

    private List<ProductListing> getProducts(Document doc) {
        List<ProductListing> scrapedData = new ArrayList<>();
        Elements productListings = doc.select(PRODUCT_LISTINGS.url);
        getProductDetails(scrapedData, productListings);
        return scrapedData;
    }

    private void getProductDetails(List<ProductListing> scrapedData, Elements productListings) {
        for (Element product : productListings) {
            int id = getId(product.id());
            double unitPrice = getUnitPrice(product);
            String company = getCompany(product);
            List<Double> shippingCost = getShippingCost(product);
            scrapedData.add(
                    ProductListing.builder()
                            .offerID(id)
                            .unitPrice(unitPrice)
                            .company(company)
                            .shippingCost(shippingCost)
                            .build()
            );
        }
    }

    private List<Double> getShippingCost(Element product) {
        return Arrays.stream(product.select(SHIPPING_COST.url).text().replace("€ ", "").replace(",", ".").replace("-", "").split(" ")).distinct().map(price -> price.trim().length() > 0 ? Double.parseDouble(price) : (double) 0).toList();
    }

    private String getCompany(Element product) {
        return Objects.requireNonNull(product.selectFirst(COMPANY.url)).attr(COMPANY_ATTRIBUTE.url);
    }

    private int getId(String productID) {
        return Integer.parseInt(productID.substring(productID.lastIndexOf("_") + 1)) + 1;
    }

    private double getUnitPrice(Element product) {
        String unitPriceString = Objects.requireNonNull(
                        product
                                .selectFirst(UNIT_PRICE.url))
                .text()
                .replace(",", ".");
        return Double.parseDouble(unitPriceString.substring(2));
    }
}
