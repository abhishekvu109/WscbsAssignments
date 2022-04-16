package com.wscbs.group12.urlshortner.service.classengine;

import java.util.Random;

public class ClassD implements ClassEngine {
    @Override
    public String geyKey(String input, int len) {
        String charArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";
        String res = "";
        for (int i = 0; i < len; i++) {
            Random rand = new Random();
            int index = rand.nextInt(64);
            res += charArr.charAt(index);
        }
        return res;
    }
}
