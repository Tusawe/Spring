package com.iesvdc.acceso.modelo;

import java.util.List;


public interface RepoUsuarioCustom {
	
		List<Usuario> findByUsername(String username);
		List<Usuario> findByEmail(String email);	
		public List<Reserva> findReservas(Usuario u);
		public List<Reserva> findReservas(Long id);
		
}
