package com.money.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.money.api.model.Usuario;
import com.money.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService{
//	faz com que carregue pelo UserName , utilizando usario e senha

	//para trezer o usuario preciso do repositorio
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		// para logar no sistema o usuario usará o email e senha
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		
		// retorno um novo usuario Username-> email, senha e as permissoes
//		return new User(email, usuario.getSenha(), getPermissoes(usuario));
		return new UsuarioSistema(usuario, getPermissoes(usuario));// retornamos agora o usuario do sistema (logado)
	}


	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority>authorities = new HashSet<>();
		
		//carregando as Permissoes de usuario (lista das pemissões de usuario)
		//chamo um for Each , onde 
		//para cada uma da permissoes vo adicionano dentro do authorities "p -> authorities.add(e)"
		usuario.getPermissoes().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		return authorities;
	}
	
	
	
}
