package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoldadoIdTest {

    @Test
    void generarCreaUnUuidValidoYDistintoCadaVez() {
        SoldadoId a = SoldadoId.generar();
        SoldadoId b = SoldadoId.generar();
        assertThat(a).isNotEqualTo(b);
        assertThat(SoldadoId.de(a.valor())).isEqualTo(a);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "abc", "123"})
    void rechazaValoresInvalidos(String valor) {
        assertThatThrownBy(() -> SoldadoId.de(valor)).isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @Test
    void toStringDevuelveElValor() {
        SoldadoId id = SoldadoId.generar();
        assertThat(id.toString()).isEqualTo(id.valor());
    }
}
