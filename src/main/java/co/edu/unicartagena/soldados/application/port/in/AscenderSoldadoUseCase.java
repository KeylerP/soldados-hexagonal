package co.edu.unicartagena.soldados.application.port.in;

import co.edu.unicartagena.soldados.application.dto.AscenderSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;

/** Puerto de ENTRADA (IN): caso de uso "ascender un soldado al siguiente rango". */
public interface AscenderSoldadoUseCase {

    SoldadoView ascender(AscenderSoldadoCommand comando);
}
