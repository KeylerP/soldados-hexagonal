package co.edu.unicartagena.soldados.domain.port.out;

import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;

import java.util.List;
import java.util.Optional;

/** Puerto de SALIDA (OUT): lo que el dominio necesita para guardar y recuperar soldados. */
public interface SoldadoRepositoryPort {

    Soldado guardar(Soldado soldado);

    Optional<Soldado> buscarPorId(SoldadoId id);

    List<Soldado> listarTodos();
}
