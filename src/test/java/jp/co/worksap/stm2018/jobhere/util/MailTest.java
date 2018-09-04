package jp.co.worksap.stm2018.jobhere.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MailTest {
    @Test
    public void sendTest() {
        Mail.send("chorespore@163.com","chorespore@163.com","Test mail to myself2","Test mail to myself!!!2");
    }
}