package com.company.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {

    /**
     * As MD5 is NOT secure (dictionaries + easy brute force) it is MANDATORY to switch
     * to a more secure hashing algorithm. I've chosen it, since it's fast and easy to implement.
     * @param password String to be hashed
     * @return String with hash generated from MD5
     */
    public static String getHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100 , 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Couldn't generate hash!");
            e.printStackTrace();
            return null;
        }
    }
}
