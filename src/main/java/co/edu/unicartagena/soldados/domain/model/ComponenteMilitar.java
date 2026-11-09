package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;

/** Value Object (enum): cuerpo o componente de las fuerzas al que pertenece el soldado. */
public enum ComponenteMilitar {
    EJERCITO,
    ARMADA,
    FUERZA_AEREA;

    public static ComponenteMilitar desde(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new DatosSoldadoInvalidosException("El componente militar es obligatorio");
        }
        try {
            return valueOf(texto.trim().toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            throw new DatosSoldadoInvalidosException("Componente militar desconocido: " + texto);
        }
    }
}
