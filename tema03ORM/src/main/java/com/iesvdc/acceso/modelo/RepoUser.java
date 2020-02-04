package com.iesvdc.acceso.modelo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoUser extends JpaRepository<User, Long>{
	
	List<User> findByUser(String username);
	List <User> findByUserAndPwd(String username, String Password);
}
