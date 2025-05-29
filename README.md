# CryptoLive

**Simulador de criptomonedas en tiempo real**

CryptoLive es una plataforma backend desarrollada con Java y Spring Boot que permite a los usuarios:

* Consultar cotizaciones de criptomonedas en tiempo real vía WebSocket
* Simular compras y ventas de criptos en una cartera virtual
* Ver historial de operaciones y estado de la cartera

---

## 🧠 Resumen del Proyecto

CryptoLive es una **aplicación backend profesional** centrada en la simulación de inversiones en criptomonedas con datos en **tiempo real**. Desarrollado con **Java 17** y **Spring Boot**, el sistema permite a los usuarios consultar precios actualizados desde la API pública de CoinGecko, realizar operaciones virtuales (compras/ventas), y gestionar su propia cartera de criptos.

La arquitectura del sistema está orientada a microservicios, modularizada por capas (controlador, servicio, repositorio) y diseñada con buenas prácticas como el uso de **caché (Redis)**, **autenticación con JWT**, pruebas unitarias/integración y despliegue automatizado.

Entre las tecnologías clave empleadas se encuentran:

* **Spring WebSocket**: para enviar cotizaciones en tiempo real a los clientes.
* **Spring Security + JWT**: para autenticación y autorización por roles (`ROLE_USER`, `ROLE_ADMIN`).
* **MySQL (en Docker)**: como base de datos relacional.
* **Flyway**: para gestionar migraciones de esquema.
* **Redis**: como sistema de caché para reducir llamadas innecesarias a la API externa.
* **Resilience4j**: implementando retry y circuit breaker en las llamadas a CoinGecko.
* **Micrometer + Prometheus + Grafana**: para monitorización avanzada y métricas de salud.
* **JUnit + Mockito + Testcontainers**: para pruebas unitarias y de integración reales.
* **GitHub Actions**: para integración y despliegue continuo (CI/CD).

### 🎯 Objetivos del proyecto

* Brindar una experiencia de simulación realista de inversión en criptomonedas.
* Aplicar buenas prácticas en arquitectura, seguridad, resiliencia y pruebas.
* Exponer endpoints REST y WebSocket eficientes, seguros y documentados (OpenAPI).
* Construir un backend moderno y profesional como ejemplo de referencia.

Con **CryptoLive**, no solo tendrás una plataforma para aprender o testear tu estrategia de inversión en criptomonedas, sino también una base sólida para construir un backend escalable, robusto y mantenible.

---

## 🛠️ Tecnologías principales

* **Java 17+**
* **Spring Boot** (WebFlux, Security, Data JPA)
* **WebSocket** (para cotizaciones en tiempo real)
* **MySQL (Docker)**
* **Redis (caché de precios)**
* **Flyway** (migraciones SQL)
* **JWT (autenticación)**
* **Docker / Docker Compose**
* **OpenAPI / Swagger UI**
* **Resilience4j** (tolerancia a fallos)
* **JUnit 5 + Mockito + Testcontainers**
* **Micrometer + Prometheus + Grafana**

---

## 📂 Estructura del proyecto

```bash
cryptolive-backend/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── src
    ├── main
    │   ├── java/com/tuusuario/cryptolive
    │   │   ├── CryptoLiveApplication.java
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── service/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── security/
    │   │   └── websocket/
    │   └── resources/
    │       ├── application.yml
    │       └── db/migrations/
    └── test/java/com/tuusuario/cryptolive
```

---

## 🚀 Primeros pasos con IntelliJ

### 1. Crear proyecto con Spring Initializr

* Abre IntelliJ IDEA → `New → Project`
* Elige **Spring Initializr** y configura:

  * **Group**: `com.nabid`
  * **Artifact**: `cryptolive`
  * **Nombre**: `CryptoLive`
  * **Tipo**: Maven
  * **Versión de Java**: 17
  * **Dependencias**:

    * Spring Web
    * Spring Data JPA
    * Spring Security
    * Spring WebSocket
    * MySQL Driver
    * Lombok (opcional)
    * Spring Boot DevTools
    * Spring Boot Actuator

### 2. Docker Compose para MySQL

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: cryptolive-mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: cryptolive
    volumes:
      - mysql-data:/var/lib/mysql
volumes:
  mysql-data:
```

Lanza el servicio con:

```bash
docker-compose up -d
```

### 3. Configuración de la base de datos

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cryptolive?useSSL=false&serverTimezone=UTC
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 8080
```

### 4. Estructura de paquetes sugerida

* `config` → configuraciones WebSocket, JWT, etc.
* `controller` → endpoints REST y WebSocket
* `service` → lógica de negocio
* `model` → entidades y DTOs
* `repository` → interfaces de persistencia
* `security` → filtros y utilidades JWT
* `websocket` → gestión de sesiones y precios en tiempo real

### 5. Ejecutar la aplicación

* Corre `CryptoLiveApplication` desde IntelliJ
* Accede a `http://localhost:8080/actuator/health` para verificar estado

---

## ✅ Próximos pasos

* Implementar conexión a CoinGecko API
* Desarrollar controlador WebSocket para emitir precios
* Construir servicios para compra/venta de criptos virtuales
* Añadir autenticación completa con JWT
* Configurar Swagger para la documentación de la API

---

**CryptoLive**: tu punto de partida para simular, aprender y construir sobre tecnologías modernas del ecosistema Spring y criptomonedas. 🚀
