package com.require4testing;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.require4testing.service.TestService;
import com.require4testing.service.UserService;

@SpringBootApplication(scanBasePackages = "com.require4testing")
@EntityScan(basePackages = "com.require4testing.model")
public class Require4TestingApplication {

    public static void main(String[] args) {
        //SpringApplication.run(Require4TestingApplication.class, args);
        ApplicationContext context = SpringApplication.run(Require4TestingApplication.class, args);
        
        UserService service = context.getBean(UserService.class);
        service.neuerUserSpeichern();
        
        TestService testService = context.getBean(TestService.class);
        testService.erstelleTestfall(1L);
    }
}