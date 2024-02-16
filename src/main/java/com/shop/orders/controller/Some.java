package com.shop.orders.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class Some {

    public static void main(String ... args) {

        tr1();

    }

    @Transactional
    public static void tr1() {
        System.out.println("First");
        tr2();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public static void tr2() {
        System.out.println("Second");
    }
}
