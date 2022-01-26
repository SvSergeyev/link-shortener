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

import java.time.LocalDateTime;

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
        return new ResponseEntity<>("Hey! You are on the main page. Please login or register to continue", HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createShortUrl(@RequestBody ShortLink url) {
        return url != null
            ? ResponseEntity.ok(linkService.create(url.getOriginalUrl()))
            : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToShortUrl(@PathVariable("shortCode") String code) {
        ShortLink link = linkService.getByShortCode(code);
        if (link != null) {
            if (link.getExpirationTime().isBefore(LocalDateTime.now())) {
                return new ResponseEntity<>("Link has expired", HttpStatus.BAD_REQUEST);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", link.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("No such link exists", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<?> deleteLink(@PathVariable("shortCode") String code) {
        if (!linkService.checkAvailabilityByShortcode(code)) {
            return new ResponseEntity<>("No such link exists", HttpStatus.BAD_REQUEST);
        }
        linkService.deleteByShortCode(code);
        return new ResponseEntity<>("Link has been removed", HttpStatus.OK);
    }

    @PatchMapping(value = "/{shortCode}")
    public ResponseEntity<?> makeLinkTemporary(@PathVariable("shortCode") String code) {
        if (!linkService.checkAvailabilityByShortcode(code)) {
            return new ResponseEntity<>("No such link exists", HttpStatus.BAD_REQUEST);
        }
        linkService.update(code);
        return new ResponseEntity<>("Link has been made temporary", HttpStatus.OK);
    }
}
