package ru.sbrf.ufs.kmcib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import ru.sbrf.efs.rmkmcib.bht.config.RmKmAppConfig;
import ru.sbrf.rmkmbh.database.config.RmKmDatabaseConfig;
import ru.sbrf.ufs.kmcib.config.*; 

import java.lang.management.ManagementFactory;

/**
 * Created by sbt-manayev-iye on 02.08.2016.
 *
 */
@SpringBootApplication
@Import({RmKmAppConfig.class,RmKmDatabaseConfig.class, WebServiceConfiguration.class})

public class MainApplication {

	 private static final Logger log = LoggerFactory.getLogger(MainApplication.class);
	    
	 /*
		public static void main(String[] args) {
			System.out.println("StubApplication is running!");
			SpringApplication.run(MainApplication.class, args);
		}*/

	    
	    private static volatile ApplicationContext applicationContext;

	    public static void main(String[] args) {
	        System.out.println("staring main RmKmBhTesterApplication");
	        String mode = args != null && args.length > 0 ? args[0] : null;
	        log.debug("PID:" + ManagementFactory.getRuntimeMXBean().getName() + " Application mode:" + mode + " context:" + applicationContext);
	        instantiate(args,mode);
	    }

	    public static synchronized void instantiate(String[] args, String mode){
	        if (applicationContext != null && mode != null && "stop".equals(mode)) {
	            System.exit(SpringApplication.exit(applicationContext, new ExitCodeGenerator() {
	                @Override
	                public int getExitCode() {
	                    return 0;
	                }
	            }));
	        } else {
	            SpringApplication app = new SpringApplication(MainApplication.class);
	            applicationContext = app.run(args);
	            log.debug("PID:" + ManagementFactory.getRuntimeMXBean().getName() + " Application started context:" + applicationContext);
	        }
	    }
}
