package co.edu.unicartagena.soldados;

import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.service.AscenderSoldadoService;
import co.edu.unicartagena.soldados.application.service.ConsultarSoldadoService;
import co.edu.unicartagena.soldados.application.service.RegistrarSoldadoService;
import co.edu.unicartagena.soldados.domain.factory.SoldadoFactory;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;
import co.edu.unicartagena.soldados.domain.service.ServicioAscensoMilitar;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.event.ConsoleEventPublisherAdapter;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence.InMemorySoldadoRepositoryAdapter;
import co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence.SoldadoPersistenceMapper;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.CliMapper;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.SoldadoCli;

import java.time.Clock;

/**
 * Raíz de composición: el único lugar que conoce todas las clases concretas
 * y las conecta (inyección de dependencias manual, por constructor).
 */
public class Main {

    public static void main(String[] args) {
        SoldadoRepositoryPort repositorio = new InMemorySoldadoRepositoryAdapter(new SoldadoPersistenceMapper());
        EventPublisherPort publicador = new ConsoleEventPublisherAdapter(System.out);
        SoldadoMapper mapper = new SoldadoMapper();

        var registrar = new RegistrarSoldadoService(new SoldadoFactory(Clock.systemDefaultZone()),
                repositorio, publicador, mapper);
        var ascender = new AscenderSoldadoService(repositorio, new ServicioAscensoMilitar(), publicador, mapper);
        var consultar = new ConsultarSoldadoService(repositorio, mapper);

        new SoldadoCli(registrar, ascender, consultar, new CliMapper(), System.in, System.out).iniciar();
    }
}
