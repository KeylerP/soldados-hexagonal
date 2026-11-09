package co.edu.unicartagena.soldados.infrastructure.entrypoint.cli;

import co.edu.unicartagena.soldados.application.dto.AscenderSoldadoCommand;
import co.edu.unicartagena.soldados.application.dto.ConsultarSoldadoQuery;
import co.edu.unicartagena.soldados.application.port.in.AscenderSoldadoUseCase;
import co.edu.unicartagena.soldados.application.port.in.ConsultarSoldadoUseCase;
import co.edu.unicartagena.soldados.application.port.in.RegistrarSoldadoUseCase;
import co.edu.unicartagena.soldados.domain.exception.DominioException;
import co.edu.unicartagena.soldados.infrastructure.entrypoint.cli.dto.RegistrarSoldadoRequest;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;

/** Entrypoint / controlador de consola. Solo habla con los puertos de entrada (use cases). */
public class SoldadoCli {

    private final RegistrarSoldadoUseCase registrar;
    private final AscenderSoldadoUseCase ascender;
    private final ConsultarSoldadoUseCase consultar;
    private final CliMapper mapper;
    private final Scanner entrada;
    private final PrintStream salida;

    public SoldadoCli(RegistrarSoldadoUseCase registrar, AscenderSoldadoUseCase ascender,
                      ConsultarSoldadoUseCase consultar, CliMapper mapper,
                      InputStream in, PrintStream out) {
        this.registrar = registrar;
        this.ascender = ascender;
        this.consultar = consultar;
        this.mapper = mapper;
        this.entrada = new Scanner(in);
        this.salida = out;
    }

    public void iniciar() {
        boolean activo = true;
        while (activo) {
            salida.println("\n=== Control de Soldados ===");
            salida.println("1) Registrar soldado  2) Ascender soldado  3) Consultar  4) Listar  0) Salir");
            salida.print("Opción: ");
            String opcion = entrada.nextLine().trim();
            try {
                switch (opcion) {
                    case "1" -> registrarSoldado();
                    case "2" -> ascenderSoldado();
                    case "3" -> consultarSoldado();
                    case "4" -> consultar.listar().forEach(v -> salida.println(mapper.aRespuesta(v)));
                    case "0" -> activo = false;
                    default -> salida.println("Opción no válida");
                }
            } catch (DominioException | IllegalArgumentException e) {
                salida.println("No se pudo completar: " + e.getMessage());
            }
        }
        salida.println("Hasta luego.");
    }

    private void registrarSoldado() {
        String nombres = pedir("Nombres: ");
        String apellidos = pedir("Apellidos: ");
        String componente = pedir("Componente (EJERCITO/ARMADA/FUERZA_AEREA): ");
        String fecha = pedir("Fecha de ingreso (AAAA-MM-DD): ");
        var vista = registrar.registrar(mapper.aComando(
                new RegistrarSoldadoRequest(nombres, apellidos, componente, fecha)));
        salida.println("Registrado: " + mapper.aRespuesta(vista));
    }

    private void ascenderSoldado() {
        String id = pedir("Id del soldado: ");
        String fecha = pedir("Fecha del ascenso (AAAA-MM-DD, vacío = hoy): ");
        LocalDate f;
        try {
            f = fecha.isBlank() ? LocalDate.now() : LocalDate.parse(fecha);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe tener formato AAAA-MM-DD");
        }
        var vista = ascender.ascender(new AscenderSoldadoCommand(id, f));
        salida.println("Ascendido: " + mapper.aRespuesta(vista));
    }

    private void consultarSoldado() {
        String id = pedir("Id del soldado: ");
        salida.println(mapper.aRespuesta(consultar.consultar(new ConsultarSoldadoQuery(id))));
    }

    private String pedir(String mensaje) {
        salida.print(mensaje);
        return entrada.nextLine();
    }
}
