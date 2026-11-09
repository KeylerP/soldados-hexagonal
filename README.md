# Control de Soldados – Arquitectura Hexagonal (CEA Ejercicio 25)

Proyecto de la asignatura **Desarrollo de Software** (Ingeniería del Software, Universidad de Cartagena).
Aplica **DDD + Arquitectura Hexagonal (Puertos y Adaptadores)** al caso de estudio de control de soldados del
Ministerio de Defensa. Alcance de esta entrega: registro y ascenso de soldados.

## Requisitos
- Java 21
- Maven 3.9+

## Cómo ejecutar
```bash
mvn clean verify          # compila, corre las pruebas y valida >= 70 % de cobertura en el dominio
mvn -q compile
java -cp target/classes co.edu.unicartagena.soldados.Main     # consola interactiva
```
Reporte de cobertura: `target/site/jacoco/index.html`.

## Estructura
```
src/main/java/co/edu/unicartagena/soldados/
├── domain/           Núcleo del negocio (solo importa java.*)  -> ver domain/README.md
├── application/      Casos de uso: puertos IN, comandos/consultas, servicios, mapper
└── infrastructure/   Adaptadores OUT (persistencia en memoria, eventos) y entrypoint CLI
```

## Reglas de dependencia
`infrastructure -> application -> domain`. El dominio no conoce a nadie; la prueba
`DominioSinFrameworksTest` falla si aparece un import externo.

## Reglas de negocio principales
- Un soldado se registra siempre como `SOLDADO_REGULAR`.
- Solo se asciende al rango inmediatamente superior; no se salta ni se desciende.
- Para ser elegible debe llevar al menos 365 días en su rango actual.
- La fecha de ingreso no puede ser futura.
- Cada registro y cada ascenso emiten un evento de dominio.

## Autor
Keyler David Porto Contreras
