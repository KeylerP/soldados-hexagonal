package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;

/**
 * Value Object: nombre completo del soldado.
 * Invariantes: nombres y apellidos no vacíos, entre 2 y 60 caracteres,
 * solo letras, espacios, punto, apóstrofe o guion. Se normaliza con trim.
 */
public record NombreCompleto(String nombres, String apellidos) {

    private static final int MIN = 2;
    private static final int MAX = 60;
    private static final String PATRON = "[\\p{L} .'-]+";

    public NombreCompleto {
        nombres = validar(nombres, "nombres");
        apellidos = validar(apellidos, "apellidos");
    }

    private static String validar(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DatosSoldadoInvalidosException("Los " + campo + " son obligatorios");
        }
        String limpio = valor.trim();
        if (limpio.length() < MIN || limpio.length() > MAX) {
            throw new DatosSoldadoInvalidosException(
                    "Los " + campo + " deben tener entre " + MIN + " y " + MAX + " caracteres");
        }
        if (!limpio.matches(PATRON)) {
            throw new DatosSoldadoInvalidosException("Los " + campo + " contienen caracteres no permitidos");
        }
        return limpio;
    }

    public String completo() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return completo();
    }
}
