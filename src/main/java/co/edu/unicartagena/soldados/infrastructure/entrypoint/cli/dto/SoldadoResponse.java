package co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.dto;

/** Response de la consola: texto listo para mostrar. */
public record SoldadoResponse(String id, String nombre, String componente, String rango) {

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s", id, nombre, componente, rango);
    }
}
