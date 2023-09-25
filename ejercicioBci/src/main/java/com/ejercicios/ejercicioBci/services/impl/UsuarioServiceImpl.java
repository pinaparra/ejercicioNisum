package com.ejercicios.ejercicioBci.services.impl;

import com.ejercicios.ejercicioBci.dtos.PhoneDTO;
import com.ejercicios.ejercicioBci.dtos.UsuarioDTO;
import com.ejercicios.ejercicioBci.entities.UsuarioEntity;
import com.ejercicios.ejercicioBci.exceptions.UsuarioException;
import com.ejercicios.ejercicioBci.models.Phone;
import com.ejercicios.ejercicioBci.models.Usuario;
import com.ejercicios.ejercicioBci.responses.RegistroResponse;
import com.ejercicios.ejercicioBci.responses.UsuarioErrorResponse;
import com.ejercicios.ejercicioBci.security.UsuarioDetailsService;
import com.ejercicios.ejercicioBci.security.jwt.JwtUtil;
import com.ejercicios.ejercicioBci.services.UsuarioService;
import com.ejercicios.ejercicioBci.utils.UsuarioUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que contiene las llamadas al alacenamiento de datos y los prepara para la exposicion en los controladores.
 */
@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioEntity usuarioEntity;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioDetailsService usuarioDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UsuarioUtil usuarioUtil;


    /**
     * Registra un nuevo usuario
     * @param usuarioDTO
     * @return
     */
    @Override
    public ResponseEntity registrar(UsuarioDTO usuarioDTO){
        try{
            if(Objects.isNull(usuarioEntity.findByEmail(usuarioDTO.getEmail()))){
                usuarioUtil.validaPassword(usuarioDTO.getPassword());
                Usuario usuario = registrarUsuario(usuarioDTO);
                String token = generaToken(usuario.getEmail(), usuario.getPassword());
                log.info("Registro completado exitosamente para usuario con correo: {}.",usuario.getEmail());
                return new ResponseEntity<>(generaResponse(usuario,token),HttpStatus.CREATED);
            }else{
                throw new UsuarioException("El correo utilizado ya está en nuestros registros.",HttpStatus.BAD_REQUEST);
            }
        }catch (UsuarioException usuarioException){
            log.info("Ha ocurrido un error en el registro, el mensaje de error es : {}",usuarioException.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse(usuarioException.getMessage()),usuarioException.getHttpStatus());
        }catch (ConstraintViolationException constraintViolationException){
            log.info("Ha ocurrido un error en el registro, el correo ingresado no ha sido valido.");
            return new ResponseEntity<>(new UsuarioErrorResponse("Formato de correo incorrecto, debe seguir este patron: aaaaaaa@dominio.cl"),HttpStatus.BAD_REQUEST);
        }catch (Exception exception){
            log.info("Ha ocurrido un error en el registro, el mensaje de error es : {}",exception.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse("Error interno"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Modifica un usuario ya creado en la base de datos.
     * @param usuarioDTO
     * @return
     */
    @Override
    public ResponseEntity modificar(UsuarioDTO usuarioDTO){
        try{
            Usuario usuario = usuarioEntity.findByEmail(usuarioDTO.getEmail());
            if(!usuarioDTO.getEmail().equalsIgnoreCase(usuarioDetailsService.getDetalleUsuario().getEmail())){
                throw new UsuarioException("El correo del usuario que intenta modificar es distinto al del token JWT",
                        HttpStatus.BAD_REQUEST);
            }
            if(!Objects.isNull(usuario)){
                modificarUsuario(usuarioDTO,usuario);
                log.info("El usuario ha sido correctamente actualizado: {}.",usuario.getEmail());
                return new ResponseEntity<>(usuario,HttpStatus.OK);
            }else{
                throw new UsuarioException("Usuario no encontrado, favor verificar el mail.",HttpStatus.BAD_REQUEST);
            }
        }catch (UsuarioException usuarioException){
            log.info("Ha ocurrido un error en la modificacion, el mensaje de error es : {}",usuarioException.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse(usuarioException.getMessage()),usuarioException.getHttpStatus());
        }catch (Exception exception){
            log.info("Ha ocurrido un error en la modificacion, el mensaje de error es : {}",exception.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse("Error interno"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca un usuario en la base de datos.
     * @param usuarioDTO
     * @return
     */
    @Override
    public ResponseEntity buscar(UsuarioDTO usuarioDTO){
        try{

            Usuario usuario = usuarioEntity.findByEmail(usuarioDTO.getEmail());
            if(!Objects.isNull(usuario)){
                return new ResponseEntity<>(usuario,HttpStatus.OK);
            }else{
                throw new UsuarioException("Usuario no encontrado, favor verificar el mail.",HttpStatus.BAD_REQUEST);
            }
        }catch (UsuarioException usuarioException){
            log.info("Ha ocurrido un error en la búsqueda, el mensaje de error es : {}",usuarioException.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse(usuarioException.getMessage()),usuarioException.getHttpStatus());
        }catch (Exception exception){
            log.info("Ha ocurrido un error en la búsqueda, el mensaje de error es : {}",exception.getMessage());
            return new ResponseEntity<>(new UsuarioErrorResponse("Error interno"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo encargado de la conversion del objeto de transporte a Entity
     * @param usuarioDTO
     * @return
     */
    private Usuario registrarUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        usuario.setName(usuarioDTO.getName());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setPhones(generaListaPhones(usuarioDTO.getPhones(),usuario));
        usuario.setRole("user");
        usuarioEntity.save(usuario);
        return usuario;
    }

    /**
     * Metodo encargado de la modificacion del usuario.
     * @param usuarioDTO
     * @param usuario
     */
    private void modificarUsuario(UsuarioDTO usuarioDTO,Usuario usuario){
        usuario.setName(usuarioDTO.getName());
        usuario.setModified(LocalDateTime.now());
        usuario.setPhones(usuario.getPhones());
        usuario.setPassword(usuarioDTO.getPassword());
        usuarioEntity.save(usuario);
    }

    /**
     * Metodo para pasar la lista de telefonos al nuevo usuario creado
     * @param phonesDTO
     * @param usuario
     * @return
     */
    private List<Phone> generaListaPhones(List<PhoneDTO> phonesDTO,Usuario usuario){
        List<Phone> phones = new ArrayList<>();
        Phone phone;
        for(PhoneDTO phoneDTO:phonesDTO){
            phone = new Phone();
            phone.setNumber(phoneDTO.getNumber());
            phone.setContryCode(phoneDTO.getContrycode());
            phone.setCityCode(phoneDTO.getCitycode());
            phone.setUsuario(usuario);
            phones.add(phone);
        }
        return phones;
    }

    /**
     * genera el JSON de respuesta para el caso de registro
     * @param usuario
     * @param token
     * @return
     * @throws UsuarioException
     */
    private RegistroResponse generaResponse(Usuario usuario,String token) throws UsuarioException {
        return new RegistroResponse(usuario.getId().toString(),
                usuario.getCreated().toString(),
                usuario.getModified().toString(),
                usuario.getLastLogin().toString(),
                token,
                usuario.isActive());
    }

    /**
     * Genera el token en el registro del usuario, este token tiene una duracion de 1 hora cronologica.
     * @param email
     * @param password
     * @return
     * @throws UsuarioException
     */
    private String generaToken(String email,String password) throws UsuarioException {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password)
            );
            if(authentication.isAuthenticated()){
                return jwtUtil.generateToken(usuarioDetailsService.getDetalleUsuario().getEmail(),
                        usuarioDetailsService.getDetalleUsuario().getRole());
            }else{
                throw new UsuarioException("Ha fallado la creación del token.",HttpStatus.BAD_REQUEST);
            }
    }

}
