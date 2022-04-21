package com.wscbs.group12.urlshortner.utility.discoveryservice.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonUtil {
    public static final String DATE_FORMAT = "dd-MMM-yyyy";
    private final DateFormat simpleDateFormat = new SimpleDateFormat("DDMMyyyyHHmmssSSS");

    public LocalDate getLocalDateFromString(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String generateUniqueKey() {
        return simpleDateFormat.format(new Date(System.currentTimeMillis())) + new Random().nextInt(10);
    }

    public String generateRandomString(int len) {
        //Numeral '0'
        int leftLimit = 48;
        // letter 'z'
        int rightLimit = 122;
        return new Random().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 58 || (i >= 65 && i <= 90) || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public String getBase64Encoding(String input) {
        return new String(Base64.getEncoder().encode(input.getBytes()));
    }

    public String getBase64Decoding(String input) {
        return new String(Base64.getDecoder().decode(input.getBytes()));
    }

}
