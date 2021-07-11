package com.lagrida.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lagrida.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
	public List<Task> findAllByOrderByIdDesc();
	
	@Query("select task from Task task, in (task.users) user where user=:UserIdAuthentificated order by task.id desc")
	public List<Task> getAllTasksToUser(@Param("UserIdAuthentificated") long UserIdAuthentificated);
	
	/*@Query("select task from Task task, in (task.users) user where user:=UserIdAuthentificated and task.id=:taskId")
	Optional<Task> findByIdAndCheckUser(@Param("taskId") long taskId, @Param("UserIdAuthentificated") long UserIdAuthentificated);*/
}
