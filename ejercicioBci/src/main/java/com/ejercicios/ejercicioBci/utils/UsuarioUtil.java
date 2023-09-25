package com.ejercicios.ejercicioBci.utils;

import com.ejercicios.ejercicioBci.exceptions.UsuarioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Slf4j
public class UsuarioUtil {

    @Value("${regex.password}")
    private String regexPassword;

    public boolean validaPassword(String password) throws UsuarioException {
        Pattern pattern = Pattern.compile(regexPassword);
        if(pattern.matcher(password).matches()){
            return true;
        }else{
            log.info("Ha ocurrido un error en el registro, la clave ingresada no ha cumplido la validacion REGEX");
            throw new UsuarioException("La contraseña ingresada no es válida.", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
