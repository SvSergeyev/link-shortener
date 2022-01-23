package tech.sergeyev.linkshortener.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;
import tech.sergeyev.linkshortener.persistence.repository.ShortLinkRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortLinkService {

    final ShortLinkRepository repository;
    final ShortLinkGenerator generator;
    @Value("${code.length}")
    int codeLength;

    public ShortLinkService(ShortLinkRepository repository,
                            ShortLinkGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    public ShortLink create(String originalUrl) {
        ShortLink link = new ShortLink();

        String code = generator.generateCode(codeLength);
        link.setShortCode(code);

        String originalUrlToString = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        link.setOriginalUrl(originalUrlToString);

        link.setCreatedAt(LocalDateTime.now());

        return repository.save(link);
    }

    public ShortLink getByShortCode(String code) {
        return repository.findByShortCode(code);
    }
}
