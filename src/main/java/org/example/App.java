package org.example;

import cloud.sql.Druid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@RestController
public class App {
    private static final Logger logger
            = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws IOException {


        SpringApplication.run(App.class, args);

        //顯示 API 環境
        getApiEnv();
    }

    @RequestMapping(value = "/")
    String hello() {
        return "Hello World!";
    }




    public static  String getApiEnv() throws IOException {
        String fileName = "/DB.properties";
        InputStream is = Druid.class.getResourceAsStream(fileName);
        Properties p = new Properties();
        p.load(is);

        String APIENV = p.getProperty("APIENV");


        logger.info("===================================");
        logger.info("==========> APIENV: " + APIENV  +" <==========");
        logger.info("===================================");
        return APIENV;
    }
}