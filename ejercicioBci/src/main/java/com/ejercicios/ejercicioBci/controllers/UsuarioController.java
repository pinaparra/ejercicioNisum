package com.ejercicios.ejercicioBci.controllers;

import com.ejercicios.ejercicioBci.dtos.UsuarioDTO;
import com.ejercicios.ejercicioBci.models.Usuario;
import com.ejercicios.ejercicioBci.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de registrar, modificar y buscar usuarios.
 */
@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    /**
     * Registra usuarios nuevos en el sistema, no requiere autenticacion para realizarse
     * @param usuarioDTO
     * @return
     */
    @PostMapping(value="usuarios/registrar",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> registrar(@RequestBody UsuarioDTO usuarioDTO){
        return usuarioService.registrar(usuarioDTO);
    }

    /**
     * Modifica un usuario ya existente diferenciando por el email.
     * @param usuarioDTO
     * @return
     */
    @PostMapping(value="usuarios/modificar",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> modificar(@RequestBody UsuarioDTO usuarioDTO){
        return usuarioService.modificar(usuarioDTO);
    }

    /**
     * Busca un usuario en especifico por email.
     * @param usuarioDTO
     * @return
     */
    @PostMapping(value="usuarios/buscar",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buscar(@RequestBody UsuarioDTO usuarioDTO){
        return usuarioService.buscar(usuarioDTO);
    }

}
