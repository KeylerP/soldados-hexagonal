package co.edu.unicartagena.soldados.application.port.in;

import co.edu.unicartagena.soldados.application.dto.RegistrarSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;

/** Puerto de ENTRADA (IN): caso de uso "registrar un soldado". */
public interface RegistrarSoldadoUseCase {

    SoldadoView registrar(RegistrarSoldadoCommand comando);
}
