package com.iesvdc.acceso.modelo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
public
@Data @AllArgsConstructor @NoArgsConstructor
class Reserva implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date fecha;
	
	@JoinColumn(name = "usuario", 
			referencedColumnName = "id", 
			nullable = false)     
	@ManyToOne(optional = false)     
	@JsonManagedReference
	private Usuario usuario;
	
	@JoinColumn(name = "horario", 
			referencedColumnName = "id", 
			nullable = false)     
	@ManyToOne(optional = false)     
	@JsonManagedReference
	private Horario horario;
	
}