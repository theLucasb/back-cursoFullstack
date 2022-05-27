package br.com.tisemcensura.fullstack.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.tisemcensura.fullstack.entities.Users;
import br.com.tisemcensura.fullstack.repositories.UsersRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepository repository;
	
	private List<Users> users = new ArrayList<>();
	private String username;
	private String password;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		setUsers(repository.findAll());
		for(Users usuario: this.getUsers()) {
			if(usuario.getUsername().equals(username)) {
				this.username = usuario.getUsername();
				this.password = usuario.getPassword();
			}
		}
		return new User(this.username, this.password, new ArrayList<>());
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

}
