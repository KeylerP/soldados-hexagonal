package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.event.DomainEvent;
import co.edu.unicartagena.soldados.domain.event.SoldadoAscendidoEvent;
import co.edu.unicartagena.soldados.domain.event.SoldadoRegistradoEvent;
import co.edu.unicartagena.soldados.domain.exception.AscensoInvalidoException;
import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoldadoTest {

    private static final LocalDate INGRESO = LocalDate.of(2024, 1, 10);
    private Soldado soldado;

    @BeforeEach
    void preparar() {
        soldado = Soldado.registrar(SoldadoId.generar(), new NombreCompleto("Juan", "Pérez"),
                ComponenteMilitar.EJERCITO, INGRESO);
    }

    @Test
    void alRegistrarseEmpiezaComoSoldadoRegularYEmiteEvento() {
        assertThat(soldado.rango()).isEqualTo(Rango.SOLDADO_REGULAR);
        assertThat(soldado.fechaUltimoAscenso()).isEqualTo(INGRESO);
        List<DomainEvent> eventos = soldado.extraerEventos();
        assertThat(eventos).hasSize(1).first().isInstanceOf(SoldadoRegistradoEvent.class);
    }

    @Test
    void extraerEventosLimpiaLaLista() {
        soldado.extraerEventos();
        assertThat(soldado.extraerEventos()).isEmpty();
    }

    @Test
    void ascenderAlRangoInmediatamenteSuperiorFunciona() {
        soldado.extraerEventos();
        LocalDate fecha = INGRESO.plusYears(1);

        soldado.ascenderA(Rango.SOLDADO_PROFESIONAL, fecha);

        assertThat(soldado.rango()).isEqualTo(Rango.SOLDADO_PROFESIONAL);
        assertThat(soldado.fechaUltimoAscenso()).isEqualTo(fecha);
        assertThat(soldado.historial()).containsExactly(
                new RegistroDeAscenso(Rango.SOLDADO_REGULAR, Rango.SOLDADO_PROFESIONAL, fecha));
        assertThat(soldado.extraerEventos()).singleElement().isInstanceOf(SoldadoAscendidoEvent.class);
    }

    @Test
    void noPermiteSaltarseRangos() {
        assertThatThrownBy(() -> soldado.ascenderA(Rango.CABO_SEGUNDO, INGRESO.plusYears(1)))
                .isInstanceOf(AscensoInvalidoException.class)
                .hasMessageContaining("SOLDADO_PROFESIONAL");
        assertThat(soldado.rango()).isEqualTo(Rango.SOLDADO_REGULAR);
        assertThat(soldado.historial()).isEmpty();
    }

    @Test
    void noPermiteDescender() {
        soldado.ascenderA(Rango.SOLDADO_PROFESIONAL, INGRESO.plusYears(1));
        assertThatThrownBy(() -> soldado.ascenderA(Rango.SOLDADO_REGULAR, INGRESO.plusYears(2)))
                .isInstanceOf(AscensoInvalidoException.class);
    }

    @Test
    void noPermiteFechaAnteriorAlUltimoCambioDeRango() {
        assertThatThrownBy(() -> soldado.ascenderA(Rango.SOLDADO_PROFESIONAL, INGRESO.minusDays(1)))
                .isInstanceOf(AscensoInvalidoException.class)
                .hasMessageContaining("anterior");
    }

    @Test
    void noPermiteArgumentosNulos() {
        assertThatThrownBy(() -> soldado.ascenderA(null, INGRESO.plusYears(1)))
                .isInstanceOf(AscensoInvalidoException.class);
        assertThatThrownBy(() -> soldado.ascenderA(Rango.SOLDADO_PROFESIONAL, null))
                .isInstanceOf(AscensoInvalidoException.class);
    }

    @Test
    void noPermiteAscenderDesdeElRangoMaximo() {
        Soldado maximo = Soldado.reconstituir(SoldadoId.generar(), new NombreCompleto("Luis", "Mora"),
                ComponenteMilitar.ARMADA, INGRESO, Rango.SARGENTO_PRIMERO, INGRESO, null);
        assertThatThrownBy(() -> maximo.ascenderA(Rango.SARGENTO_PRIMERO, INGRESO.plusYears(5)))
                .isInstanceOf(AscensoInvalidoException.class)
                .hasMessageContaining("máximo");
    }

    @Test
    void elHistorialNoSePuedeModificarDesdeAfuera() {
        assertThatThrownBy(() -> soldado.historial().add(
                new RegistroDeAscenso(Rango.SOLDADO_REGULAR, Rango.SOLDADO_PROFESIONAL, INGRESO)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void fechaDeIngresoEsObligatoria() {
        assertThatThrownBy(() -> Soldado.registrar(SoldadoId.generar(), new NombreCompleto("Juan", "Pérez"),
                ComponenteMilitar.EJERCITO, null))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
    }

    @Test
    void diasEnRangoActualCuentaDesdeElUltimoAscenso() {
        assertThat(soldado.diasEnRangoActual(INGRESO.plusDays(30))).isEqualTo(30);
    }

    @Test
    void reconstituirNoEmiteEventosYConservaElHistorial() {
        var previo = List.of(new RegistroDeAscenso(Rango.SOLDADO_REGULAR, Rango.SOLDADO_PROFESIONAL, INGRESO.plusYears(1)));
        Soldado s = Soldado.reconstituir(SoldadoId.generar(), new NombreCompleto("Ana", "Ruiz"),
                ComponenteMilitar.FUERZA_AEREA, INGRESO, Rango.SOLDADO_PROFESIONAL, INGRESO.plusYears(1), previo);
        assertThat(s.extraerEventos()).isEmpty();
        assertThat(s.historial()).hasSize(1);
    }

    @Test
    void laIgualdadDependeSoloDelId() {
        SoldadoId id = SoldadoId.generar();
        Soldado a = Soldado.registrar(id, new NombreCompleto("Ana", "Ruiz"), ComponenteMilitar.ARMADA, INGRESO);
        Soldado b = Soldado.registrar(id, new NombreCompleto("Otra", "Persona"), ComponenteMilitar.EJERCITO, INGRESO);
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(soldado);
    }
}
