package co.edu.unicartagena.soldados.application;

import co.edu.unicartagena.soldados.application.dto.RegistrarSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.service.RegistrarSoldadoService;
import co.edu.unicartagena.soldados.domain.event.SoldadoRegistradoEvent;
import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;
import co.edu.unicartagena.soldados.domain.factory.SoldadoFactory;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarSoldadoServiceTest {

    @Mock SoldadoRepositoryPort repositorio;
    @Mock EventPublisherPort publicador;

    private RegistrarSoldadoService servicio;

    @BeforeEach
    void preparar() {
        Clock reloj = Clock.fixed(Instant.parse("2026-11-06T12:00:00Z"), ZoneOffset.UTC);
        servicio = new RegistrarSoldadoService(new SoldadoFactory(reloj), repositorio, publicador, new SoldadoMapper());
    }

    @Test
    void registraGuardaYPublicaElEvento() {
        when(repositorio.guardar(any(Soldado.class))).thenAnswer(inv -> inv.getArgument(0));

        SoldadoView vista = servicio.registrar(
                new RegistrarSoldadoCommand("Juan", "Pérez", "EJERCITO", LocalDate.of(2026, 2, 1)));

        assertThat(vista.rango()).isEqualTo("SOLDADO_REGULAR");
        assertThat(vista.nombreCompleto()).isEqualTo("Juan Pérez");
        verify(repositorio).guardar(any(Soldado.class));
        verify(publicador).publicar(any(SoldadoRegistradoEvent.class));
    }

    @Test
    void siLosDatosSonInvalidosNoGuardaNiPublica() {
        assertThatThrownBy(() -> servicio.registrar(
                new RegistrarSoldadoCommand("", "Pérez", "EJERCITO", LocalDate.of(2026, 2, 1))))
                .isInstanceOf(DatosSoldadoInvalidosException.class);
        verify(repositorio, never()).guardar(any());
        verify(publicador, never()).publicar(any());
    }
}
