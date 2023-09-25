package com.ejercicios.ejercicioBci.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsuarioException extends Exception{

    HttpStatus httpStatus;
    public UsuarioException(String mensajeError,HttpStatus httpStatus){
        super(mensajeError);
        this.httpStatus = httpStatus;
    }
}
