package pl.com.foks.myexpensesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyExpensesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyExpensesBackendApplication.class, args);
    }

}
