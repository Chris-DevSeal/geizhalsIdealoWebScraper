package eu.devseal.geizhalsscraper.controller;

import eu.devseal.geizhalsscraper.exceptions.CustomFileNotFoundException;
import eu.devseal.geizhalsscraper.service.GeizhalsWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/geizhals")
@RequiredArgsConstructor
@Slf4j
public class GeizhalsController {
    private final GeizhalsWebService geizhalsWebService;

    @GetMapping(produces = "text/csv")
    @CrossOrigin
    byte[] evaluateScrapedData(@RequestParam(defaultValue = "false") boolean reload) throws CustomFileNotFoundException {
        return geizhalsWebService.getCsvData(reload);
    }

}
