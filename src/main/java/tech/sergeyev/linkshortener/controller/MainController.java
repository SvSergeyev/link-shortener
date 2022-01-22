package tech.sergeyev.linkshortener.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sergeyev.linkshortener.persistence.model.ShortUrl;
import tech.sergeyev.linkshortener.persistence.repository.ShortUrlRepository;
import tech.sergeyev.linkshortener.service.ShortLinkGenerator;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class.getSimpleName());

    private final ShortUrlRepository urlRepository;
    private final ShortLinkGenerator generator;
    @Value("${code.length}")
    private int codeLength;

    public MainController(ShortUrlRepository urlRepository,
                          ShortLinkGenerator generator) {
        this.urlRepository = urlRepository;
        this.generator = generator;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public ShortUrl createShortUrl(@RequestBody ShortUrl url) {
        String code = generator.generateCode(codeLength);
        String urlToString = URLDecoder.decode(url.getOriginalUrl(), StandardCharsets.UTF_8);
        url = new ShortUrl(code, urlToString);
        return urlRepository.save(url);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToShortUrl(@PathVariable("shortUrl") String shortUrl) {
        ShortUrl url = urlRepository.findByShortUrl(shortUrl);
        if (url != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", url.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        }
        return ResponseEntity.notFound().build();
    }
}
