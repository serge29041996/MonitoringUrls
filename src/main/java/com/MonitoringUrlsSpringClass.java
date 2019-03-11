/*
 * Copyright (C) 2019.
 * This file is part of project MonitoringUrls
 * Written by Sergiy Krasnikov <sergei29041996@gmail.com>
 */

package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class for start of Spring Boot application.
 */
@SpringBootApplication
public class MonitoringUrlsSpringClass {
  /**
   * Main method for start of application.
   * @param args arguments to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(MonitoringUrlsSpringClass.class, args);
  }
}
