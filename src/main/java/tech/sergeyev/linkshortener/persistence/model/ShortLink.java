package tech.sergeyev.linkshortener.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String shortCode;
    String originalUrl;
    @CreatedDate
    LocalDateTime createdAt;
    @ManyToOne
    Author author;
    Boolean temporary = false;

    @Override
    public String toString() {
        return "ShortUrl{" +
                "shortCode='" + shortCode + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}