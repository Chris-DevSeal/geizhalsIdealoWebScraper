package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.ProductListing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    ProductService productService = new ProductService();
    ProductListing productListing = ProductListing.builder()
            .offerID(1)
            .shippingCost(List.of(1.5, 2.3, 3.1))
            .company("comat.at")
            .unitPrice(123.45)
            .build();
    ProductListing productListing2 = ProductListing.builder()
            .offerID(2)
            .shippingCost(List.of(4.5, 5.7, 6.9))
            .company("other")
            .unitPrice(145.45)
            .build();

    @Test
    void getTotalPrice() {
        double expected = 124.95;
        double actual = productService.getTotalPrice(productListing);
        assertEquals(expected, actual);
    }

    @Test
    void formatDoubleToString() {
        String expected = "123.45";
        String actual = productService.formatDoubleToString(123.45);
        assertEquals(expected, actual);
    }

    @Test
    void getSmallestShippingCost() {
        double expected = 1.5;
        double actual = productService.getSmallestShippingCost(productListing.getShippingCost());
        assertEquals(expected, actual);
    }

    @Test
    void findOptimalPrice() {
        double expected = 143.45;
        double actual = productService.findOptimalPrice(productListing, productListing2);
        assertEquals(expected, actual);
    }

    @Test
    void findFirstProductWhereCompanyIsNot() {
        List<ProductListing> productListings = List.of(productListing, productListing2);
        List<String> companyNames = List.of(productListing.getCompany());
        ProductListing expected = productListing2;
        ProductListing actual = productService.findFirstProductWhereCompanyIsNot(productListings, companyNames);
        assertEquals(expected, actual);
    }

    @Test
    void findProductListingByCompanyName() {
        List<ProductListing> productListings = List.of(productListing, productListing2);
        List<String> companyNames = List.of(productListing.getCompany());
        ProductListing expected = productListing;
        ProductListing actual = productService.findProductListingByCompanyName(productListings, companyNames);
        assertEquals(expected, actual);
    }
}