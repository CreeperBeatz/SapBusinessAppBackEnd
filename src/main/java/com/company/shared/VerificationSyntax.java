package com.company.shared;

import java.util.regex.Pattern;

public class VerificationSyntax {

    /**
     * With a given String, Pattern.matches is executed
     * email must contain word, followed by @, followed
     * by word, followed by . , followed by 2-4 chars
     *
     * @param email String to be checked
     * @return true if regex matches, false if it doesn't
     */
    public static boolean verifyEmail(String email) {
        if(Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * With a given String, Pattern.matches is executed
     * password must contain minimum eight characters,
     * at least one letter and one number:
     *
     * @param password String to checked
     * @return true if regex matches, false if it doesn't
     */
    public static boolean verifyPassword(String password) {
        if(Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", password)) {
            return true;
        } else {
            return false;
        }
    }
}
