package co.edu.unicartagena.soldados.domain.event;

import java.time.LocalDate;

/** Ocurrió: un soldado fue incorporado al sistema con rango inicial. */
public record SoldadoRegistradoEvent(String soldadoId, String nombreCompleto, LocalDate fecha)
        implements DomainEvent {
}
