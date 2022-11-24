package com.sk.test.service;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
