package co.edu.unicartagena.soldados.application.dto;

import java.time.LocalDate;

/** Comando (CQRS): intención de registrar un soldado. Solo datos, sin lógica. */
public record RegistrarSoldadoCommand(String nombres, String apellidos, String componente, LocalDate fechaIngreso) {
}
