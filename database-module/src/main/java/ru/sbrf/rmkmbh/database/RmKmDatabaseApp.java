package ru.sbrf.rmkmbh.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.sbrf.rmkmbh.database.config.RmKmDatabaseConfig;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 */
@SpringBootApplication
@Import(RmKmDatabaseConfig.class)
public class RmKmDatabaseApp {
    private static final Logger log = LoggerFactory.getLogger(RmKmDatabaseApp.class);
    public static void main(String[] args) {
        SpringApplication.run(RmKmDatabaseApp.class, args);
        log.debug("RmKmDatabaseApp started");
        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
