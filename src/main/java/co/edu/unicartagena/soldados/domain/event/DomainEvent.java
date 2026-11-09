package co.edu.unicartagena.soldados.domain.event;

import java.time.LocalDate;

/** Contrato común de los eventos de dominio: algo relevante que ya ocurrió. */
public interface DomainEvent {

    String soldadoId();

    LocalDate fecha();
}
