package co.edu.unicartagena.soldados.application.dto;

import java.time.LocalDate;

/** Respuesta de la capa de aplicación: foto de solo lectura de un soldado. */
public record SoldadoView(String id, String nombreCompleto, String componente, String rango,
                          LocalDate fechaIngreso, LocalDate fechaUltimoAscenso) {
}
