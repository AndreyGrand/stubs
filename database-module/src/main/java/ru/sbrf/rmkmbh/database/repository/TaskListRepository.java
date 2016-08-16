package ru.sbrf.rmkmbh.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sbrf.ufs.kmcib.entity.TaskList;

public interface TaskListRepository extends CrudRepository<TaskList, Long> {

	List<TaskList> findBySpname(String Spname);
	List<TaskList> findBySystemid(String Systemid);
	List<TaskList> findByMethod(String Method);

	List<TaskList> findBySpnameAndSystemid(String Spname, String Systemid);
	
	List<TaskList> findByUserlogin(String userlogin);
	List<TaskList> findByOrgtype(String orgtype);
}
