package co.edu.unicartagena.soldados.application.port.in;

import co.edu.unicartagena.soldados.application.dto.ConsultarSoldadoQuery;
import co.edu.unicartagena.soldados.application.dto.SoldadoView;

import java.util.List;

/** Puerto de ENTRADA (IN): consultas de solo lectura. */
public interface ConsultarSoldadoUseCase {

    SoldadoView consultar(ConsultarSoldadoQuery consulta);

    List<SoldadoView> listar();
}
