package tech.sergeyev.linkshortener.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;
import tech.sergeyev.linkshortener.service.ShortLinkService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainController {
    static final Logger LOGGER = LoggerFactory.getLogger(MainController.class.getSimpleName());

    final ShortLinkService linkService;

    public MainController(ShortLinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/")
    public ResponseEntity<?> index() {
        return new ResponseEntity<>("Hello! It's main page", HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createShortUrl(@RequestBody ShortLink url) {
        return url != null
            ? ResponseEntity.ok(linkService.create(url.getOriginalUrl()))
            : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToShortUrl(@PathVariable("shortCode") String code) {
        ShortLink url = linkService.getOriginalUrlByShortCode(code);
        if (url != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", url.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
        return ResponseEntity.notFound().build();
    }
}
