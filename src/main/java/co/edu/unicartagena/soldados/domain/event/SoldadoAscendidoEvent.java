package co.edu.unicartagena.soldados.domain.event;

import co.edu.unicartagena.soldados.domain.model.Rango;

import java.time.LocalDate;

/** Ocurrió: un soldado subió de rango cumpliendo las reglas de la jerarquía. */
public record SoldadoAscendidoEvent(String soldadoId, Rango rangoAnterior, Rango rangoNuevo, LocalDate fecha)
        implements DomainEvent {
}
