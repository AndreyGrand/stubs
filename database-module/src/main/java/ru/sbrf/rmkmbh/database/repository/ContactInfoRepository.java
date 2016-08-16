package ru.sbrf.rmkmbh.database.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sbrf.ufs.kmcib.entity.ContactInfo;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Long> {

	List<ContactInfo> findByContactid(String contactid);
}
