package com.iesvdc.acceso.controlador;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iesvdc.acceso.modelo.RepoUsuario;
import com.iesvdc.acceso.modelo.User;
import com.iesvdc.acceso.modelo.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	@Autowired RepoUsuario repoUsuario;
/*
	@PostMapping("user")
	public User login(@RequestParam("user") String username, @RequestParam("pwd") String pwd) {
		// TODO Mirar en la BBDD si es correcta la contraseña y si lo es generamos el token
		User user = new User();
		Usuario usuario = repoUsuario.findUserByUsernameAndPassword(username, pwd); 
		if (usuario!=null) {
			String token = getJWTToken(username);
			user.setUser(username);
			user.setToken(token);			
		} else {
			throw new ResourceNotFoundException();
		}
		return user;	
	}

	private String getJWTToken(String username) {
		String secretKey = "ohq829y3rhfai8efhlnmbpoxj1y2";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("vdcAcceso")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(
						new Date(System.currentTimeMillis() + 1000*60*60*24*7)) 
				// 1000 ms/sec * 60 sec/min * 60 min/hour * 24 hour/day * 7 days = 604.800.000 
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	
	*/
}