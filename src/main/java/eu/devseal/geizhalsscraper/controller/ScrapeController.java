package eu.devseal.geizhalsscraper.controller;

import eu.devseal.geizhalsscraper.exceptions.CustomFileNotFoundException;
import eu.devseal.geizhalsscraper.service.ScrapeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/geizhals")
@RequiredArgsConstructor
@Slf4j
public class ScrapeController {
    private final ScrapeService scrapeService;

    @GetMapping(produces = "text/csv")
    @CrossOrigin
    byte[] evaluateScrapedData(@RequestParam(defaultValue = "false") boolean reload) throws CustomFileNotFoundException {
        return scrapeService.getCsvData(reload);
    }

}
