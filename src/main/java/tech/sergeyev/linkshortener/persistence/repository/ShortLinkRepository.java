package tech.sergeyev.linkshortener.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.sergeyev.linkshortener.persistence.model.ShortLink;

import java.util.Optional;

@Repository
public interface ShortLinkRepository extends CrudRepository<ShortLink, Long> {
    Optional<ShortLink> findByToken(String token);
    ShortLink findByOriginalUrl(String url);
    boolean existsByOriginalUrl(String url);
    boolean existsByToken(String token);
}
