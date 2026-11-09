package co.edu.unicartagena.soldados.domain.exception;

/** Se lanza cuando un dato de entrada viola una invariante (nombre vacío, id mal formado, etc.). */
public class DatosSoldadoInvalidosException extends DominioException {

    public DatosSoldadoInvalidosException(String mensaje) {
        super(mensaje);
    }
}
