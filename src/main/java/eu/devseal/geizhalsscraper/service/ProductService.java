package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.ProductListing;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static eu.devseal.geizhalsscraper.data.StaticConstants.NO_LISTING_ID;
import static java.util.Comparator.comparing;

@Service
public class ProductService {

    public double getTotalPrice(ProductListing product) {
        return product.getUnitPrice() + getSmallestShippingCost(product.getShippingCost());
    }

    public String formatDoubleToString(double price) {
        return String.format("%.2f", price).replace(",", ".");
    }

    public Double getSmallestShippingCost(List<Double> shippingCost) {
        if (shippingCost == null) {
            return 0.0;
        }
        return shippingCost.stream()
                .min(Comparator.naturalOrder())
                .orElse(shippingCost.get(0));
    }

    public double findOptimalPrice(ProductListing changingProduct, ProductListing comparingProduct) {
        double optimalPrice = getTotalPrice(changingProduct);
        double comparingPrice = getTotalPrice(comparingProduct);
        optimalPrice = comparingPrice < optimalPrice ? decreasePriceUntil(optimalPrice, comparingPrice) : increasePriceUntil(optimalPrice, comparingPrice);
        return optimalPrice - getSmallestShippingCost(changingProduct.getShippingCost());

    }

    public ProductListing findFirstProductWhereCompanyIsNot(List<ProductListing> products, List<String> companyNames) {
        return products
                .stream()
                .filter(product -> !companyNames.contains(product.getCompany()))
                .min(comparing(this::getTotalPrice))
                .orElse(listingNotFound());
    }
    public ProductListing findProductListingByCompanyName(List<ProductListing> products, List<String> companyNames) {
        return products.stream()
                .filter(product -> companyNames.contains(product.getCompany()))
                .findFirst()
                .orElse(listingNotFound());
    }
    private ProductListing listingNotFound() {
        return ProductListing.builder().offerID(NO_LISTING_ID).build();
    }

    private double increasePriceUntil(double optimalPrice, double comparingPrice) {
        while (optimalPrice < comparingPrice) {
            if (optimalPrice + 10 >= comparingPrice) {
                break;
            }
            optimalPrice += 10;
        }
        return optimalPrice;
    }

    private double decreasePriceUntil(double optimalPrice, double comparingPrice) {
        while (optimalPrice >= comparingPrice) {
            optimalPrice -= 10;
        }
        return optimalPrice;
    }
}
