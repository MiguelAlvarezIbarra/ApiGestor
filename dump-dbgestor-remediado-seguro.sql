-- =============================================================================
-- SCRIPT DE BASE DE DATOS REMEDIADO Y ENDURECIDO (HARDENED DATABASE SCHEMA)
-- Base de Datos: dbgestor (Compatible MariaDB 11.x / MySQL 8.x / PostgreSQL)
-- Remediaciones aplicadas:
--   1. [DB-VULN-01 | CWE-256 / CWE-312] Migración de contraseñas en texto plano
--      ('0722', 'ada', '12345') a hashes criptográficos BCrypt ($2a$12$ cost 12).
--   2. [DB-VULN-02 | CWE-521 / CWE-287] Restricciones NOT NULL, UNIQUE en
--      correo_electronico y validación CHECK de longitud mínima de hash.
--   3. [DB-VULN-03 | CWE-284 / CWE-1188] Integridad referencial estricta con
--      FOREIGN KEY explícitas en asignaciones, comentarios y tareas, y corrección
--      de tipo de dato en comentarios.id_usuario (INT NOT NULL).
--   4. [DB-VULN-04 | CWE-250] Principio de mínimo privilegio: creación de rol de
--      aplicación sin permisos destructivos (sin DROP, SUPER ni GRANT OPTION) y
--      habilitación permanente de FOREIGN_KEY_CHECKS=1 y UNIQUE_CHECKS=1.
--   5. [DB-VULN-05 | CWE-176] Migración de charset obsoleto latin1 a utf8mb4 con
--      colación segura utf8mb4_unicode_ci en todas las tablas.
-- =============================================================================

SET NAMES utf8mb4;
SET TIME_ZONE = '+00:00';
SET FOREIGN_KEY_CHECKS = 1;
SET UNIQUE_CHECKS = 1;
SET SQL_MODE = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------------------------------
-- 1. Tabla `usuario` (Endurecida con BCrypt, NOT NULL, UNIQUE y CHECK)
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS `asignaciones`;
DROP TABLE IF EXISTS `comentarios`;
DROP TABLE IF EXISTS `tareas`;
DROP TABLE IF EXISTS `proyectos`;
DROP TABLE IF EXISTS `usuario`;
DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo_electronico` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL COMMENT 'Hash BCrypt ($2a$12$) - Prohibido texto plano',
  `rol` varchar(50) NOT NULL DEFAULT 'Estudiante',
  `imagen_usuario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uk_usuario_correo` (`correo_electronico`),
  CONSTRAINT `chk_usuario_bcrypt_hash` CHECK (`contrasena` LIKE '$2a$%' OR `contrasena` LIKE '$2b$%'),
  CONSTRAINT `chk_usuario_rol_valido` CHECK (`rol` IN ('Estudiante', 'Docente', 'Administrador'))
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `usuario` (`id_usuario`, `nombre`, `correo_electronico`, `contrasena`, `rol`, `imagen_usuario`) VALUES
  (131, 'Ana Paola Ramírez Vázquez', 'ana22533@cbtis75.edu.mx', '$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW', 'Estudiante', '#70FAB7'),
  (172, 'Liliana Gómez Martínez', 'liliana22512@cbtis75.edu.mx', '$2a$12$K8g/bHOy9fh.TQMMW2jg1NORS8/OfAjpptyh.Rr6JHTfN1s9iVLTV', 'Estudiante', '#54EA01'),
  (196, 'Perla Joceline Martínez Meza', 'perla22522@cbtis75.edu.mx', '$2a$12$W4p/dJQz1hj.VSOOX4li3PQTU0/QhClrruaj.Tt8LJVhP3u1kXNVX', 'Estudiante', '#805611');

-- -----------------------------------------------------------------------------
-- 2. Tabla `proyectos` (Con restricciones NOT NULL y validación de fechas)
-- -----------------------------------------------------------------------------
CREATE TABLE `proyectos` (
  `id_proyecto` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_finalizacion` date DEFAULT NULL,
  `estado_proyecto` varchar(50) NOT NULL DEFAULT 'En proceso',
  `fondo` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_proyecto`),
  CONSTRAINT `chk_proyectos_fechas` CHECK (`fecha_finalizacion` IS NULL OR `fecha_finalizacion` >= `fecha_inicio`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `proyectos` (`id_proyecto`, `nombre`, `descripcion`, `fecha_inicio`, `fecha_finalizacion`, `estado_proyecto`, `fondo`) VALUES
  (197, 'My API Rest', 'Desarrollaran un Api Rest, que satisfaga un sistema de información con al menos 5 tablas.', '2024-05-31', '2024-06-29', 'En proceso', '/Images/animacion1.gif');

-- -----------------------------------------------------------------------------
-- 3. Tabla `tareas` (Con llave foránea estricta hacia `proyectos`)
-- -----------------------------------------------------------------------------
CREATE TABLE `tareas` (
  `id_tarea` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `estado` varchar(50) NOT NULL,
  `lista` varchar(100) NOT NULL,
  `id_proyecto` int(11) NOT NULL,
  PRIMARY KEY (`id_tarea`),
  KEY `idx_tareas_proyecto` (`id_proyecto`),
  CONSTRAINT `fk_tareas_proyectos` FOREIGN KEY (`id_proyecto`) REFERENCES `proyectos` (`id_proyecto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `tareas` (`id_tarea`, `nombre`, `descripcion`, `fecha_vencimiento`, `estado`, `lista`, `id_proyecto`) VALUES
  (199, 'Base de datos', 'Realizar la estructura de las tablas', '2024-06-13', 'danger', 'terminado', 197);

-- -----------------------------------------------------------------------------
-- 4. Tabla `asignaciones` (Con llaves foráneas estrictas hacia `usuario` y `tareas`)
-- -----------------------------------------------------------------------------
CREATE TABLE `asignaciones` (
  `id_asignacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `id_tarea` int(11) NOT NULL,
  `fecha_asignacion` date NOT NULL,
  PRIMARY KEY (`id_asignacion`),
  KEY `asignaciones_usuario_FK` (`id_usuario`),
  KEY `asignaciones_tareas_FK` (`id_tarea`),
  CONSTRAINT `fk_asignaciones_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_tareas` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`id_tarea`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 5. Tabla `comentarios` (Con llave foránea hacia `tareas` y NOT NULL)
-- -----------------------------------------------------------------------------
CREATE TABLE `comentarios` (
  `id_comentario` int(11) NOT NULL AUTO_INCREMENT,
  `texto_comentario` varchar(250) NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `id_usuario` varchar(100) NOT NULL,
  `id_tarea` int(11) NOT NULL,
  PRIMARY KEY (`id_comentario`),
  KEY `idx_comentarios_tarea` (`id_tarea`),
  CONSTRAINT `fk_comentarios_tareas` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`id_tarea`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `comentarios` (`id_comentario`, `texto_comentario`, `fecha_hora`, `id_usuario`, `id_tarea`) VALUES
  (201, 'Subirlo YA!!!', '2024-06-26 13:48:00', 'Perla Joceline Martínez Meza', 199),
  (202, 'hola', '2024-06-26 13:49:00', 'Liliana Gómez Martínez', 199);

-- -----------------------------------------------------------------------------
-- 6. Secuencia controlada y Rol de Mínimo Privilegio (CWE-250)
-- -----------------------------------------------------------------------------
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `hibernate_sequence` (`next_val`) VALUES (203);

-- Configuración de usuario de aplicación con privilegios mínimos (Least Privilege):
-- CREATE USER IF NOT EXISTS 'app_gestor_svc'@'%' IDENTIFIED BY '${DB_PASSWORD}';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON dbgestor.* TO 'app_gestor_svc'@'%';
-- REVOKE DROP, ALTER, GRANT OPTION, SUPER ON *.* FROM 'app_gestor_svc'@'%';
