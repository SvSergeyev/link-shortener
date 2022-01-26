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

    @GetMapping("/{token}")
    public ResponseEntity<?> redirectToShortUrl(@PathVariable("token") String token) {
        ShortLink link = linkService.getByToken(token);
        if (link != null) {
            if (link.getTemporary() && link.getExpirationTime().isBefore(LocalDateTime.now())) {
                return new ResponseEntity<>("Link has expired", HttpStatus.BAD_REQUEST);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", link.getOriginalUrl());

            link.setClickCount(link.getClickCount() + 1);
            linkService.update(link);

            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("No such link exists", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> deleteLink(@PathVariable("token") String token) {
        if (!linkService.checkAvailabilityByToken(token)) {
            return new ResponseEntity<>("No such link exists", HttpStatus.BAD_REQUEST);
        }
        linkService.deleteByToken(token);
        return new ResponseEntity<>("Link has been removed", HttpStatus.OK);
    }

    @PatchMapping(value = "/{token}")
    public ResponseEntity<?> makeLinkTemporary(@PathVariable("token") String token) {
        if (!linkService.checkAvailabilityByToken(token)) {
            return new ResponseEntity<>("No such link exists", HttpStatus.BAD_REQUEST);
        }
        ShortLink link = linkService.getByToken(token);
        if (link.getTemporary()) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        link.setTemporary(true);
        link.setExpirationTime(LocalDateTime.now().plusMinutes(1));
        linkService.update(link);
        return new ResponseEntity<>("Link has been made temporary", HttpStatus.OK);
    }
}
