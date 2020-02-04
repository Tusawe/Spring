package com.iesvdc.acceso.modelo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository
public class RepoUsuarioImpl implements RepoUsuarioCustom{

	@PersistenceContext
    EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findByUsername(String username) {
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM gestion_reservas.usuario" +
                "WHERE username = \'?\'", Usuario.class);
        query.setParameter(1, username);
        return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findByEmail(String email) {
		Query query = entityManager.createNativeQuery(
				"SELECT * FROM gestion_reservas.usuario" +
                "WHERE email LIKE ?", Usuario.class);
        query.setParameter(1, email + "%");

        return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public List<Reserva> findReservas(Usuario u){
		Query query = entityManager.createNamedQuery(
				"SELECT * FROM gestion_reservas.reserva" + 
				"WHERE usuario=?", Reserva.class);
		query.setParameter(1, u.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override 
	public List<Reserva> findReservas(Long u){
		Query query = entityManager.createNamedQuery(
				"SELECT * FROM reserva " + 
				"WHERE usuario=?", Reserva.class);
		query.setParameter(1, u);
		return query.getResultList();
	}
}



