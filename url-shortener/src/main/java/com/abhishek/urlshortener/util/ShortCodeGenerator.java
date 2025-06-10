package com.abhishek.urlshortener.util;

public class ShortCodeGenerator {

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 5;

    public static String generateCode(long input) {
        StringBuilder sb = new StringBuilder();

        while (input > 0) {
            sb.append(BASE62.charAt((int) (input % 62)));
            input /= 62;
        }

        while (sb.length() < CODE_LENGTH) {
            sb.append('0');
        }

        String code = sb.reverse().substring(0, CODE_LENGTH);

        if (!code.matches(".*[a-zA-Z].*")) {
            char letter = 'a';
            long time = System.currentTimeMillis();
            int alphabetIndex = (int) (time % 52);
            letter = BASE62.substring(10).charAt(alphabetIndex);
            code = code.substring(0, CODE_LENGTH - 1) + letter;
        }

        return code;
    }
}
