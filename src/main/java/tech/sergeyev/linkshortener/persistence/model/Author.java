package tech.sergeyev.linkshortener.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    List<ShortLink> createdLinks;

    @Override
    public String toString() {
        return "Author{" +
                "username='" + username + '\'' +
                ", createdLinks=" + createdLinks +
                '}';
    }
}
