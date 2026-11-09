package co.edu.unicartagena.soldados.domain.model;

import co.edu.unicartagena.soldados.domain.event.DomainEvent;
import co.edu.unicartagena.soldados.domain.event.SoldadoAscendidoEvent;
import co.edu.unicartagena.soldados.domain.event.SoldadoRegistradoEvent;
import co.edu.unicartagena.soldados.domain.exception.AscensoInvalidoException;
import co.edu.unicartagena.soldados.domain.exception.DatosSoldadoInvalidosException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad y RAÍZ DE AGREGADO. Su identidad es el SoldadoId.
 *
 * Invariantes que protege:
 *  - siempre tiene id, nombre, componente, rango y fecha de ingreso;
 *  - el rango solo cambia por ascenderA(), nunca con un setter;
 *  - solo se asciende al rango inmediatamente superior;
 *  - la fecha de un ascenso no puede ser anterior al ingreso ni al último ascenso;
 *  - cada ascenso queda en el historial y genera un evento de dominio.
 */
public class Soldado {

    private final SoldadoId id;
    private final NombreCompleto nombre;
    private final ComponenteMilitar componente;
    private final LocalDate fechaIngreso;
    private Rango rango;
    private LocalDate fechaUltimoAscenso;
    private final List<RegistroDeAscenso> historial = new ArrayList<>();
    private final List<DomainEvent> eventos = new ArrayList<>();

    private Soldado(SoldadoId id, NombreCompleto nombre, ComponenteMilitar componente,
                    LocalDate fechaIngreso, Rango rango, LocalDate fechaUltimoAscenso) {
        this.id = Objects.requireNonNull(id, "id");
        this.nombre = Objects.requireNonNull(nombre, "nombre");
        this.componente = Objects.requireNonNull(componente, "componente");
        if (fechaIngreso == null) {
            throw new DatosSoldadoInvalidosException("La fecha de ingreso es obligatoria");
        }
        this.fechaIngreso = fechaIngreso;
        this.rango = Objects.requireNonNull(rango, "rango");
        this.fechaUltimoAscenso = fechaUltimoAscenso == null ? fechaIngreso : fechaUltimoAscenso;
    }

    /** Alta de un soldado nuevo: arranca en SOLDADO_REGULAR y emite SoldadoRegistradoEvent. */
    public static Soldado registrar(SoldadoId id, NombreCompleto nombre, ComponenteMilitar componente,
                                    LocalDate fechaIngreso) {
        Soldado s = new Soldado(id, nombre, componente, fechaIngreso, Rango.SOLDADO_REGULAR, fechaIngreso);
        s.eventos.add(new SoldadoRegistradoEvent(id.valor(), nombre.completo(), fechaIngreso));
        return s;
    }

    /** Reconstruye un soldado ya existente (por ejemplo, leído de la base). No emite eventos. */
    public static Soldado reconstituir(SoldadoId id, NombreCompleto nombre, ComponenteMilitar componente,
                                       LocalDate fechaIngreso, Rango rango, LocalDate fechaUltimoAscenso,
                                       List<RegistroDeAscenso> historialPrevio) {
        Soldado s = new Soldado(id, nombre, componente, fechaIngreso, rango, fechaUltimoAscenso);
        if (historialPrevio != null) {
            s.historial.addAll(historialPrevio);
        }
        return s;
    }

    /** Único camino para cambiar el rango. Aquí viven las reglas del ascenso. */
    public void ascenderA(Rango nuevoRango, LocalDate fecha) {
        if (nuevoRango == null || fecha == null) {
            throw new AscensoInvalidoException("El nuevo rango y la fecha del ascenso son obligatorios");
        }
        Rango esperado = rango.siguiente().orElseThrow(() ->
                new AscensoInvalidoException("El soldado ya está en el rango máximo: " + rango));
        if (nuevoRango != esperado) {
            throw new AscensoInvalidoException(
                    "Solo se puede ascender de " + rango + " a " + esperado + ", no a " + nuevoRango);
        }
        if (fecha.isBefore(fechaUltimoAscenso)) {
            throw new AscensoInvalidoException(
                    "La fecha del ascenso (" + fecha + ") es anterior a la del último cambio de rango ("
                            + fechaUltimoAscenso + ")");
        }
        Rango anterior = this.rango;
        this.rango = nuevoRango;
        this.fechaUltimoAscenso = fecha;
        this.historial.add(new RegistroDeAscenso(anterior, nuevoRango, fecha));
        this.eventos.add(new SoldadoAscendidoEvent(id.valor(), anterior, nuevoRango, fecha));
    }

    public long diasEnRangoActual(LocalDate fechaReferencia) {
        return ChronoUnit.DAYS.between(fechaUltimoAscenso, fechaReferencia);
    }

    /** Entrega los eventos acumulados y limpia la lista (para que se publiquen una sola vez). */
    public List<DomainEvent> extraerEventos() {
        List<DomainEvent> copia = List.copyOf(eventos);
        eventos.clear();
        return copia;
    }

    public SoldadoId id() { return id; }
    public NombreCompleto nombre() { return nombre; }
    public ComponenteMilitar componente() { return componente; }
    public Rango rango() { return rango; }
    public LocalDate fechaIngreso() { return fechaIngreso; }
    public LocalDate fechaUltimoAscenso() { return fechaUltimoAscenso; }

    public List<RegistroDeAscenso> historial() {
        return Collections.unmodifiableList(historial);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Soldado otro && id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
