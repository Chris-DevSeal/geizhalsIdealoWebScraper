package eu.devseal.geizhalsscraper.controller;

import eu.devseal.geizhalsscraper.service.GeizhalsWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("api/v1/geizhals")
@RequiredArgsConstructor
@Slf4j
public class GeizhalsController {
    private final GeizhalsWebService geizhalsWebService;
    private final File file;

    @GetMapping(produces = "text/csv")
    byte[] evaluateScrapedData() throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            geizhalsWebService.refreshScrapeResults(fileWriter);
        } catch (IOException e) {
            log.error("Constroller Error: "+e);
        }
        return Files.readAllBytes(file.toPath());
    }

}
