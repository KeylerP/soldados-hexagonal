package co.edu.unicartagena.soldados.application.service;

import co.edu.unicartagena.soldados.application.dto.AscenderSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.port.in.AscenderSoldadoUseCase;
import co.edu.unicartagena.soldados.domain.exception.SoldadoNoEncontradoException;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;
import co.edu.unicartagena.soldados.domain.service.ServicioAscensoMilitar;

/** Servicio de caso de uso: busca, delega la regla al dominio, guarda y publica eventos. */
public class AscenderSoldadoService implements AscenderSoldadoUseCase {

    private final SoldadoRepositoryPort repositorio;
    private final ServicioAscensoMilitar servicioAscenso;
    private final EventPublisherPort publicador;
    private final SoldadoMapper mapper;

    public AscenderSoldadoService(SoldadoRepositoryPort repositorio, ServicioAscensoMilitar servicioAscenso,
                                  EventPublisherPort publicador, SoldadoMapper mapper) {
        this.repositorio = repositorio;
        this.servicioAscenso = servicioAscenso;
        this.publicador = publicador;
        this.mapper = mapper;
    }

    @Override
    public SoldadoView ascender(AscenderSoldadoCommand c) {
        SoldadoId id = SoldadoId.de(c.soldadoId());
        Soldado soldado = repositorio.buscarPorId(id)
                .orElseThrow(() -> new SoldadoNoEncontradoException(c.soldadoId()));
        servicioAscenso.ascender(soldado, c.fecha());
        Soldado guardado = repositorio.guardar(soldado);
        soldado.extraerEventos().forEach(publicador::publicar);
        return mapper.aVista(guardado);
    }
}
