package tech.sergeyev.linkshortener.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tech.sergeyev.linkshortener.persistence.model.Author;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;
import tech.sergeyev.linkshortener.persistence.repository.ShortLinkRepository;
import tech.sergeyev.linkshortener.service.security.AuthorDetailsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortLinkService {

    final ShortLinkRepository repository;
    final ShortLinkGenerator generator;
    final AuthorDetailsService authorService;
    @Value("${code.length}")
    int codeLength;

    public ShortLinkService(ShortLinkRepository repository,
                            ShortLinkGenerator generator,
                            AuthorDetailsService authorService) {
        this.repository = repository;
        this.generator = generator;
        this.authorService = authorService;
    }

    public ShortLink create(String originalUrl) {
        ShortLink link = new ShortLink();

        UserDetails authorizedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: юзер не кастуется в автора!!!
        Author author = (Author) authorService.loadUserByUsername(authorizedUser.getUsername());
        link.setAuthor(author);

        String code = generator.generateCode(codeLength);
        link.setShortCode(code);

        String originalUrlToString = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        link.setOriginalUrl(originalUrlToString);

        link.setCreatedAt(LocalDateTime.now());

        return repository.save(link);
    }

    public ShortLink getOriginalUrlByShortCode(String code) {
        return repository.findByShortCode(code);
    }
}
