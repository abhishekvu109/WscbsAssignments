package com.wscbs.group12.urlshortner.service.classengine;

public class ClassA implements ClassEngine {

    @Override
    public String geyKey(String input, int len) {
        return input.substring(0, len);
    }
}
