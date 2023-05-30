package eu.devseal.geizhalsscraper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class AppConfiguration {

    @Bean
    File file(@Value("${spring.datasource.FILEPATH}") String filePath) {
        return new File(filePath);
    }

}
