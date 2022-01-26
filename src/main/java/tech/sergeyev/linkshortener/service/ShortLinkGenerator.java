package tech.sergeyev.linkshortener.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Service
public class ShortLinkGenerator {
    private final RandomStringGenerator generator;

    public ShortLinkGenerator() {
        this.generator = new RandomStringGenerator
                .Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
    }

    public String generateToken(int length) {
        return generator.generate(length);
    }
}
