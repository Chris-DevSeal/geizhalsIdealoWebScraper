package eu.devseal.geizhalsscraper.service;

import eu.devseal.geizhalsscraper.data.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductFormatter {
    public String format(Product product) {
        String[] prodArr = product.name().toLowerCase().split("_");
        for (int i = 0; i < prodArr.length; i++) {
            prodArr[i] = prodArr[i].substring(0, 1).toUpperCase() + prodArr[i].substring(1);
        }
        return String.format("Saeco %s", String.join(" ", prodArr));
    }
}
