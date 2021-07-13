package org.occideas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "org.occideas",
        exclude = { SecurityAutoConfiguration.class })
@ImportResource("applicationContext.xml")
public class OccideasApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OccideasApplication.class, args);
    }

}
