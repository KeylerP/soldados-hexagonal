# Paquete `/domain`

Aquí vive el núcleo del negocio del CEA **Control de Soldados**. Regla de oro: este paquete
**solo importa clases de `java.*`** (y de sí mismo). Nada de Spring, JPA, JDBC ni JSON.
La prueba `DominioSinFrameworksTest` lo verifica automáticamente.

| Subpaquete | Qué contiene |
|---|---|
| `model` | `Soldado` (raíz de agregado), `RegistroDeAscenso` (entidad interna), VOs: `SoldadoId`, `NombreCompleto`, `Rango`, `ComponenteMilitar` |
| `event` | `DomainEvent`, `SoldadoRegistradoEvent`, `SoldadoAscendidoEvent` |
| `exception` | `DominioException` y sus hijas: `AscensoInvalidoException`, `DatosSoldadoInvalidosException`, `SoldadoNoEncontradoException` |
| `service` | `ServicioAscensoMilitar` (servicio de dominio) |
| `specification` | `Specification<T>` y `SoldadoElegibleParaAscensoSpecification` |
| `factory` | `SoldadoFactory` |
| `port.out` | Puertos de salida: `SoldadoRepositoryPort`, `EventPublisherPort` |

## Patrones aplicados y por qué
- **Factory (`SoldadoFactory`)**: crear un soldado implica generar id, validar VOs y comprobar la fecha; con la factory eso queda en un solo lugar.
- **Specification (`SoldadoElegibleParaAscensoSpecification`)**: la regla de elegibilidad tiene nombre propio, se prueba sola y se puede combinar con `y`, `o`, `no`.
- **Domain Service (`ServicioAscensoMilitar`)**: la política de ascenso cruza la regla de tiempo con la del agregado y no cabe de forma natural dentro de `Soldado`.
