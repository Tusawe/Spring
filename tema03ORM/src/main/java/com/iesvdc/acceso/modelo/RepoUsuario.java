package com.iesvdc.acceso.modelo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {
	
	
	@Query("SELECT u FROM usuario u WHERE u.username = ?1")
	Usuario findUserByUsername(String username);
	
	@Query("SELECT u FROM usuario u WHERE u.username = ?1 and u.password = ?2")
	Usuario findUserByUsernameAndPassword(String username, String password);

	//List<Usuario> findByUsername(String username);
	// List<Usuario> findByEmail(String email);
	
	//public List<Reserva> findReservas(Long id);
	//public List<Reserva> findReservas(Usuario u);
}
