package eu.devseal.geizhalsscraper.controller;

import eu.devseal.geizhalsscraper.service.GeizhalsWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin
    byte[] evaluateScrapedData(@RequestParam(defaultValue = "false") boolean reload) throws IOException {
        if (reload || !file.exists()) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                geizhalsWebService.refreshScrapeResults(fileWriter);
            } catch (IOException e) {
                log.error("Controller Error: "+e);
            }
        }
        return Files.readAllBytes(file.toPath());
    }

}
