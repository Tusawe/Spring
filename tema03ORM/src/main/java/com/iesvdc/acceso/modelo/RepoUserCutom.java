package com.iesvdc.acceso.modelo;

import java.util.List;

public interface RepoUserCutom {

	List<User> findByUser(String username);
	List <User> findByUserAndPwd(String username, String Password);
}
