-- Volcando estructura de base de datos para visercomcfg
CREATE DATABASE IF NOT EXISTS `visercomcfg`;
USE `visercomcfg`;

-- Role Definition
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;

-- Grupo Definition
CREATE TABLE IF NOT EXISTS `grupo` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(200) NOT NULL,
	`hash` VARCHAR(200) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;

-- Room Definition
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `expires_at` timestamp NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `is_enabled` tinyint(1) NOT NULL,
  `has_regexp` tinyint(1) DEFAULT 0,
  `regexp` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;

-- User Definition
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(100) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL,
  `profile` bigint(20) NOT NULL,
  `grupo` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  KEY `FK_user_grupo` (`grupo`),
  KEY `FK_user_role` (`profile`),
  CONSTRAINT `FK_user_grupo` FOREIGN KEY (`grupo`) REFERENCES `grupo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_user_role` FOREIGN KEY (`profile`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;

-- INSERT Basic Information
INSERT INTO visercomcfg.role
(id, name, description)
VALUES(1, 'sysadmin', 'Usuario administrador del sistema');
INSERT INTO visercomcfg.role
(id, name, description)
VALUES(2, 'Admin', 'Administradores de grupo');
INSERT INTO visercomcfg.role
(id, name, description)
VALUES(3, 'Client', 'Usuario cliente, con acceso a web, pero permisos limitados');
INSERT INTO visercomcfg.role
(id, name, description)
VALUES(4, 'API', 'Usuario de la API sin acceso a web');
-- Volcando estructura para tabla visercomcfg.user

INSERT INTO visercomcfg.grupo
(id, name, hash)
VALUES(1, 'CESTEL', 'd50271');

INSERT INTO visercomcfg.user
(id, username, password, email, profile, grupo) VALUES
(1, 'sysadmin', '$2a$10$Sn0JM7LtHO2s21XQlbsyg.WZoCpai2T3qgYMVX69V/bu./PfGd1e2', 'sysadmin@cestel.es', 1, 1);
