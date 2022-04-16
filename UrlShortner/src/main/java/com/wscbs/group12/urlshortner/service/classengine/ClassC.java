package com.wscbs.group12.urlshortner.service.classengine;

public class ClassC implements ClassEngine {

    @Override
    public String geyKey(String input, int len) {
        String output = "";
        for (int i = 0; i < input.length() && output.length() == len; i += 2) {
            output += input.charAt(i);
        }
        return output;
    }
}
