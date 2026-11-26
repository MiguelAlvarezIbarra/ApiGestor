package com.mx.cbtis.gestor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@RestController
public class UsuarioController {

	private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

	@Autowired
	UsuarioRepository usuariotable;

	private String hashPasswordIfPlain(String password) {
		if (password != null && !password.startsWith("$2")) {
			return PASSWORD_ENCODER.encode(password);
		}
		return password;
	}

	@PostMapping("/getUsuarioLogin")
	public Usuario getUsuarioLogin(@RequestBody Usuario userParam) {
		Optional<Usuario> optionaluser = usuariotable.findByCorreo(userParam.getCorreo_electronico());
		if (optionaluser.isPresent()
				&& PASSWORD_ENCODER.matches(String.valueOf(userParam.getContrasena()), optionaluser.get().getContrasena())) {
			return optionaluser.get();
		}
		Usuario user = new Usuario();
		user.setNombre("000");
		return user;
	}

	@GetMapping("/buscarUsuario")
	public Usuario getUsuario(@RequestParam int id) {
		Usuario buscar = new Usuario();
		Optional<Usuario> usuario = usuariotable.findById(id);
		if (usuario.isPresent()) {
			buscar = usuario.get();
		}
		return buscar;
	}

	@PostMapping("/addUsuario")
	public int addUsuario(@RequestBody(required = false) Usuario usuario) {
		int flag = 0;
		if (usuario != null) {
			usuario.setContrasena(hashPasswordIfPlain(usuario.getContrasena()));
			usuariotable.save(usuario);
			flag = 1;
		}
		return flag;
	}

	@PostMapping("/updateUsuario")
	public int updateUsuario(@RequestBody(required = false) Usuario usuario) {
		int flag = 0;
		if (usuario != null) {
			usuario.setContrasena(hashPasswordIfPlain(usuario.getContrasena()));
			usuariotable.save(usuario);
			flag = 1;
		}
		return flag;
	}

	@DeleteMapping("/deleteUsuario")
	public int deleteUsuario(@RequestBody(required = false) Usuario usuario) {
		int flag = 0;
		if (usuario != null) {
			usuariotable.delete(usuario);
			flag = 1;
		}
		return flag;
	}

	@PostMapping("/buscarUsuarios")
	public List<Usuario> getUsuarios() {
		List<Usuario> listaUsuarios = new ArrayList<>();
		usuariotable.findAll().forEach(listaUsuarios::add);
		return listaUsuarios;
	}
}
