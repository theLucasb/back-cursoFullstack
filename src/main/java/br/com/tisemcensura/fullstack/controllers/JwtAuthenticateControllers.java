package br.com.tisemcensura.fullstack.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.tisemcensura.fullstack.entities.Users;
import br.com.tisemcensura.fullstack.repositories.UsersRepository;
import br.com.tisemcensura.fullstack.security.JwtTokenUtil;
import br.com.tisemcensura.fullstack.security.JwtUserDetailsService;
import br.com.tisemcensura.fullstack.security.requests.JwtRequest;

@CrossOrigin
@RestController
public class JwtAuthenticateControllers {

	private List<Users> usuarios = new ArrayList<>();
	private String token;
	private Long id;

	@Autowired
	private UsersRepository repository;

//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Optional<Users> createAuthenticateToken(@RequestBody JwtRequest authenticateRequest) throws Exception {
		usuarios = repository.findAll();
		for (Users usuario : usuarios) {
			if (usuario.getUsername().equals(authenticateRequest.getUsername())) {
				authenticate(authenticateRequest.getUsername(), authenticateRequest.getPassword());
				final UserDetails userDetails =  jwtUserDetailService
						.loadUserByUsername(authenticateRequest.getUsername());
				
				this.token = jwtTokenUtil.generateToken(userDetails);
				this.id = usuario.getId(); 
				Optional<Users> obj = null;
				obj = repository.findById(this.id);
				obj.orElseThrow().setToken(token);
				obj.orElseThrow().setPassword("");
				return obj;
				
			}
		}
		this.id = (long) 1; 
		Optional<Users> obj = null;
		obj = repository.findById(this.id);
		obj.orElseThrow().setToken(null);
		obj.orElseThrow().setId(null);
		obj.orElseThrow().setEmail("Erro ao realizar login");
		obj.orElseThrow().setPassword("");
		obj.orElseThrow().setUsername("Nome de usuário ou senha incorretos!");
		return obj;
	}
	
	private void authenticate(String username, String password) throws Exception {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		} catch (DisabledException e) {

			throw new Exception("USER_DISABLED", e);

		} catch (BadCredentialsException e) {

			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}


}
