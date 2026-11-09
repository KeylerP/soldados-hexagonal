package co.edu.unicartagena.soldados.domain.factory;

import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.Rango;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoldadoFactoryTest {

    private final Clock reloj = Clock.fixed(Instant.parse("2026-11-06T12:00:00Z"), ZoneOffset.UTC);
    private final SoldadoFactory factory = new SoldadoFactory(reloj);

    @Test
    void creaUnSoldadoValidoConRangoInicial() {
        Soldado s = factory.crear("Carlos", "Mendoza", "armada", LocalDate.of(2026, 1, 5));
        assertThat(s.rango()).isEqualTo(Rango.SOLDADO_REGULAR);
        assertThat(s.componente()).isEqualTo(ComponenteMilitar.ARMADA);
        assertThat(s.id()).isNotNull();
    }

    @Test
    void rechazaFechaFutura() {
        assertThatThrownBy(() -> factory.crear("Carlos", "Mendoza", "ARMADA", LocalDate.of(2026, 11, 7)))
                .isInstanceOf(DatosSoldadoInvalidosException.class)
                .hasMessageContaining("futura");
    }

    @Test
    void rechazaFechaNula() {
        assertThatThrownBy(() -> factory.crear("Carlos", "Mendoza", "ARMADA", null))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @Test
    void rechazaDatosInvalidosDeLosValueObjects() {
        assertThatThrownBy(() -> factory.crear("", "Mendoza", "ARMADA", LocalDate.of(2026, 1, 1)))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
        assertThatThrownBy(() -> factory.crear("Carlos", "Mendoza", "NAVY", LocalDate.of(2026, 1, 1)))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }
}
