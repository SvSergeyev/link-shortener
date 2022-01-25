package tech.sergeyev.linkshortener.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.sergeyev.linkshortener.persistence.model.Author;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;

import java.util.List;

@Repository
public interface ShortLinkRepository extends CrudRepository<ShortLink, Long> {
    ShortLink findByShortCode(String url);
    ShortLink findByOriginalUrl(String url);
    List<ShortLink> findAllByAuthor(Author author);
    boolean existsByOriginalUrl(String url);
    boolean existsByShortCode(String code);
}
