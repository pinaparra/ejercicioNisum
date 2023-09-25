package com.ejercicios.ejercicioBci.entities;

import com.ejercicios.ejercicioBci.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioEntity extends JpaRepository<Usuario,Long> {

    List<Usuario> findById(long id);

    Usuario findByEmail(String email);








}
