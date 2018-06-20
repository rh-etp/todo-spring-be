package au.com.redhat.labs.demos.todoapi.common.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author faisalmasood fmasood@redhat.com
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ReactiveUserDetailsServiceAutoConfiguration.class})
@ComponentScan(basePackages = "au.com.redhat.labs.demos.todoapi")
public class LabsReactiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabsReactiveApplication.class, args);
    }
}
