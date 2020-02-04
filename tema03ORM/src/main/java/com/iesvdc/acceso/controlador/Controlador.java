package com.iesvdc.acceso.controlador;

import java.util.List;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iesvdc.acceso.modelo.Horario;
import com.iesvdc.acceso.modelo.Instalacion;
import com.iesvdc.acceso.modelo.RepoHorario;
import com.iesvdc.acceso.modelo.RepoInstalacion;
import com.iesvdc.acceso.modelo.RepoReserva;
import com.iesvdc.acceso.modelo.RepoUsuario;
import com.iesvdc.acceso.modelo.Reserva;
import com.iesvdc.acceso.modelo.Usuario;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class Controlador {
	
    // Repositorios
    @Autowired
    RepoUsuario repoUsuario;
    
    @Autowired
    RepoReserva repoReserva;
    
    @Autowired 
    RepoInstalacion repoInstalacion;
    
    @Autowired
    RepoHorario repoHorario;

    // CRUD USUARIOS
    
    // BUSCAR todos
    @GetMapping("/usuario")
    public List<Usuario> getAllUsuarios() {
        return repoUsuario.findAll();
    }
    
    // BUSCAR un usuario
    @GetMapping("/usuario/{id}")
    public Usuario getOneUsuarios(@PathVariable(value = "id") Long userId) {
    	Usuario usuario = repoUsuario.findById(userId).orElseThrow(
        		() -> new ResourceNotFoundException());
        return usuario;
    }
    
    // CREAR usuario
    @PostMapping(value="/usuario" , consumes={"application/json"} )
    @ResponseBody public Usuario createUsuario(@Valid @RequestBody Usuario usuario) {   
        return repoUsuario.save(usuario);
    }
    
    // BORRAR usuario
    @DeleteMapping("/usuario/{id}")
    public Usuario deleteUsuario(@PathVariable(value = "id") Long userId) {
        Usuario usuario = repoUsuario.findById(userId).orElseThrow(
        		() -> new ResourceNotFoundException());
        repoUsuario.delete(usuario);
        return usuario;
    }
    
    // ACTUALIZAR usuario
    @PutMapping(value="/usuario/{id}", consumes={"application/json"})
    @ResponseBody public Usuario updateUsuario(
			@PathVariable(value = "id") Long userId, 
			@Valid @RequestBody Usuario usuario) {
	    
	    Usuario updated_user = repoUsuario.findById(userId).orElseThrow(() -> new ResourceNotFoundException());
	    updated_user.setUsername(usuario.getUsername());
	    updated_user.setPassword(usuario.getPassword());
	    //new_cli.setClienteDireccionList(cliente.getClienteDireccionList());
	    return repoUsuario.save(updated_user);
    }
    
    
/*
    @GetMapping("/usuario/{id}/reservas")
    public List<Reserva> getReservas(@PathVariable(value="id") Long id) {    	
        return repoUsuario.findReservas(id);
    }*/
    
    
    // CRUD Instalaciones 
    
    // findAll()
    @GetMapping("/instalacion")
    public List<Instalacion> getAllInstalacions() {
        return repoInstalacion.findAll();
    }
    //findOne()
    @GetMapping("/instalacion/{id}")
    public Instalacion findOneInstalacion(@PathVariable(value = "id") Long instalacionId) {
        Instalacion instalacion = repoInstalacion.findById(instalacionId).orElseThrow(
        		() -> new ResourceNotFoundException());
        return instalacion;
    }
    
    //deleteOne()
    @DeleteMapping("/instalacion/{id}")
    public Instalacion deleteInstalacion(@PathVariable(value = "id") Long instalacionId) {
        Instalacion instalacion = repoInstalacion.findById(instalacionId).orElseThrow(
        		() -> new ResourceNotFoundException());
        repoInstalacion.delete(instalacion);
        return instalacion;
    }
    
    //updateOne()
    @PutMapping(value="/instalacion/{id}", consumes={"application/json"})
    @ResponseBody public Instalacion updateInstalacion(
			@PathVariable(value = "id") Long instalacionId, 
			@Valid @RequestBody Instalacion instalacion) {
	    
	    Instalacion updated_instalacion = repoInstalacion.findById(instalacionId).orElseThrow(() -> new ResourceNotFoundException());
	    updated_instalacion.setNombre(instalacion.getNombre());
	    //new_cli.setClienteDireccionList(cliente.getClienteDireccionList());
	    return repoInstalacion.save(updated_instalacion);
    }
    
  //create()
    @PostMapping(value="/instalacion", consumes={"application/json"})
    @ResponseBody public Instalacion createInstalacion(
			@Valid @RequestBody Instalacion instalacion) {
	    
	    //new_cli.setClienteDireccionList(cliente.getClienteDireccionList());
	    return repoInstalacion.save(instalacion);
    }
    
    
  //findHorarios()
    @GetMapping("/instalacion/{id}/horario")
    public List<Horario> findHorariosInstalacion(@PathVariable(value = "id") Long instalacionId) {
        Instalacion instalacion = repoInstalacion.findById(instalacionId).orElseThrow(
        		() -> new ResourceNotFoundException());
        
        return repoHorario.findByInstalacionId(instalacion.getId());
    }
    
  //findHorarios()
    @GetMapping("/instalacion/{inst}/horario/{hora}")
    public Horario findHorarioInstalacion(
    			@PathVariable(value = "inst") Long instalacionId,
    			@PathVariable(value = "hora") Long horarioId) {
        Horario horario = repoHorario.findById(horarioId).orElseThrow(
        		()->new ResourceNotFoundException());
        return horario;
    }
    
    @PostMapping(value="/instalacion/{inst}/horario",  consumes={"application/json"})
    @ResponseBody 
    public Horario createHorarioInstalacion(
    			@Valid @RequestBody Horario horario,
    			@PathVariable(value = "inst") Long instalacionId) {
        Instalacion instalacion = repoInstalacion.findById(instalacionId).orElseThrow(
        		()->new ResourceNotFoundException());
        horario.setInstalacion(instalacion);
        return horario;
    }
    
    @PutMapping(value="/instalacion/{inst}/horario/{hora}", consumes={"application/json"})
    @ResponseBody
    public Horario updateHorarioInstalacion(
    			@Valid @RequestBody Horario horario,
    			@PathVariable(value = "inst") Long instalacionId,
    			@PathVariable(value = "hora") Long horarioId) {
        Horario old_horario = repoHorario.findById(horarioId).orElseThrow(
        		()->new ResourceNotFoundException());
        Instalacion instalacion = repoInstalacion.findById(instalacionId).orElseThrow(
        		()->new ResourceNotFoundException());
        
        old_horario.setInicio(horario.getInicio());
        old_horario.setFin(horario.getFin());
        old_horario.setInstalacion(instalacion);
        
        repoHorario.save(old_horario);
        
   		return old_horario;
    }
    
    /*
    @GetMapping("/horario")
    public List<Horario> getAllHorarios(){
    	return repoHorario.findAll();
    }*/
   
    
    @GetMapping("/reserva")
    public List<Reserva> getAllReservas() {
        return repoReserva.findAll();
    }
    
    //deleteOne()
    @DeleteMapping("/instalacion/{id}/horario/{idHorario}")
    public void deleteHorario(@PathVariable(value = "id") Long instalacionId,
            @PathVariable(value = "idHorario") Long horarioId ) {
        repoHorario.deleteById(horarioId);
    }

}








