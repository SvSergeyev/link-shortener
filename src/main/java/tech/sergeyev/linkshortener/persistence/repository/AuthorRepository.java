package tech.sergeyev.linkshortener.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.sergeyev.linkshortener.persistence.model.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findAuthorByUsername(String username);
}
