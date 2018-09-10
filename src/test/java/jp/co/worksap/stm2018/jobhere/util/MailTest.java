package jp.co.worksap.stm2018.jobhere.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MailTest {

    @Test
    @Ignore
    public void sendTest() {

        Mail.send("chorespore@163.com", "chorespore@163.com", "Test mail to myself using new Thread!", "Test mail to myself!!!");
        Mail.send("chorespore@163.com", "chorespore@163.com", "Test mail to myself using new Thread!2", "Test mail to myself!!!2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished!");
    }
}
