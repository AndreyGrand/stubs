package ru.sbrf.rmkmbh.database;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.rmkmbh.database.config.RmKmDatabaseConfig;
import ru.sbrf.rmkmbh.database.repository.TaskDetailsRepository;
import ru.sbrf.ufs.kmcib.entity.TaskDetails;


/**
 * Created by sbt-manayev-iye on 09.08.2016.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DbTest {

    @Autowired
    private TaskDetailsRepository repository;

    @Test
    @Transactional
    public void doTest(){
        TaskDetails entity = new TaskDetails();
        entity.setId(BigDecimal.valueOf(1000L));
        entity.setSpname("spName1");
        String method = "sdpfdfsdfsdfsdfsd23454645";
		entity.setMethod(method);
        entity = repository.save(entity);
        TaskDetails findOne = repository.findByMethod(method).get(0);
        assert findOne.getSpname().equals("spName1");
    }
}
