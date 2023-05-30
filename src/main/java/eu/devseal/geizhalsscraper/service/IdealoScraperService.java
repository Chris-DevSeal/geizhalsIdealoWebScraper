package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductListing;
import eu.devseal.geizhalsscraper.data.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static eu.devseal.geizhalsscraper.data.IdealoCssQuery.*;

@Service
@RequiredArgsConstructor
public class IdealoScraperService {
    private final ProductRepository repository;

    public Map<Product, List<ProductListing>> scrape() throws IOException {
        Map<Product, List<ProductListing>> scrapedProducts = new LinkedHashMap<>();
        for (Product product : repository.findAll()) {
            Document doc = getDocument(product.getIdealoURL());
            List<ProductListing> productList = getProducts(doc);
            scrapedProducts.put(product, productList);
        }
        return scrapedProducts;
    }

    private List<ProductListing> getProducts(Document doc) {
        List<ProductListing> scrapedData = new ArrayList<>();
        Elements productListings = doc.select(PRODUCT_LISTINGS.url);
        getProductDetails(productListings, scrapedData);
        return scrapedData;
    }

    private void getProductDetails(Elements productListings, List<ProductListing> scrapedData) {
        for (Element product : productListings) {
            int id = getId(product);
            double unitPrice = getUnitPrice(product);
            String company = getCompany(product);
            double totalPrice = getTotalPrice(product);
            scrapedData.add(
                    ProductListing.builder()
                            .offerID(id)
                            .company(company)
                            .shippingCost(List.of(totalPrice - unitPrice))
                            .unitPrice(unitPrice)
                            .build()
            );
        }
    }

    private double getTotalPrice(Element product) {
        String productPriceText = product.select(PRODUCT_PRICE.url).text().replace(",", ".").replace(" ", "");
        String i = productPriceText.substring(productPriceText.lastIndexOf("€") + 1, productPriceText.lastIndexOf("i") - 1);
        return Double.parseDouble(i);
    }

    private String getCompany(Element product) {
        JSONObject jsonObject = (JSONObject) getGtmPayload(product);
        return jsonObject.get("shop_name").toString();
    }

    private double getUnitPrice(Element product) {
        JSONObject jsonObject = (JSONObject) getGtmPayload(product);
        return Double.parseDouble(jsonObject.get("product_price").toString());
    }

    private int getId(Element product) {
        JSONObject jsonObject = (JSONObject) getGtmPayload(product);
        return Integer.parseInt(jsonObject.get("position").toString());
    }

    private Object getGtmPayload(Element product) {
        String attr = product.select(PRODUCT_ATTRIBUTES.url).attr(DATA_GTM_PAYLOAD.url);
        return JSONValue.parse(attr);
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Accept-Language", "*")
                .get();
    }
}
