package br.com.tisemcensura.fullstack.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.tisemcensura.fullstack.entities.Users;
import br.com.tisemcensura.fullstack.repositories.UsersRepository;
import br.com.tisemcensura.fullstack.security.JwtTokenUtil;
import br.com.tisemcensura.fullstack.security.JwtUserDetailsService;

@CrossOrigin
@RestController
public class JwtAuthenticateControllers {

	private List<Users> usuarios = new ArrayList<>();
	private String token;

	@Autowired
	private UsersRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String createAuthenticateToken(@RequestBody Users authenticateRequest) {
		usuarios = repository.findAll();
		for (Users usuario : usuarios) {

			if (usuario.getUsername().equals(authenticateRequest.getUsername())
					&& passwordEncoder.matches(authenticateRequest.getPassword(), usuario.getPassword())) {
				
				final UserDetails userDetails =  jwtUserDetailService.loadUserByUsername(authenticateRequest.getUsername());
				
				this.token = jwtTokenUtil.generateToken(userDetails);
						
				return this.token;
			}
		}
		return "Error";
	}

}
