# CryptoLive

**Simulador de criptomonedas en tiempo real**

CryptoLive es un backend desarrollado con Java 17 y Spring Boot que permite a los usuarios registrados gestionar un portafolio virtual de criptomonedas, con datos de precios obtenidos de CoinGecko y actualizados periódicamente.

---

## 🧠 Resumen del Proyecto

CryptoLive ofrece:

- **Autenticación**: Registro y login de usuarios con JWT y Google OAuth2.
- **Gestión de portafolio**: Añadir, editar y eliminar criptomonedas indicando la cantidad que posee el usuario.
- **Cálculo de valor**: Obtiene precios actuales desde la API pública de CoinGecko y calcula el valor de cada moneda y del portafolio.
- **Proceso BATCH**: Tarea programada que refresca precios cada 30 segundos y actualiza el valor en la base de datos.
- **WebSocket**: Emite cotizaciones en tiempo real a clientes suscritos  (frontend dinámico).
- **Historial y métricas**: Registro de operaciones y métricas de salud con Actuator, Micrometer, Prometheus y Grafana.

La arquitectura está modularizada en capas (`controller`, `service`, `repository`, `model`, `security`, `websocket`, etc.) y emplea buenas prácticas como caché con Redis, migraciones con Flyway, resiliencia con Resilience4j, pruebas unitarias e integración con Testcontainers, y despliegue en Docker.

---

## 🛠️ Tecnologías Principales

- **Java 17+** y **Spring Boot** (WebFlux, Security, Data JPA)
- **MySQL** en Docker (o alternativa MongoDB, evaluado según necesidades)
- **Redis** para cache de precios
- **Spring Security + JWT** y **OAuth2 (Google)**
- **CoinGecko API** (llamadas agrupadas para optimizar límites)
- **Spring WebSocket** para envío en tiempo real
- **Flyway** para migraciones de esquema
- **Resilience4j** para retry/circuit breaker en llamadas externas
- **Micrometer + Prometheus + Grafana** para monitorización
- **JUnit 5 + Mockito + Testcontainers** para pruebas
- **Swagger/OpenAPI** para documentar la API
- **GitHub Actions** para CI/CD
- **Docker / Docker Compose** para orquestar servicios

---

## 📂 Estructura del Proyecto

```bash
cryptolive-backend/
├── docker-compose.yml       # MySQL, Redis, otros servicios
├── Dockerfile               # Construcción de la imagen de la aplicación
├── pom.xml                  # Dependencias Maven
└── src
    ├── main
    │   ├── java/com/tuusuario/cryptolive
    │   │   ├── CryptoLiveApplication.java
    │   │   ├── config/       # Configuraciones (WebClient, WebSocket, Security)
    │   │   ├── controller/   # Controladores REST y WebSocket
    │   │   ├── service/      # Lógica de negocio
    │   │   ├── model/        # Entidades JPA / Documentos y DTOs
    │   │   ├── repository/   # Repositorios de datos
    │   │   ├── security/     # JWT, OAuth2, filtros y utilidades
    │   │   └── websocket/    # Configuración y mensajería STOMP
    │   └── resources
    │       ├── application.yml  # Configuración de Spring, BD, OAuth2, JWT
    │       └── db/migrations    # Scripts Flyway
    └── test/java/com/tuusuario/cryptolive
```

---

## 🚀 Configuración y Primeros Pasos

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/tuusuario/cryptolive-backend.git
   cd cryptolive-backend
   ```

2. **Variables de Entorno**

   - Crear archivo `.env` (no versionar) con:
     ```dotenv
     DATABASE_URL=jdbc:mysql://localhost:3306/cryptolive?useSSL=false&serverTimezone=UTC
     DATABASE_USER=root
     DATABASE_PASSWORD=root
     JWT_SECRET=ClaveMuySeguraYLarga
     GOOGLE_CLIENT_ID=tu_google_client_id
     GOOGLE_CLIENT_SECRET=tu_google_client_secret
     ```
   - En `application.yml`, referenciar:
     ```yaml
     spring:
       datasource:
         url: ${DATABASE_URL}
         username: ${DATABASE_USER}
         password: ${DATABASE_PASSWORD}
       jpa:
         hibernate:
           ddl-auto: update
         show-sql: true
       security:
         oauth2:
           client:
             registration:
               google:
                 client-id: ${GOOGLE_CLIENT_ID}
                 client-secret: ${GOOGLE_CLIENT_SECRET}
                 scope: openid, profile, email
     jwt:
       secret: ${JWT_SECRET}
       expiration-ms: 3600000
     ```
   - Asegúrate de que `.gitignore` incluya el `.env` y no incluya credenciales.

3. **Levantar servicios con Docker Compose**

   - `docker-compose.yml` debe definir al menos:
     - MySQL: usuario/clave `root`, base `cryptolive`
     - Redis: para cache
   - Ejecutar:
     ```bash
     docker-compose up -d
     ```

4. **Ejecutar la aplicación**

   - Desde IntelliJ o línea de comando:
     ```bash
     mvn spring-boot:run
     ```
   - Verificar salud:
     ```bash
     curl http://localhost:8080/actuator/health
     ```

5. **Swagger UI**

   - Acceder en: `http://localhost:8080/swagger-ui.html` para explorar la API.

---

## 📬 Resultados en Postman

A continuación se muestran ejemplos visuales de las peticiones y respuestas en Postman para registro, login y CRUD de portafolio. Mantén estas capturas en la documentación o en el repositorio como referencia.

### 1. Registro de Usuario

- **Endpoint:** `POST /auth/signup`
- **Validaciones:**
  - Email no exista en BD
  - Contraseña mínimo 8 caracteres
  - Email contenga `@`
![image](https://github.com/user-attachments/assets/4347da73-5a8b-41a3-94bf-6d4a8a9d4a83)



### 2. Login de Usuario

- **Endpoint:** `POST /auth/login`
- **Respuesta:** JSON con el JWT que el frontend usará en futuras peticiones
![image](https://github.com/user-attachments/assets/24b8279a-92ab-4663-9283-f62136153cf2)



### 3. Añadir Moneda al Portafolio

- **Endpoint:** `POST /api/portfolio/coins`
- **Autorización:** Bearer Token con el JWT obtenido en login
- **Body ejemplo:** `{ "coinId": "bitcoin", "quantity": 0.5 }`
![image](https://github.com/user-attachments/assets/e798b65d-f2af-4fa9-b53f-a9f34020de7e)



- **Header Autorización:**&#x20;
- ![image](https://github.com/user-attachments/assets/08623655-2f9a-499f-9e65-e392d648af88)


### 4. Ver Monedas en Portafolio

- **Endpoint:** `GET /api/portfolio`
- **Respuesta:** Lista de monedas con:
  - `coinId`, `quantity`, `lastPrice`, `totalValue` (quantity \* lastPrice)
  - Valor total del portafolio sumando todas las monedas
![image](https://github.com/user-attachments/assets/43cdd92b-1653-4901-96d9-dfa1efb9ee0e)



### 5. Editar Cantidad en Portafolio

- **Endpoint:** `PUT /api/portfolio/coins/{coinId}`
- **Body ejemplo:** `{ "quantity": 1.2 }`
![image](https://github.com/user-attachments/assets/cb4d0522-1ee9-4bee-bd17-c27009ac977d)



### 6. Eliminar Moneda del Portafolio

- **Endpoint:** `DELETE /api/portfolio/coins/{coinId}`
  ![image](https://github.com/user-attachments/assets/541e4888-c2a4-4094-a7cd-632cb6a40c12)




> **Nota**: Asegúrate de incluir siempre en Headers la autorización: `Authorization: Bearer <JWT>` en las peticiones protegidas.

---

## ✅ Próximos Pasos

- **Conexión a CoinGecko API**: implementar búsquedas y obtención de precios agrupados con WebClient y manejar límites (cache y batch).
- **WebSocket**: configurar broadcasting de precios en tiempo real si el frontend lo requiere.
- **Servicios de portafolio**: lógica para cálculo de valor total y actualización programada cada 10s.
- **Pruebas**: añadir unitarias e integración con Testcontainers para asegurar el comportamiento de portafolio.
- **Monitorización**: integrar métricas y health checks para el módulo de portafolio.
- **Documentación y ejemplos**: ampliar Swagger y colecciones de Postman / OpenAPI.

---

**CryptoLive** es tu backend de referencia para simular y gestionar inversiones en criptomonedas con buenas prácticas de desarrollo, seguridad y despliegue. 🚀

