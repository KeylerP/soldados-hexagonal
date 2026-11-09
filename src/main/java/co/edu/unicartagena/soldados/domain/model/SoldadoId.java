package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;

import java.util.UUID;

/**
 * Value Object: identidad del soldado.
 * Invariante: siempre es un UUID válido. Inmutable.
 */
public record SoldadoId(String valor) {

    public SoldadoId {
        if (valor == null || valor.isBlank()) {
            throw new DatosSoldadoInvalidosException("El id del soldado no puede estar vacío");
        }
        try {
            UUID.fromString(valor);
        } catch (IllegalArgumentException e) {
            throw new DatosSoldadoInvalidosException("El id del soldado no es un UUID válido: " + valor);
        }
    }

    public static SoldadoId generar() {
        return new SoldadoId(UUID.randomUUID().toString());
    }

    public static SoldadoId de(String valor) {
        return new SoldadoId(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
