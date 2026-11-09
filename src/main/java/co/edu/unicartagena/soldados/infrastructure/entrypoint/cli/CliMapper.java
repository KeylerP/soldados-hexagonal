package co.edu.unicartagena.soldados.infrastructure.entrypoint.cli;

import co.edu.unicartagena.soldados.application.dto.RegistrarSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.dto.RegistrarSoldadoRequest;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.dto.SoldadoResponse;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Traduce entre los DTO de la consola (texto) y los comandos/vistas de la aplicación. */
public class CliMapper {

    public RegistrarSoldadoCommand aComando(RegistrarSoldadoRequest r) {
        try {
            return new RegistrarSoldadoCommand(r.nombres(), r.apellidos(), r.componente(),
                    LocalDate.parse(r.fechaIngreso().trim()));
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD");
        }
    }

    public SoldadoResponse aRespuesta(SoldadoView v) {
        return new SoldadoResponse(v.id(), v.nombreCompleto(), v.componente(), v.rango());
    }
}
