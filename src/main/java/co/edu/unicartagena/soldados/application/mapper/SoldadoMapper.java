package co.edu.unicartagena.soldados.application.mapper;

import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.domain.model.Soldado;

/** Convierte el agregado del dominio en el DTO que sale de la capa de aplicación. */
public class SoldadoMapper {

    public SoldadoView aVista(Soldado s) {
        return new SoldadoView(
                s.id().valor(),
                s.nombre().completo(),
                s.componente().name(),
                s.rango().name(),
                s.fechaIngreso(),
                s.fechaUltimoAscenso());
    }
}
