package com.ejercicios.ejercicioBci.security;

import com.ejercicios.ejercicioBci.entities.UsuarioEntity;
import com.ejercicios.ejercicioBci.models.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioEntity usuarioEntity;

    private Usuario usuario;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        usuario = usuarioEntity.findByEmail(username);
        if (!Objects.isNull(usuario)){
            return new User(usuario.getEmail(),usuario.getPassword(),new ArrayList<>());
        }else{
            log.error("Usuario o Token inválido.");
            return null;
        }
    }

    public Usuario getDetalleUsuario(){
        return usuario;
    }
}
