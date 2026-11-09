package co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence;

import co.edu.unicartagena.soldados.domain.model.ComponenteMilitar;
import co.edu.unicartagena.soldados.domain.model.NombreCompleto;
import co.edu.unicartagena.soldados.domain.model.Rango;
import co.edu.unicartagena.soldados.domain.model.RegistroDeAscenso;
import co.edu.unicartagena.soldados.domain.model.Soldado;
import co.edu.unicartagena.soldados.domain.model.SoldadoId;

import java.util.List;

/** Capa anticorrupción de persistencia: traduce dominio <-> entidad de tabla. */
public class SoldadoPersistenceMapper {

    public SoldadoEntity aEntidad(Soldado s) {
        SoldadoEntity e = new SoldadoEntity();
        e.id = s.id().valor();
        e.nombres = s.nombre().nombres();
        e.apellidos = s.nombre().apellidos();
        e.componente = s.componente().name();
        e.rango = s.rango().name();
        e.fechaIngreso = s.fechaIngreso();
        e.fechaUltimoAscenso = s.fechaUltimoAscenso();
        for (RegistroDeAscenso r : s.historial()) {
            SoldadoEntity.AscensoEntity a = new SoldadoEntity.AscensoEntity();
            a.rangoAnterior = r.rangoAnterior().name();
            a.rangoNuevo = r.rangoNuevo().name();
            a.fecha = r.fecha();
            e.ascensos.add(a);
        }
        return e;
    }

    public Soldado aDominio(SoldadoEntity e) {
        List<RegistroDeAscenso> historial = e.ascensos.stream()
                .map(a -> new RegistroDeAscenso(Rango.valueOf(a.rangoAnterior), Rango.valueOf(a.rangoNuevo), a.fecha))
                .toList();
        return Soldado.reconstituir(
                SoldadoId.de(e.id),
                new NombreCompleto(e.nombres, e.apellidos),
                ComponenteMilitar.valueOf(e.componente),
                e.fechaIngreso,
                Rango.valueOf(e.rango),
                e.fechaUltimoAscenso,
                historial);
    }
}
