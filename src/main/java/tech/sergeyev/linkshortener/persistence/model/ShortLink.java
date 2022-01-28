package tech.sergeyev.linkshortener.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Long id;
    String token;
    String originalUrl;
    LocalDateTime expirationTime;
    @JsonIgnore
    int clickCount;
    @JsonIgnore
    @ManyToOne
    Author author;
    Boolean temporary = false;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShortUrl{");
        sb.append("id='").append(id).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", originalUrl='").append(originalUrl).append('\'');
        if (temporary) {
            sb.append(", expirationTime='").append(expirationTime).append('\'');
        }
        else {
            sb.append(", temporary='").append(false).append('\'');
        }
        return sb.toString();
    }
}
