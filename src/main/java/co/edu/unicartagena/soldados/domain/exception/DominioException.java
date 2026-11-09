package co.edu.unicartagena.soldados.domain.exception;

/**
 * Raíz de todas las excepciones de negocio del dominio.
 * Es una RuntimeException para no ensuciar las firmas con "throws".
 */
public abstract class DominioException extends RuntimeException {

    protected DominioException(String mensaje) {
        super(mensaje);
    }
}
