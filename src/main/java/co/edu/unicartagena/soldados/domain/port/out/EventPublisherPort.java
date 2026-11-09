package co.edu.unicartagena.soldados.domain.port.out;

import co.edu.unicartagena.soldados.domain.event.DomainEvent;

/** Puerto de SALIDA (OUT): cómo el dominio avisa al mundo que algo ocurrió (consola, cola, correo...). */
public interface EventPublisherPort {

    void publicar(DomainEvent evento);
}
