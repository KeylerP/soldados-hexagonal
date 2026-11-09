package co.edu.unicartagena.soldados.domain;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de arquitectura casera: el paquete /domain solo puede importar java.*
 * o clases del propio dominio. Si alguien mete un framework o una clase de
 * infraestructura/aplicación, esta prueba falla.
 */
class DominioSinFrameworksTest {

    private static final Path RAIZ = Path.of("src/main/java/co/edu/unicartagena/soldados/domain");

    @Test
    void elDominioSoloImportaJavaYSusPropiasClases() throws IOException {
        try (Stream<Path> archivos = Files.walk(RAIZ)) {
            List<String> importsProhibidos = archivos
                    .filter(p -> p.toString().endsWith(".java"))
                    .flatMap(DominioSinFrameworksTest::importsDe)
                    .filter(i -> !i.startsWith("import java.")
                            && !i.startsWith("import co.edu.unicartagena.soldados.domain."))
                    .toList();
            assertThat(importsProhibidos).isEmpty();
        }
    }

    @Test
    void elDominioNoDependeDeAplicacionNiInfraestructura() throws IOException {
        try (Stream<Path> archivos = Files.walk(RAIZ)) {
            List<String> malos = archivos
                    .filter(p -> p.toString().endsWith(".java"))
                    .flatMap(DominioSinFrameworksTest::importsDe)
                    .filter(i -> i.contains(".application.") || i.contains(".infrastructure."))
                    .toList();
            assertThat(malos).isEmpty();
        }
    }

    private static Stream<String> importsDe(Path archivo) {
        try {
            return Files.readAllLines(archivo).stream().map(String::trim).filter(l -> l.startsWith("import "));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
