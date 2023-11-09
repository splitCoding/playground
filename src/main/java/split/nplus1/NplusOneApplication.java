package split.nplus1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NplusOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(NplusOneApplication.class, args);
    }
}
