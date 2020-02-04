package com.iesvdc.acceso.modelo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jdbc.repository.query.Query;

@Repository
public interface RepoHorario extends JpaRepository<Horario, Long> {

	@Query("SELECT h FROM horario h WHERE h.instalacion=?1")
	List<Horario> findByInstalacionId(long instalacionId);
	
}
