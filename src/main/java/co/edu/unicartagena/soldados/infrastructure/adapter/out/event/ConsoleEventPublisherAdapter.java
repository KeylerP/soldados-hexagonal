package co.edu.unicartagena.soldados.infrastructure.adapter.out.event;

import co.edu.unicartagena.soldados.domain.event.DomainEvent;
import co.edu.unicartagena.soldados.domain.port.out.EventPublisherPort;

import java.io.PrintStream;

/** Adaptador de SALIDA: "publica" los eventos imprimiéndolos. Mañana podría ser una cola o un correo. */
public class ConsoleEventPublisherAdapter implements EventPublisherPort {

    private final PrintStream salida;

    public ConsoleEventPublisherAdapter(PrintStream salida) {
        this.salida = salida;
    }

    @Override
    public void publicar(DomainEvent evento) {
        salida.println("[EVENTO] " + evento.getClass().getSimpleName() + " -> " + evento);
    }
}
