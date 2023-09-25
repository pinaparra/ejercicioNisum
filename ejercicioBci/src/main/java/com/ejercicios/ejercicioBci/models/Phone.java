package com.ejercicios.ejercicioBci.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String number;

    private String cityCode;

    private String contryCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
}
