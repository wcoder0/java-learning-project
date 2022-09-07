package com.example.base.streamapi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApi {


    @Test
    public void testList() {
        List list = new ArrayList<>();

        list.forEach(e -> {
            System.out.println(e);
        });

        list.stream().forEach(e -> {
            System.out.println(e);
        });

        list.stream().filter(e -> {

            if ((Integer) e > 10) {
                return true;
            }

            return false;
        }).collect(Collectors.toList());

        list.stream().flatMap(e -> {

            return e;
        });
    }
}
