package com.meaf.core.dao.service;

import java.util.Random;


public class MiscHelper {
    private static final String AB = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
    private static Random rnd = new Random();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
