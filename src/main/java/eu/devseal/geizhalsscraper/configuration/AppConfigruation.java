package eu.devseal.geizhalsscraper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class AppConfigruation {

    @Bean
    File file() {
        return new File("src/main/resources/output/scrapedData.csv");
    }

}
