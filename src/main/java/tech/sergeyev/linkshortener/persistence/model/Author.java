package tech.sergeyev.linkshortener.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String username;
    String password;
    @OneToMany(mappedBy = "author")
    List<ShortLink> createdLinks;

    @Override
    public String toString() {
        return "Author{" +
                "username='" + username + '\'' +
                ", createdLinks=" + createdLinks +
                '}';
    }
}
