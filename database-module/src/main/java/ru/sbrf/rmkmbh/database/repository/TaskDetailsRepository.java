package ru.sbrf.rmkmbh.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sbrf.ufs.kmcib.entity.TaskDetails;

public interface TaskDetailsRepository extends CrudRepository<TaskDetails, Long> {

	List<TaskDetails> findBySpname(String Spname);
	List<TaskDetails> findBySystemid(String Systemid);
	List<TaskDetails> findByMethod(String Method);
	List<TaskDetails> findByMessageLogin(String MessageLogin);
	List<TaskDetails> findByMessageActionid(String MessageActionid);
	
	List<TaskDetails> findByMethodAndMessageLoginAndMessageActionid(String Method, String MessageLogin, String MessageActionid);
	List<TaskDetails> findByMessageLoginAndMessageActionid(String MessageLogin, String MessageActionid);
	List<TaskDetails> findBySpnameAndSystemid(String Spname, String Systemid);
	
}
