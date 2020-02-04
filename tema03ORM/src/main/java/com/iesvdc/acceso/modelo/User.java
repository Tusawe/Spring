package com.iesvdc.acceso.modelo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public
@Data @NoArgsConstructor @AllArgsConstructor
class User implements Serializable {
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String user;
	private String pwd;
	private String token;
	private Timestamp token_valid_from;
	private Timestamp token_valid_until;
	
}
