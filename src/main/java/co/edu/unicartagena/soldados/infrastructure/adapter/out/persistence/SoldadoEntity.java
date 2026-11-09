package co.edu.unicartagena.soldados.infrastructure.adapter.out.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de persistencia (tipo "entidad de tabla"): todo son tipos simples.
 * Es lo que en un proyecto real llevaría las anotaciones de JPA; el dominio ni se entera.
 */
public class SoldadoEntity {

    public String id;
    public String nombres;
    public String apellidos;
    public String componente;
    public String rango;
    public LocalDate fechaIngreso;
    public LocalDate fechaUltimoAscenso;
    public List<AscensoEntity> ascensos = new ArrayList<>();

    public static class AscensoEntity {
        public String rangoAnterior;
        public String rangoNuevo;
        public LocalDate fecha;
    }
}
