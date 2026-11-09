package co.edu.unicartagena.soldados.domain.specification;

import co.edu.unicartagena.soldados.domain.model.Soldado;

import java.time.LocalDate;

/**
 * Regla: un soldado es elegible para ascender si no está en el rango máximo
 * y lleva al menos X días en su rango actual.
 */
public class SoldadoElegibleParaAscensoSpecification implements Specification<Soldado> {

    private final LocalDate fechaReferencia;
    private final long diasMinimosEnRango;

    public SoldadoElegibleParaAscensoSpecification(LocalDate fechaReferencia, long diasMinimosEnRango) {
        this.fechaReferencia = fechaReferencia;
        this.diasMinimosEnRango = diasMinimosEnRango;
    }

    @Override
    public boolean esSatisfechaPor(Soldado soldado) {
        return soldado.rango().siguiente().isPresent()
                && soldado.diasEnRangoActual(fechaReferencia) >= diasMinimosEnRango;
    }
}
