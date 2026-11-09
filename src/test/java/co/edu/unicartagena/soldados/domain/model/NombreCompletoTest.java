package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NombreCompletoTest {

    @Test
    void normalizaEspaciosYArmaElNombreCompleto() {
        NombreCompleto n = new NombreCompleto("  Juan Carlos ", " Pérez Gómez ");
        assertThat(n.nombres()).isEqualTo("Juan Carlos");
        assertThat(n.completo()).isEqualTo("Juan Carlos Pérez Gómez");
        assertThat(n).hasToString("Juan Carlos Pérez Gómez");
    }

    @Test
    void dosNombresIgualesSonIguales() {
        assertThat(new NombreCompleto("Ana", "Ruiz")).isEqualTo(new NombreCompleto(" Ana ", "Ruiz"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "A", "Juan123", "Ju@n"})
    void rechazaNombresInvalidos(String nombres) {
        assertThatThrownBy(() -> new NombreCompleto(nombres, "Pérez"))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @ParameterizedTest
    @CsvSource({"Juan,''", "Juan,P", "Juan,Pérez9"})
    void rechazaApellidosInvalidos(String nombres, String apellidos) {
        assertThatThrownBy(() -> new NombreCompleto(nombres, apellidos))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @Test
    void rechazaNombresDemasiadoLargos() {
        String largo = "a".repeat(61);
        assertThatThrownBy(() -> new NombreCompleto(largo, "Pérez"))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }
}
