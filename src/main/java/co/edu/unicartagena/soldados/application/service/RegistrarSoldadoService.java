package co.edu.unicartagena.soldados.application.service;

import co.edu.unicartagena.soldados.application.dto.RegistrarSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.port.in.RegistrarSoldadoUseCase;
import co.edu.unicartagena.soldados.domain.factory.SoldadoFactory;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;

/** Servicio de caso de uso: orquesta factory + repositorio + publicación de eventos. */
public class RegistrarSoldadoService implements RegistrarSoldadoUseCase {

    private final SoldadoFactory factory;
    private final SoldadoRepositoryPort repositorio;
    private final EventPublisherPort publicador;
    private final SoldadoMapper mapper;

    public RegistrarSoldadoService(SoldadoFactory factory, SoldadoRepositoryPort repositorio,
                                   EventPublisherPort publicador, SoldadoMapper mapper) {
        this.factory = factory;
        this.repositorio = repositorio;
        this.publicador = publicador;
        this.mapper = mapper;
    }

    @Override
    public SoldadoView registrar(RegistrarSoldadoCommand c) {
        Soldado soldado = factory.crear(c.nombres(), c.apellidos(), c.componente(), c.fechaIngreso());
        Soldado guardado = repositorio.guardar(soldado);
        soldado.extraerEventos().forEach(publicador::publicar);
        return mapper.aVista(guardado);
    }
}
