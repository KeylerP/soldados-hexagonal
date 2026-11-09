package co.edu.unicartagena.soldados.domain.exception;

/** Se lanza cuando se pide un soldado que no existe en el sistema. */
public class SoldadoNoEncontradoException extends DominioException {

    public SoldadoNoEncontradoException(String id) {
        super("No existe un soldado con id " + id);
    }
}
