package co.edu.unicartagena.soldados.infrastructure;

import co.edu.unicartagena.soldados.application.dto.AscenderSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.ConsultarSoldadoQuery;
import co.edu.unicartagena.soldados.application.dto.RegistrarSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.service.AscenderSoldadoService;
import co.edu.unicartagena.soldados.application.service.ConsultarSoldadoService;
import co.edu.unicartagena.soldados.application.service.RegistrarSoldadoService;
import co.edu.unicartagena.soldados.domain.exception.SoldadoNoEncontradoException;
import co.edu.unicartagena.soldados.domain.factory.SoldadoFactory;
import co.edu.unicartagena.soldados.domain.service.ServicioAscensoMilitar;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.event.ConsoleEventPublisherAdapter;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence.InMemorySoldadoRepositoryAdapter;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence.SoldadoPersistenceMapper;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.CliMapper;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.SoldadoCli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Prueba de integración: casos de uso reales + adaptadores en memoria, sin mocks. */
class IntegracionHexagonalTest {

    private ByteArrayOutputStream bufer;
    private RegistrarSoldadoService registrar;
    private AscenderSoldadoService ascender;
    private ConsultarSoldadoService consultar;

    @BeforeEach
    void preparar() {
        bufer = new ByteArrayOutputStream();
        var repo = new InMemorySoldadoRepositoryAdapter(new SoldadoPersistenceMapper());
        var publicador = new ConsoleEventPublisherAdapter(new PrintStream(bufer, true, StandardCharsets.UTF_8));
        var mapper = new SoldadoMapper();
        registrar = new RegistrarSoldadoService(new SoldadoFactory(Clock.systemDefaultZone()), repo, publicador, mapper);
        ascender = new AscenderSoldadoService(repo, new ServicioAscensoMilitar(), publicador, mapper);
        consultar = new ConsultarSoldadoService(repo, mapper);
    }

    @Test
    void registrarAscenderYConsultarPersisteElCambio() {
        SoldadoView creado = registrar.registrar(
                new RegistrarSoldadoCommand("Juan", "Pérez", "EJERCITO", LocalDate.now().minusYears(2)));

        ascender.ascender(new AscenderSoldadoCommand(creado.id(), LocalDate.now()));

        SoldadoView leido = consultar.consultar(new ConsultarSoldadoQuery(creado.id()));
        assertThat(leido.rango()).isEqualTo("SOLDADO_PROFESIONAL");
        assertThat(consultar.listar()).hasSize(1);
        assertThat(bufer.toString(StandardCharsets.UTF_8))
                .contains("SoldadoRegistradoEvent").contains("SoldadoAscendidoEvent");
    }

    @Test
    void consultarUnIdInexistenteFalla() {
        String id = java.util.UUID.randomUUID().toString();
        assertThatThrownBy(() -> consultar.consultar(new ConsultarSoldadoQuery(id)))
                .isInstanceOf(SoldadoNoEncontradoException.class);
    }

    @Test
    void laConsolaRegistraYListaUnSoldado() {
        String entrada = String.join("\n",
                "1", "Ana", "Ruiz", "ARMADA", "2025-01-15",
                "4",
                "3", "no-es-un-uuid",
                "9",
                "0") + "\n";
        ByteArrayOutputStream salidaCli = new ByteArrayOutputStream();
        var cli = new SoldadoCli(registrar, ascender, consultar, new CliMapper(),
                new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(salidaCli, true, StandardCharsets.UTF_8));

        cli.iniciar();

        String texto = salidaCli.toString(StandardCharsets.UTF_8);
        assertThat(texto).contains("Registrado:").contains("Ana Ruiz").contains("SOLDADO_REGULAR");
        assertThat(texto).contains("No se pudo completar").contains("Opción no válida").contains("Hasta luego.");
    }

    @Test
    void laConsolaRechazaUnaFechaMalEscrita() {
        String entrada = "1\nAna\nRuiz\nARMADA\n15/01/2025\n0\n";
        ByteArrayOutputStream salidaCli = new ByteArrayOutputStream();
        new SoldadoCli(registrar, ascender, consultar, new CliMapper(),
                new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(salidaCli, true, StandardCharsets.UTF_8)).iniciar();
        assertThat(salidaCli.toString(StandardCharsets.UTF_8)).contains("AAAA-MM-DD");
    }
}
