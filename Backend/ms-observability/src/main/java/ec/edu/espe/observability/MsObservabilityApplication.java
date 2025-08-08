package ec.edu.espe.observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class MsObservabilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsObservabilityApplication.class, args);
    }
}
