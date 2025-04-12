package com.wzh;

import java.util.UUID;

public class Demo {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String shortUuid = uuid.toString().replace("-", "");
        System.out.println("16位字符串是: " + shortUuid);
    }
}
