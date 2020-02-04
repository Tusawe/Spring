package com.iesvdc.acceso.modelo;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
public 
@Data @NoArgsConstructor @ AllArgsConstructor
class Horario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Time inicio;
	private Time fin;
		
	@JoinColumn(name = "instalacion", 
			referencedColumnName = "id", 
			nullable = false)     
	@ManyToOne(optional = false)     
	@JsonBackReference
	private Instalacion instalacion;

	
	
}