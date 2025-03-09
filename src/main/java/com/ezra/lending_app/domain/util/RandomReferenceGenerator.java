package com.ezra.lending_app.domain.util;

import org.apache.commons.text.RandomStringGenerator;

public class RandomReferenceGenerator {
    public static String generateReference() {
        final char[] allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        final int referenceLength = 10;
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(allowedCharacters)
                .get();
        return generator.generate(referenceLength);
    }
}
