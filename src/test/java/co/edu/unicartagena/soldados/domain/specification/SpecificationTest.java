package co.edu.unicartagena.soldados.domain.specification;

import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.NombreCompleto;
import co.edu.unicartagena.soldados.domain.model.Rango;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationTest {

    private static final LocalDate INGRESO = LocalDate.of(2024, 1, 1);

    private Soldado soldado(Rango rango) {
        return Soldado.reconstituir(SoldadoId.generar(), new NombreCompleto("Mario", "Díaz"),
                ComponenteMilitar.EJERCITO, INGRESO, rango, INGRESO, null);
    }

    @Test
    void esElegibleSiTieneTiempoYNoEsElMaximo() {
        var spec = new SoldadoElegibleParaAscensoSpecification(INGRESO.plusDays(365), 365);
        assertThat(spec.esSatisfechaPor(soldado(Rango.CABO_TERCERO))).isTrue();
    }

    @Test
    void noEsElegibleSiLeFaltaTiempo() {
        var spec = new SoldadoElegibleParaAscensoSpecification(INGRESO.plusDays(364), 365);
        assertThat(spec.esSatisfechaPor(soldado(Rango.CABO_TERCERO))).isFalse();
    }

    @Test
    void noEsElegibleSiEsElRangoMaximo() {
        var spec = new SoldadoElegibleParaAscensoSpecification(INGRESO.plusYears(20), 365);
        assertThat(spec.esSatisfechaPor(soldado(Rango.SARGENTO_PRIMERO))).isFalse();
    }

    @Test
    void sePuedenCombinarConYOYNo() {
        Specification<Soldado> siempre = s -> true;
        Specification<Soldado> nunca = s -> false;
        Soldado s = soldado(Rango.CABO_TERCERO);
        assertThat(siempre.y(nunca).esSatisfechaPor(s)).isFalse();
        assertThat(siempre.o(nunca).esSatisfechaPor(s)).isTrue();
        assertThat(nunca.no().esSatisfechaPor(s)).isTrue();
    }
}
