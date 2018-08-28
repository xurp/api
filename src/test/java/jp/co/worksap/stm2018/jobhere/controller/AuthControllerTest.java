package jp.co.worksap.stm2018.jobhere.controller;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class AuthControllerTest {

    @Test
    public void getUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
        assertEquals(uuid, uuid);
    }
}