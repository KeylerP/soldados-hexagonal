package co.edu.unicartagena.soldados.application.dto;

/** Consulta (CQRS): pedir los datos de un soldado por su id. */
public record ConsultarSoldadoQuery(String soldadoId) {
}
