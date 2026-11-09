package co.edu.unicartagena.soldados.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad interna del agregado Soldado: deja constancia de cada ascenso.
 * Solo se crea desde la raíz (Soldado); nadie de afuera la modifica.
 */
public record RegistroDeAscenso(Rango rangoAnterior, Rango rangoNuevo, LocalDate fecha) {

    public RegistroDeAscenso {
        Objects.requireNonNull(rangoAnterior, "rangoAnterior");
        Objects.requireNonNull(rangoNuevo, "rangoNuevo");
        Objects.requireNonNull(fecha, "fecha");
    }
}
