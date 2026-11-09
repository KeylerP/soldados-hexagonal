package co.edu.unicartagena.soldados.application.dto;

import java.time.LocalDate;

/** Comando (CQRS): intención de ascender a un soldado en una fecha dada. */
public record AscenderSoldadoCommand(String soldadoId, LocalDate fecha) {
}
