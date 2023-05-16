package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GeizhalsProductService {

    public double getTotalPrice(GeizhalsProduct product) {
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
}
