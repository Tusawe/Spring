package com.iesvdc.acceso.modelo;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
public
@Data @AllArgsConstructor @NoArgsConstructor
class Usuario implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String email;
	
	private String token;
	//@OneToMany(mappedBy="usuario")
	//private List<Reserva> reservas;
	
	
	
}
