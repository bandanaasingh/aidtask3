package ae.anyorder.bigorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BigOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigOrderApplication.class, args);
	}
}
