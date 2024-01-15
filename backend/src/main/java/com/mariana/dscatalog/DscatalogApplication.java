package com.mariana.dscatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mariana.dscatalog.services.S3Service;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DscatalogApplication  implements CommandLineRunner {
	
	@Autowired
	private S3Service s3service;

	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

    @PostConstruct
    public void init() {
        
        try {
            s3service.uploadFile("C:\\temp\\print.jpg");
        } catch (Exception e) {
           
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        
    }
}