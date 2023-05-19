package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import eu.devseal.geizhalsscraper.data.Product;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static eu.devseal.geizhalsscraper.data.CssQuery.*;
import static eu.devseal.geizhalsscraper.data.StaticConstants.NO_LISTING_ID;
import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class GeizhalsScraperService {
    private final GeizhalsProductService productService;

    public Map<Product, List<GeizhalsProduct>> scrape() throws IOException {
        Map<Product, List<GeizhalsProduct>> scrapedProducts = new LinkedHashMap<>();
        for (Product url : Product.values()) {
            Document doc = getDocument(url);
            List<GeizhalsProduct> productList = getProducts(doc);
            scrapedProducts.put(url, productList);
        }
        return scrapedProducts;
    }


    private Document getDocument(Product product) throws IOException {
        return Jsoup.connect(product.geizhalsURL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Accept-Language", "*")
                .get();
    }

    private List<GeizhalsProduct> getProducts(Document doc) {
        List<GeizhalsProduct> scrapedData = new ArrayList<>();
        Elements productListings = doc.select(PRODUCT_LISTINGS.val);
        getProductDetails(scrapedData, productListings);
        return scrapedData;
    }

    private void getProductDetails(List<GeizhalsProduct> scrapedData, Elements productListings) {
        for (Element product : productListings) {
            int id = getId(product.id());
            double unitPrice = getUnitPrice(product);
            String company = getCompany(product);
            List<Double> shippingCost = getShippingCost(product);
            scrapedData.add(
                    GeizhalsProduct.builder()
                            .offerID(id)
                            .unitPrice(unitPrice)
                            .company(company)
                            .shippingCost(shippingCost)
                            .build()
            );
        }
    }

    private List<Double> getShippingCost(Element product) {
        return Arrays.stream(product.select(SHIPPING_COST.val).text().replace("€ ", "").replace(",", ".").replace("-", "").split(" ")).distinct().map(price -> price.trim().length() > 0 ? Double.parseDouble(price) : (double) 0).toList();
    }

    private String getCompany(Element product) {
        return Objects.requireNonNull(product.selectFirst(COMPANY.val)).attr(COMPANY_ATTRIBUTE.val);
    }

    private int getId(String productID) {
        return Integer.parseInt(productID.substring(productID.lastIndexOf("_") + 1)) + 1;
    }

    private double getUnitPrice(Element product) {
        String unitPriceString = Objects.requireNonNull(
                        product
                                .selectFirst(UNIT_PRICE.val))
                .text()
                .replace(",", ".");
        return Double.parseDouble(unitPriceString.substring(2));
    }

    public List<GeizhalsProduct> findBestPerformingCompanies(List<GeizhalsProduct> products) {
        return List.copyOf(products).stream()
                .sorted(comparing(productService::getTotalPrice))
                .limit(2)
                .collect(Collectors.toList());
    }

    public List<GeizhalsProduct> findBestPerformingCompanies(List<GeizhalsProduct> products, int limit) {
        return List.copyOf(products).stream()
                .sorted(comparing(productService::getTotalPrice))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public GeizhalsProduct findProductListingByCompanyName(List<GeizhalsProduct> products, String companyName) {
        return products.stream()
                .filter(product -> product.getCompany().equals(companyName))
                .findFirst()
                .orElse(listingNotFound());
    }

    private GeizhalsProduct listingNotFound() {
        return GeizhalsProduct.builder().offerID(NO_LISTING_ID).build();
    }

    public GeizhalsProduct findFirstProductWhereCompanyIsNot(List<GeizhalsProduct> products, String companyName) {
        return products
                .stream()
                .filter(product -> !product.getCompany().equals(companyName))
                .findFirst()
                .orElse(listingNotFound());
    }
}
