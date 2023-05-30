package eu.devseal.geizhalsscraper.runner;

import eu.devseal.geizhalsscraper.data.Product;
import eu.devseal.geizhalsscraper.data.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductPopulator {
    @Bean
    ApplicationRunner populateProducts(ProductRepository repository) {
        return args -> {
            List<Product> products = List.of(
                    Product.builder()
                            .name("Saeco Lirika Black")
                            .geizhalsURL("https://geizhals.at/philips-saeco-lirika-10004476-a1075297.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/4004808_-lirika-coffee-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Lirika Plus")
                            .geizhalsURL("https://geizhals.at/philips-saeco-lirika-macchiato-10004477-a1075335.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/4173595_-lirika-macchiato-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Lirika OTC")
                            .geizhalsURL("https://geizhals.at/philips-saeco-lirika-one-touch-cappuccino-titan-10004768-a1548532.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/4962039_-lirika-one-touch-saeco.html")
                            .build()
            );
            repository.saveAll(products);
        };
    }
}
