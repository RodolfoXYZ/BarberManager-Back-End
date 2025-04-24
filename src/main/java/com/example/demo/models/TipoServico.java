package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoServico {
    DOMICILIO,
    PRESENCIAL;

    @JsonCreator
    public static TipoServico fromString(String value) {
        return TipoServico.valueOf(value.toUpperCase());
    }
}
