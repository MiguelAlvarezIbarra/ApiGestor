# ApiGestor - Microservicio Backend API Rest

Microservicio backend basado en **Spring Boot** y **Java 21**, encargado de la lógica de negocio y persistencia de datos (gestión de usuarios, proyectos, tareas, comentarios y asignaciones).

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 21 (LTS)
- **Framework:** Spring Boot 2.7.17
- **Persistencia:** Spring Data JPA / Hibernate
- **Base de Datos:** PostgreSQL
- **Gestor de Dependencias:** Apache Maven 3.9+
- **Pruebas:** Spring Boot Starter Test

---

## 🚀 Requisitos y Ejecución

### Requisitos previos:
- JDK 21 instalado y configurado en `JAVA_HOME`.
- Base de datos PostgreSQL en ejecución.
- Apache Maven 3.9+ (o usar `./mvnw`).

### Compilar y empaquetar:
```bash
mvn clean package -DskipTests
```

### Ejecutar el microservicio:
```bash
mvn spring-boot:run
```

---

## 🔄 Actualización a Java 21

Se realizó el upgrade de **Java 11** a **Java 21 LTS** actualizando la propiedad `<java.version>21</java.version>` en el archivo `pom.xml`, habilitando compatibilidad para compilación con JDK 21 y Maven 3.9+.
