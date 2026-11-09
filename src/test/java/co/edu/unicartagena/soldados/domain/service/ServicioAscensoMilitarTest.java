package co.edu.unicartagena.soldados.domain.service;

import co.edu.unicartagena.soldados.domain.exception.AscensoInvalidoException;
import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.NombreCompleto;
import co.edu.unicartagena.soldados.domain.model.Rango;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServicioAscensoMilitarTest {

    private static final LocalDate INGRESO = LocalDate.of(2023, 3, 1);
    private final ServicioAscensoMilitar servicio = new ServicioAscensoMilitar();

    private Soldado nuevoSoldado() {
        return Soldado.registrar(SoldadoId.generar(), new NombreCompleto("Pedro", "Salas"),
                ComponenteMilitar.EJERCITO, INGRESO);
    }

    @Test
    void ascendeCuandoCumpleElTiempoMinimo() {
        Soldado s = nuevoSoldado();
        Rango nuevo = servicio.ascender(s, INGRESO.plusDays(ServicioAscensoMilitar.DIAS_MINIMOS_EN_RANGO));
        assertThat(nuevo).isEqualTo(Rango.SOLDADO_PROFESIONAL);
        assertThat(s.rango()).isEqualTo(Rango.SOLDADO_PROFESIONAL);
    }

    @Test
    void rechazaCuandoNoCumpleElTiempoMinimo() {
        Soldado s = nuevoSoldado();
        assertThatThrownBy(() -> servicio.ascender(s, INGRESO.plusDays(100)))
                .isInstanceOf(AscensoInvalidoException.class)
                .hasMessageContaining("no es elegible");
        assertThat(s.rango()).isEqualTo(Rango.SOLDADO_REGULAR);
    }

    @Test
    void rechazaCuandoYaEstaEnElRangoMaximo() {
        Soldado maximo = Soldado.reconstituir(SoldadoId.generar(), new NombreCompleto("Luis", "Mora"),
                ComponenteMilitar.ARMADA, INGRESO, Rango.SARGENTO_PRIMERO, INGRESO, null);
        assertThatThrownBy(() -> servicio.ascender(maximo, INGRESO.plusYears(10)))
                .isInstanceOf(AscensoInvalidoException.class);
    }

    @Test
    void permiteConfigurarOtroTiempoMinimo() {
        ServicioAscensoMilitar rapido = new ServicioAscensoMilitar(30);
        Soldado s = nuevoSoldado();
        assertThat(rapido.ascender(s, INGRESO.plusDays(30))).isEqualTo(Rango.SOLDADO_PROFESIONAL);
    }

    @Test
    void sePuedeAscenderVariasVecesSeguidasRespetandoElTiempoEnCadaRango() {
        Soldado s = nuevoSoldado();
        LocalDate fecha = INGRESO;
        for (int i = 0; i < 3; i++) {
            fecha = fecha.plusDays(400);
            servicio.ascender(s, fecha);
        }
        assertThat(s.rango()).isEqualTo(Rango.CABO_SEGUNDO);
        assertThat(s.historial()).hasSize(3);
    }
}
