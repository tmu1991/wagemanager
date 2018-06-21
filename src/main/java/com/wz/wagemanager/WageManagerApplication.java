package com.wz.wagemanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
//@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = {"com.wz.wagemanager"})
public class WageManagerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(WageManagerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		log.info ("-----------project start--------------");
	}
}
