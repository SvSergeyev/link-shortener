package tech.sergeyev.linkshortener.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tech.sergeyev.linkshortener.persistence.model.Author;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;
import tech.sergeyev.linkshortener.persistence.repository.AuthorRepository;
import tech.sergeyev.linkshortener.persistence.repository.ShortLinkRepository;
import tech.sergeyev.linkshortener.service.security.AuthorDetailsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortLinkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortLinkService.class);

    final ShortLinkRepository linkRepository;
    final ShortLinkGenerator generator;
    final AuthorDetailsService authorService;
    final AuthorRepository authorRepository;
    @Value("${code.length}")
    int codeLength;

    public ShortLinkService(ShortLinkRepository linkRepository,
                            ShortLinkGenerator generator,
                            AuthorDetailsService authorService,
                            AuthorRepository authorRepository) {
        this.linkRepository = linkRepository;
        this.generator = generator;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    public ShortLink create(String originalUrl) {
        if (linkRepository.existsByOriginalUrl(originalUrl)) {
            return linkRepository.findByOriginalUrl(originalUrl);
        }

        ShortLink link = new ShortLink();

        UserDetails authorizedUser = authorService.loadUserByUsername(
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername()
        );
        Author author = authorRepository.findAuthorByUsername(authorizedUser.getUsername()).orElseThrow();
        link.setAuthor(author);

        String code = generator.generateCode(codeLength);
        link.setShortCode(code);

        String originalUrlToString = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        link.setOriginalUrl(originalUrlToString);

        link.setCreatedAt(LocalDateTime.now());

        return linkRepository.save(link);
    }

    public ShortLink getByShortCode(String code) {
        return linkRepository.findByShortCode(code).orElseThrow();
    }

    public boolean checkAvailabilityByShortcode(String code) {
        return linkRepository.existsByShortCode(code);
    }

    public void deleteByShortCode(String code) {
        linkRepository.delete(linkRepository.findByShortCode(code).orElseThrow());
    }

    public void update(String code) {
        ShortLink link = linkRepository.findByShortCode(code).orElseThrow();
        if (!link.getTemporary()) {
            link.setTemporary(true);
            link.setExpirationTime(LocalDateTime.now().plusMinutes(1));
        }
        linkRepository.save(link);
    }
}
