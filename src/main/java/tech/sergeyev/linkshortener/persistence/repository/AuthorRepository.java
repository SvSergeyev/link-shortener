package tech.sergeyev.linkshortener.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.sergeyev.linkshortener.persistence.model.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findAuthorByUsername(String username);
    Boolean existsAuthorByUsername(String username);
}
