package jp.co.worksap.stm2018.jobhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class JobhereApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobhereApplication.class, args);
    }
}
