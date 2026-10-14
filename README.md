# ms-digitalfix-notify

Microservicio de notificaciones para DigitalFix, encargado de consumir mensajes desde RabbitMQ y procesar acciones asociadas a órdenes de trabajo.

## Tecnologías

- Java 21
- Spring Boot
- Spring AMQP
- RabbitMQ
- Docker
- Maven

## Puerto

```text
8083
```

## Funcionalidad

El microservicio escucha eventos publicados por otros componentes de DigitalFix y procesa mensajes asociados a:

- Envío de correo.
- Despacho de técnicos.
- Generación de reportes.

Los mensajes son consumidos mediante `@RabbitListener`.

## RabbitMQ

### Exchanges

```text
cmd.direct
cmd.topic
cmd.dead.dlx
```

### Colas principales

```text
q.cmd.email
q.cmd.dispatch
q.cmd.report
```

### Dead Letter Queues

```text
q.cmd.email.dlq
q.cmd.dispatch.dlq
q.cmd.report.dlq
```

### Routing Keys

Direct:

```text
email.send
dispatch.ticket
report.gen
```

Topic:

```text
email.*
dispatch.#
report.*
```

DLQ:

```text
email.dead
dispatch.dead
report.dead
```

## Manejo de mensajes

El consumidor utiliza ACK manual.

Cuando un mensaje se procesa correctamente:

```text
ACK
```

Si ocurre un error durante el procesamiento:

```text
NACK
requeue = false
```

El mensaje es enviado automáticamente a la Dead Letter Queue correspondiente.

Esto evita ciclos infinitos de reintentos y permite revisar mensajes inválidos posteriormente.

## Ejemplo de flujo

```text
Workorders
    |
    v
RabbitMQ
    |
    +--> q.cmd.email ------> Notify
    |
    +--> q.cmd.dispatch ---> Notify
    |
    +--> q.cmd.report -----> Notify
                              |
                              +--> ACK
                              |
                              +--> Error -> DLQ
```

## Variables de entorno

El microservicio utiliza las siguientes variables:

```text
RABBITMQ_HOST
RABBITMQ_PORT
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
```

Ejemplo:

```properties
RABBITMQ_HOST=rabbitmq-1
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=usuario
RABBITMQ_PASSWORD=contraseña
```

No se recomienda almacenar credenciales reales directamente en el repositorio.

## Ejecución local

Compilar:

```bash
./mvnw clean package -DskipTests
```

Ejecutar:

```bash
./mvnw spring-boot:run
```

## Docker

Construir imagen:

```bash
docker build -t digitalfix-notify:latest .
```

Ejecutar:

```bash
docker run -d \
  --name digitalfix-notify \
  -e RABBITMQ_HOST=rabbitmq-1 \
  -e RABBITMQ_PORT=5672 \
  -e RABBITMQ_USERNAME=usuario \
  -e RABBITMQ_PASSWORD=contraseña \
  digitalfix-notify:latest
```

## Pruebas realizadas

Durante las pruebas se verificó:

- Conexión correcta con RabbitMQ.
- Consumo de mensajes de Email.
- Consumo de mensajes de Dispatch.
- Confirmación mediante ACK.
- Manejo de mensajes inválidos.
- Envío automático de mensajes fallidos hacia DLQ.
- Integración con el microservicio Workorders.
- Ejecución sobre infraestructura AWS.

## Integración DigitalFix

Flujo probado:

```text
Frontend
   |
   v
API Gateway
   |
   v
BFF
   |
   v
Workorders
   |
   v
RabbitMQ
   |
   v
Notify
```

El microservicio forma parte de la arquitectura Cloud Native de DigitalFix y permite separar el procesamiento asíncrono del flujo principal de órdenes de trabajo.
