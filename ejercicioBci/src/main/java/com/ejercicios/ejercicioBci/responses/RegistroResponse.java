package com.ejercicios.ejercicioBci.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResponse {

    private String id;

    private String created;

    private String modified;

    private String last_login;

    private String token;

    private boolean isActive;

}
