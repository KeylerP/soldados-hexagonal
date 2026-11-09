package co.edu.unicartagena.soldados.application;

import co.edu.unicartagena.soldados.application.dto.AscenderSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.service.AscenderSoldadoService;
import co.edu.unicartagena.soldados.domain.event.SoldadoAscendidoEvent;
import co.edu.unicartagena.soldados.domain.exception.AscensoInvalidoException;
import co.edu.unicartagena.soldados.domain.exception.SoldadoNoEncontradoException;
import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.NombreCompleto;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;
import co.edu.unicartagena.soldados.domain.service.ServicioAscensoMilitar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AscenderSoldadoServiceTest {

    private static final LocalDate INGRESO = LocalDate.of(2024, 1, 1);

    @Mock SoldadoRepositoryPort repositorio;
    @Mock EventPublisherPort publicador;

    private AscenderSoldadoService servicio;
    private Soldado soldado;

    @BeforeEach
    void preparar() {
        servicio = new AscenderSoldadoService(repositorio, new ServicioAscensoMilitar(), publicador, new SoldadoMapper());
        soldado = Soldado.registrar(SoldadoId.generar(), new NombreCompleto("Juan", "Pérez"),
                ComponenteMilitar.EJERCITO, INGRESO);
        soldado.extraerEventos();
    }

    @Test
    void ascendeGuardaYPublicaElEvento() {
        when(repositorio.buscarPorId(soldado.id())).thenReturn(Optional.of(soldado));
        when(repositorio.guardar(soldado)).thenReturn(soldado);

        SoldadoView vista = servicio.ascender(new AscenderSoldadoCommand(soldado.id().valor(), INGRESO.plusDays(400)));

        assertThat(vista.rango()).isEqualTo("SOLDADO_PROFESIONAL");
        verify(repositorio).guardar(soldado);
        verify(publicador).publicar(any(SoldadoAscendidoEvent.class));
    }

    @Test
    void siNoExisteLanzaSoldadoNoEncontrado() {
        when(repositorio.buscarPorId(any())).thenReturn(Optional.empty());
        String id = SoldadoId.generar().valor();

        assertThatThrownBy(() -> servicio.ascender(new AscenderSoldadoCommand(id, INGRESO)))
                .isInstanceOf(SoldadoNoEncontradoException.class);
        verify(repositorio, never()).guardar(any());
    }

    @Test
    void siNoEsElegibleNoGuardaNiPublica() {
        when(repositorio.buscarPorId(soldado.id())).thenReturn(Optional.of(soldado));

        assertThatThrownBy(() -> servicio.ascender(new AscenderSoldadoCommand(soldado.id().valor(), INGRESO.plusDays(10))))
                .isInstanceOf(AscensoInvalidoException.class);
        verify(repositorio, never()).guardar(any());
        verify(publicador, never()).publicar(any());
    }
}
