package com.lagrida.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lagrida.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername(String username);
	Optional<User> findById(long id);
	List<User> findByUsernameStartsWith(String username);
	Page<User> findAll(Pageable pageable);
	
	@Query("select user from User user where user.id in :usersIds")
	List<User> getAllUsersWithIds(@Param("usersIds") List<Long> usersIds);
	
}
