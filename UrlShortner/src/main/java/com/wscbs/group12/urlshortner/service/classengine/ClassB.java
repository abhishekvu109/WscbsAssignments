package com.wscbs.group12.urlshortner.service.classengine;

public class ClassB implements ClassEngine {

    @Override
    public String geyKey(String input, int len) {
        return new String(new StringBuilder(input).reverse()).substring(0, len);
    }
}
