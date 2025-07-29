package com.require4testing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;




@SpringBootApplication(scanBasePackages = "com.require4testing")
@EntityScan(basePackages = "com.require4testing.model")
public class Require4TestingApplication {
	

    public static void main(String[] args) {
    	
    	
    	 SpringApplication.run(Require4TestingApplication.class, args);
        
    }
}
