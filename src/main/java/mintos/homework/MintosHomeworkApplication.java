package mintos.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MintosHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MintosHomeworkApplication.class, args);
    }

}
