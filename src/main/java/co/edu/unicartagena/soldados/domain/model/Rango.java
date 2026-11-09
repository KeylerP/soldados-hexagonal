package co.edu.unicartagena.soldados.domain.model;

import java.util.Optional;

/**
 * Value Object (enum): rango militar. Define el orden de la jerarquía.
 * La regla "solo se asciende al rango inmediatamente superior" vive aquí.
 */
public enum Rango {
    SOLDADO_REGULAR(1),
    SOLDADO_PROFESIONAL(2),
    CABO_TERCERO(3),
    CABO_SEGUNDO(4),
    CABO_PRIMERO(5),
    SARGENTO_SEGUNDO(6),
    SARGENTO_PRIMERO(7);

    private final int nivel;

    Rango(int nivel) {
        this.nivel = nivel;
    }

    public int nivel() {
        return nivel;
    }

    public Optional<Rango> siguiente() {
        for (Rango r : values()) {
            if (r.nivel == this.nivel + 1) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public boolean esSuperiorA(Rango otro) {
        return this.nivel > otro.nivel;
    }
}
