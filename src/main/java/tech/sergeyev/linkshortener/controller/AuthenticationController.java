package tech.sergeyev.linkshortener.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sergeyev.linkshortener.payload.AuthDto;
import tech.sergeyev.linkshortener.persistence.model.Author;
import tech.sergeyev.linkshortener.persistence.repository.AuthorRepository;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationManager authenticationManager;
    AuthorRepository authorRepository;
    PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    AuthorRepository authorRepository,
                                    PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto loginData) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.getUsername(),
                        loginData.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ResponseEntity<>("Login successful!", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@RequestBody AuthDto registrationData) {
        if (authorRepository.existsAuthorByUsername(registrationData.getUsername())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        Author author = new Author();
        author.setUsername(registrationData.getUsername());
        author.setPassword(passwordEncoder.encode(registrationData.getPassword()));

        authorRepository.save(author);

        return new ResponseEntity<>("You registered successfully", HttpStatus.OK);
    }
}
