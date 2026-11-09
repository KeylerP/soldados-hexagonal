package co.edu.unicartagena.soldados.domain.factory;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.NombreCompleto;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Patrón Factory: concentra la creación de un soldado nuevo (id generado,
 * VOs validados, ingreso no futuro) para que nadie lo arme a medias.
 */
public class SoldadoFactory {

    private final Clock reloj;

    public SoldadoFactory(Clock reloj) {
        this.reloj = reloj;
    }

    public Soldado crear(String nombres, String apellidos, String componente, LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new DatosSoldadoInvalidosException("La fecha de ingreso es obligatoria");
        }
        if (fechaIngreso.isAfter(LocalDate.now(reloj))) {
            throw new DatosSoldadoInvalidosException("La fecha de ingreso no puede ser futura");
        }
        return Soldado.registrar(
                SoldadoId.generar(),
                new NombreCompleto(nombres, apellidos),
                ComponenteMilitar.desde(componente),
                fechaIngreso);
    }
}
