package org.utils;

import java.util.Random;

public class StringUtils {
    public static String repeat(String in, int times) {
        if (times == 0) {
            return "";
        } else if (times == 1) {
            return in;
        }

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < times; ++i) {
            output.append(in);
        }
        return output.toString();
    }

    public static String generateRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345678";
        
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        String saltStr = salt.toString();
        
        return saltStr;
    }
}