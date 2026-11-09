package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RangoTest {

    @ParameterizedTest
    @CsvSource({"SOLDADO_REGULAR,SOLDADO_PROFESIONAL", "SOLDADO_PROFESIONAL,CABO_TERCERO",
            "CABO_PRIMERO,SARGENTO_SEGUNDO", "SARGENTO_SEGUNDO,SARGENTO_PRIMERO"})
    void siguienteDevuelveElRangoInmediatamenteSuperior(Rango actual, Rango esperado) {
        assertThat(actual.siguiente()).contains(esperado);
    }

    @Test
    void elRangoMaximoNoTieneSiguiente() {
        assertThat(Rango.SARGENTO_PRIMERO.siguiente()).isEmpty();
    }

    @Test
    void esSuperiorAComparaPorNivel() {
        assertThat(Rango.CABO_PRIMERO.esSuperiorA(Rango.CABO_TERCERO)).isTrue();
        assertThat(Rango.CABO_TERCERO.esSuperiorA(Rango.CABO_PRIMERO)).isFalse();
        assertThat(Rango.CABO_TERCERO.nivel()).isEqualTo(3);
    }

    @Test
    void componenteSeInterpretaDesdeTextoConEspaciosYMinusculas() {
        assertThat(ComponenteMilitar.desde(" fuerza aerea ")).isEqualTo(ComponenteMilitar.FUERZA_AEREA);
        assertThat(ComponenteMilitar.desde("armada")).isEqualTo(ComponenteMilitar.ARMADA);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void componenteVacioSeRechaza(String texto) {
        assertThatThrownBy(() -> ComponenteMilitar.desde(texto))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @Test
    void componenteDesconocidoSeRechaza() {
        assertThatThrownBy(() -> ComponenteMilitar.desde("marines"))
                .isInstanceOf(DatosSoldadoInvalidosException.class)
                .hasMessageContaining("desconocido");
    }
}
