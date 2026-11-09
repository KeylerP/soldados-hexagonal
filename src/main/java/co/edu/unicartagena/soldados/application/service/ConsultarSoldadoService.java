package co.edu.unicartagena.soldados.application.service;

import co.edu.unicartagena.soldados.application.dto.ConsultarSoldadoQuery;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.application.mapper.SoldadoMapper;
import co.edu.unicartagena.soldados.application.port.in.ConsultarSoldadoUseCase;
import co.edu.unicartagena.soldados.domain.exception.SoldadoNoEncontradoException;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;

import java.util.List;

public class ConsultarSoldadoService implements ConsultarSoldadoUseCase {

    private final SoldadoRepositoryPort repositorio;
    private final SoldadoMapper mapper;

    public ConsultarSoldadoService(SoldadoRepositoryPort repositorio, SoldadoMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @Override
    public SoldadoView consultar(ConsultarSoldadoQuery q) {
        return repositorio.buscarPorId(SoldadoId.de(q.soldadoId()))
                .map(mapper::aVista)
                .orElseThrow(() -> new SoldadoNoEncontradoException(q.soldadoId()));
    }

    @Override
    public List<SoldadoView> listar() {
        return repositorio.listarTodos().stream().map(mapper::aVista).toList();
    }
}
