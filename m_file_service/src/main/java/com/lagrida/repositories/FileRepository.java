package com.lagrida.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.lagrida.entities.AppFile;

public interface FileRepository extends JpaRepository<AppFile, Long>{
	
	Optional<AppFile> findByFileName(String fileName);
	
	@Query("select appFile from AppFile appFile where appFile.fileName in :filesNames")
	List<AppFile> getAllFilesWithFileNames(@Param("filesNames") List<String> filesNames);
	
	@Modifying
	@Query("delete from AppFile appFile where appFile.fileName in :filesNames and appFile.userOwner=:userOwner")
	void deleteAllFilesWithFilesNames(@Param("filesNames") List<String> filesNames, @Param("userOwner") long UserIdAuthentificated);
}
