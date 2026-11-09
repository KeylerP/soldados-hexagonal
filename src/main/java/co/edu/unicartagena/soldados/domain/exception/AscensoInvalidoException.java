package co.edu.unicartagena.soldados.domain.exception;

/** Se lanza cuando se intenta un ascenso que rompe las reglas de la jerarquía militar. */
public class AscensoInvalidoException extends DominioException {

    public AscensoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
