package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoUsuario {
    CLIENTE,
    BARBEIRO,
    DONO_BARBEARIA;

    @JsonCreator
    public static TipoUsuario fromString(String value) {
        return TipoUsuario.valueOf(value.toUpperCase());
    }
}
