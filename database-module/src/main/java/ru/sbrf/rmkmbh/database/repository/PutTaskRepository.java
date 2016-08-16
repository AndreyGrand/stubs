package ru.sbrf.rmkmbh.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ru.sbrf.ufs.kmcib.entity.PutTask;

public interface PutTaskRepository extends CrudRepository<PutTask, Long> {

	List<PutTask> findBySpname(String Spname);
	List<PutTask> findBySystemid(String Systemid);
	List<PutTask> findByMethod(String Method);

	List<PutTask> findBySpnameAndSystemid(String Spname, String Systemid);
	
	@Query("select s from PutTask s where id in (select c.putTask.id from ContactInfo c where c.contactid = ?1)")
	List<PutTask> findByContactInfo(String s); 
}