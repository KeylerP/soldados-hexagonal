package co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.dto;

/** Request de la consola: todo llega como texto, tal como lo escribe el usuario. */
public record RegistrarSoldadoRequest(String nombres, String apellidos, String componente, String fechaIngreso) {
}
