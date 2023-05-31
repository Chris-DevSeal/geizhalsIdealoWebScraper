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
                            .build(),
                    Product.builder()
                            .name("Saeco New Royal Black")
                            .geizhalsURL("https://geizhals.at/saeco-royal-black-a2414719.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/200979376_-9j0040-royal-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco New Royal OTC")
                            .geizhalsURL("https://geizhals.at/saeco-new-royal-one-touch-cappucino-9j0080-a2465588.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/201072951_-new-royal-otc-9j0080-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Aulika Evo Black")
                            .geizhalsURL("https://geizhals.at/philips-saeco-aulika-evo-black-10000045-a2171537.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/6743777_-aulika-evo-black-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Aulika Evo Focus")
                            .geizhalsURL("https://geizhals.at/philips-saeco-aulika-evo-focus-10000040-a2171515.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/6743825_-aulika-evo-focus-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Aulika Evo Office")
                            .geizhalsURL("https://geizhals.at/philips-saeco-aulika-evo-office-10000044-a2183014.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/6325207_-aulika-office-black-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Aulika Evo Top")
                            .geizhalsURL("https://geizhals.at/saeco-aulika-evo-top-hsc-10005374-a2411516.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/6569152_-aulika-top-evo-high-speed-cappuccino-saeco.html")
                            .build(),
                    Product.builder()
                            .name("Saeco Aulika Evo Top RI")
                            .geizhalsURL("https://geizhals.at/saeco-aulika-evo-top-hsc-ri-10005373-a2411511.html")
                            .idealoURL("https://www.idealo.at/preisvergleich/OffersOfProduct/6569166_-aulika-top-evo-ri-high-speed-cappuccino-saeco.html")
                            .build()
            );
            repository.saveAll(products);
        };
    }
}
