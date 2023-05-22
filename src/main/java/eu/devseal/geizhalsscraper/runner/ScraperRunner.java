package eu.devseal.geizhalsscraper.runner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ScraperRunner {

    @Bean
    File file() {
        return new File("src/main/resources/output/scrapedData.csv");
    }

}
