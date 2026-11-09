package co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence;

import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;
import co.edu.unicartagena.soldados.domain.port.out.SoldadoRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de SALIDA: implementa el puerto de repositorio guardando en memoria.
 * Para pasar a JDBC/JPA solo se cambia esta clase; el dominio y los casos de uso quedan igual.
 */
public class InMemorySoldadoRepositoryAdapter implements SoldadoRepositoryPort {

    private final Map<String, SoldadoEntity> tabla = new ConcurrentHashMap<>();
    private final SoldadoPersistenceMapper mapper;

    public InMemorySoldadoRepositoryAdapter(SoldadoPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Soldado guardar(Soldado soldado) {
        tabla.put(soldado.id().valor(), mapper.aEntidad(soldado));
        return soldado;
    }

    @Override
    public Optional<Soldado> buscarPorId(SoldadoId id) {
        return Optional.ofNullable(tabla.get(id.valor())).map(mapper::aDominio);
    }

    @Override
    public List<Soldado> listarTodos() {
        return tabla.values().stream().map(mapper::aDominio).toList();
    }
}
