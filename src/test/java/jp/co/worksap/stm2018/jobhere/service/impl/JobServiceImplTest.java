package jp.co.worksap.stm2018.jobhere.service.impl;

import org.junit.Test;


import java.sql.Timestamp;

import static org.junit.Assert.*;

public class JobServiceImplTest {

    @Test
    public void update() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        assertEquals(timestamp, timestamp);
    }
}