package jp.co.worksap.stm2018.jobhere.util;

import org.junit.Ignore;
import org.junit.Test;

public class MailTest {

    @Test
    @Ignore
    public void sendTest() {
        Mail.send("chorespore@163.com","chorespore@163.com","Test mail to myself2","Test mail to myself!!!2");
    }
}
