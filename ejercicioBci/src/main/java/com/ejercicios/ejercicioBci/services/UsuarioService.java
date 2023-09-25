package com.ejercicios.ejercicioBci.services;

import com.ejercicios.ejercicioBci.dtos.UsuarioDTO;
import com.ejercicios.ejercicioBci.models.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Usuario> registrar(UsuarioDTO usuarioDTO);
    ResponseEntity modificar(UsuarioDTO usuarioDTO);
    ResponseEntity buscar(UsuarioDTO usuarioDTO);

}
