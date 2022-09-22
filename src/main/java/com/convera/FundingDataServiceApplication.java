package com.convera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Funding Data Service.
 * 
 * @Author: Nasir Ahmed
 * @Decription: This Service perform Curd Operation of Order. Also expose end
 *              points for other services.
 */
@SpringBootApplication
public class FundingDataServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FundingDataServiceApplication.class, args);
  }

}
