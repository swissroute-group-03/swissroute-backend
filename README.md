# 🚆 SwissRoute Backend

Backend REST del proyecto **SwissRoute**, desarrollado por el **Grupo 03** con Spring Boot, PostgreSQL y Spring Security.

---

# 🛠️ Tecnologías Utilizadas

- Java 21
- Spring Boot 3
- PostgreSQL
- Spring Data JPA / Hibernate
- Spring Security + JWT
- WebClient
- Swagger / OpenAPI
- Maven
- Lombok
- MapStruct

---

# 📂 Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:

- **Controller:** exposición de endpoints REST.
- **Service:** lógica de negocio.
- **Repository:** acceso a datos.
- **DTO:** transferencia de información.
- **Mapper:** conversión Entity ↔ DTO con MapStruct.
- **Exception Handler:** manejo global de excepciones.

---

# 📥 Clonar el Repositorio

```bash
git clone https://github.com/swissroute-group-03/swissroute-backend.git
cd swissroute-backend
```

---

# 🐘 Configuración de PostgreSQL

Ingresar a PostgreSQL con el usuario administrador:

```bash
psql -U postgres
```

Crear el usuario del proyecto:

```sql
CREATE USER swissroute_user WITH PASSWORD 'swissroute';
```

Crear la base de datos:

```sql
CREATE DATABASE swissroute_db OWNER swissroute_user;
```

Conectarse a la base de datos:

```sql
\c swissroute_db
```

Crear el schema del proyecto:

```sql
CREATE SCHEMA IF NOT EXISTS swissroute AUTHORIZATION swissroute_user;
```

Dar permisos sobre el schema:

```sql
GRANT ALL PRIVILEGES ON SCHEMA swissroute TO swissroute_user;
```

Dar permisos sobre tablas y secuencias existentes:

```sql
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA swissroute TO swissroute_user;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA swissroute TO swissroute_user;
```

Dar permisos automáticos sobre futuras tablas y secuencias:

```sql
ALTER DEFAULT PRIVILEGES IN SCHEMA swissroute
GRANT ALL PRIVILEGES ON TABLES TO swissroute_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA swissroute
GRANT ALL PRIVILEGES ON SEQUENCES TO swissroute_user;
```

---

# ⚙️ Variables de Entorno

Cada integrante puede crear su propio archivo de variables de entorno según su configuración local.

## Ejemplo para Windows PowerShell

```powershell
[System.Environment]::SetEnvironmentVariable('DB_HOST', 'localhost', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('DB_PORT', '5432', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('DB_NAME', 'swissroute_db', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('DB_USER', 'swissroute_user', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('DB_PASS', 'swissroute', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('SERVER_PORT', '8080', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('TRANSPORT_API_BASE_URL', 'http://transport.opendata.ch/v1', [System.EnvironmentVariableTarget]::User)

[System.Environment]::SetEnvironmentVariable('SECRET_KEY', 'jwt-secret-key', [System.EnvironmentVariableTarget]::User)
```

Después de registrar las variables de entorno, cerrar y volver a abrir IntelliJ IDEA o la terminal.

---

# 🗄️ Datos Iniciales

El proyecto cuenta con el archivo:

```txt
src/main/resources/import.sql
```

Este archivo registra automáticamente:

- 2 roles iniciales: `ADMIN` y `USER`.
- 2 usuarios de prueba.

El archivo se ejecuta cuando Hibernate crea las tablas al iniciar el proyecto.

---

# ▶️ Ejecutar el Proyecto

Desde la raíz del proyecto:

```bash
mvn clean spring-boot:run
```

La aplicación iniciará en:

```txt
http://localhost:8080
```

---

# 📘 Swagger / OpenAPI

La documentación estará disponible en:

```txt
http://localhost:8080/swagger-ui/index.html
```

---

# ✅ Funcionalidades Implementadas

- Configuración base Spring Boot
- Conexión a PostgreSQL
- Registro de usuarios
- Encriptación de contraseñas con BCrypt
- Spring Security + JWT
- Swagger / OpenAPI
- DTOs y Mappers con MapStruct
- Manejo global de excepciones

---

# 👥 Equipo

Proyecto desarrollado por:

```txt
SwissRoute Group 03
```