package co.edu.unicartagena.soldados.domain.service;

import co.edu.unicartagena.soldados.domain.exception.AscensoInvalidoException;
import co.edu.unicartagena.soldados.domain.model.Rango;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.specification.SoldadoElegibleParaAscensoSpecification;
import co.edu.unicartagena.soldados.domain.specification.Specification;

import java.time.LocalDate;

/**
 * Servicio de dominio: coordina la política de ascenso (tiempo mínimo en el rango)
 * con la regla interna del agregado. No guarda nada ni conoce la base de datos.
 */
public class ServicioAscensoMilitar {

    public static final long DIAS_MINIMOS_EN_RANGO = 365;

    private final long diasMinimos;

    public ServicioAscensoMilitar() {
        this(DIAS_MINIMOS_EN_RANGO);
    }

    public ServicioAscensoMilitar(long diasMinimos) {
        this.diasMinimos = diasMinimos;
    }

    /** Asciende al soldado al siguiente rango si cumple la política. Devuelve el nuevo rango. */
    public Rango ascender(Soldado soldado, LocalDate fecha) {
        Specification<Soldado> elegible = new SoldadoElegibleParaAscensoSpecification(fecha, diasMinimos);
        if (!elegible.esSatisfechaPor(soldado)) {
            throw new AscensoInvalidoException(
                    "El soldado no es elegible: lleva " + soldado.diasEnRangoActual(fecha)
                            + " días en " + soldado.rango() + " y se requieren " + diasMinimos
                            + " (o ya está en el rango máximo)");
        }
        Rango nuevo = soldado.rango().siguiente().orElseThrow();
        soldado.ascenderA(nuevo, fecha);
        return nuevo;
    }
}
