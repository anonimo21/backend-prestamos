# prestamos

API REST en **Spring Boot 3.3.4** (Java 17) para la gestión de préstamos y clientes.
Consumida por un frontend Angular en `http://localhost:4200` (ver `@CrossOrigin` en `ClienteController`).

## Stack

- Spring Boot 3.3.4, Java 17
- Spring Web, Spring Data JPA, Spring Validation, Spring Actuator
- MySQL (producción) / H2 (runtime de pruebas)
- Lombok 1.18.40
- springdoc-openapi 2.6.0 (Swagger UI)
- Maven (wrapper incluido: `./mvnw`)

## Arquitectura en capas

```
controller/   → expone los endpoints REST (recibe/responde DTOs)
service/      → lógica de negocio, validaciones, transacciones
repository/   → interfaces Spring Data JPA (acceso a BD)
domain/       → entidades JPA (@Entity) que mapean a tablas
dto/          → objetos de entrada/salida (no son entidades)
mapper/       → convierte entre entity y DTO
exception/    → excepciones personalizadas + @ControllerAdvice global
```

## Entidades (`src/main/java/com/prestamos/prestamos/domain/`)

### 1. `Cliente` — tabla `clientes`
Representa a la persona que solicita préstamos.

| Campo              | Tipo              | Notas                                                       |
|--------------------|-------------------|-------------------------------------------------------------|
| `id`               | Long              | PK auto-increment                                           |
| `identificacion`   | String            | Único, mapea a columna `dni` (DNI/cédula)                   |
| `nombre`           | String            |                                                             |
| `apellido`         | String            |                                                             |
| `email`            | String            |                                                             |
| `telefono`         | String            |                                                             |
| `direccion`        | String            |                                                             |
| `fechaRegistro`    | LocalDateTime     | Se setea sola en `@PrePersist`                              |
| `prestamos`        | `List<Prestamo>`  | `@OneToMany` — los préstamos del cliente                    |

Método helper: `getNombreCompleto()`.
Regla de negocio: **no se puede eliminar si tiene préstamos asociados** (`BusinessRuleException`).

### 2. `Prestamo` — tabla `prestamos`
Solicitud de crédito aprobada/en curso.

| Campo              | Tipo              | Notas                                                          |
|--------------------|-------------------|----------------------------------------------------------------|
| `id`               | Long              | PK                                                             |
| `cliente`          | `Cliente`         | `@ManyToOne` FK `cliente_id`                                   |
| `monto`            | Double            | Capital solicitado                                             |
| `tasaInteres`      | Double            | % (ej: 10.0)                                                   |
| `plazoMeses`       | Integer           | Plazo en meses                                                 |
| `montoTotal`       | Double            | `monto + interes`                                              |
| `cuotaMensual`     | Double            | Cuota fija mensual                                             |
| `estado`           | `EstadoPrestamo`  | Enum (PENDIENTE/APROBADO/RECHAZADO/PAGADO)                     |
| `fechaSolicitud`   | LocalDateTime     | `@PrePersist`                                                  |
| `fechaAprobacion`  | LocalDateTime     | Nullable                                                       |
| `cuotas`           | `List<Cuota>`     | `@OneToMany` con `@OrderBy("numeroCuota ASC")`                 |

### 3. `Cuota` — tabla `cuotas`
Cada una de las mensualidades que componen un préstamo.

| Campo              | Tipo              | Notas                                                  |
|--------------------|-------------------|--------------------------------------------------------|
| `id`               | Long              | PK                                                     |
| `prestamo`         | `Prestamo`        | `@ManyToOne` FK `prestamo_id`                          |
| `numeroCuota`      | Integer           | 1, 2, 3...                                             |
| `monto`            | Double            | Importe de esta cuota                                  |
| `fechaVencimiento` | LocalDate         |                                                        |
| `estado`           | `EstadoCuota`     | Enum (PENDIENTE/PAGADO/VENCIDO)                        |
| `fechaPago`        | LocalDate         | Nullable hasta que se pague                            |

Al crear un préstamo, el servicio genera **N cuotas** automáticamente (una por mes) y la última absorbe el redondeo para cuadrar el total.

### 4. `EstadoPrestamo` (enum)
Ciclo de vida del préstamo:

- `PENDIENTE` — recién solicitado, esperando decisión.
- `APROBADO` — en curso de pago (cuotas activas).
- `RECHAZADO` — denegado.
- `PAGADO` — todas las cuotas pagadas.

Cada valor tiene un `descripcion` legible (ej: `"Aprobado, en curso de pago"`).

### 5. `EstadoCuota` (enum)
Estado individual de cada mensualidad:

- `PENDIENTE` — aún no vencida o sin pagar.
- `PAGADO` — pagada.
- `VENCIDO` — pasó la fecha de vencimiento sin pago.

## Endpoints principales

- `GET /api/v1/clientes?buscar=...` — listar/buscar clientes.
- `POST /api/v1/clientes` — crear (valida que no exista la `identificacion`).
- `GET /api/v1/prestamos?estado=APROBADO` — filtrar préstamos.
- `POST /api/v1/prestamos` — crear préstamo → genera las N cuotas en cascada.
- `PUT /api/v1/prestamos/{id}` — actualizar.
- `DELETE /api/v1/{recurso}/{id}` — eliminar.

## Excepciones personalizadas

- `ResourceNotFoundException` → 404 (cliente/préstamo no existe).
- `DuplicateResourceException` → 409 (identificación duplicada).
- `BusinessRuleException` → 422 (cliente con préstamos no se puede borrar).
- `GlobalExceptionHandler` — `@ControllerAdvice` que las traduce a `ApiResponse` uniforme.

## Resumen en una línea

**Clientes piden préstamos → los préstamos se dividen en cuotas → cada cuota tiene un estado** — y toda esa jerarquía se expone como una API REST con DTOs y mappers para no filtrar entidades JPA hacia el exterior.
