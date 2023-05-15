package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.GeizhalsProduct;
import org.springframework.stereotype.Service;

@Service
public class GeizhalsProductService {

    public double getTotalPrice(GeizhalsProduct product) {
        double smallestShippingCost = product.getShippingCost().stream()
                .min(Double::compareTo)
                .orElse(product.getShippingCost().get(0));
        return product.getUnitPrice() + smallestShippingCost;
    }
}
