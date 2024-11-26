-- Tabla para los roles (nombre codificado en hexadecimal)
CREATE TABLE 726f6c6573 (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla para los usuarios (nombre codificado en hexadecimal)
CREATE TABLE 7573756172696f (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla intermedia para la relación Many-to-Many (nombre codificado en hexadecimal)
CREATE TABLE 7573756172696f5f726f6c6573 (
    usuario_id BIGINT,
    rol_id BIGINT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES 7573756172696f(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES 726f6c6573(id) ON DELETE CASCADE
);

CREATE TABLE `6e756d65726163696f6e` (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  entrada_m1 int(11) NOT NULL,
  salida_m1 int(11) NOT NULL,
  entrada_m2 int(11) NOT NULL,
  salida_m2 int(11) NOT NULL,
  bar varchar(50) COLLATE latin1_spanish_ci DEFAULT 'lucy',
  	
);

CREATE TABLE `7573756172696f73636f6e6164656c616e746f` (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  cantidad_asignada float NOT NULL DEFAULT '0'
);

CREATE TABLE `636f6e636570746f676173746f6164656c616e746f` (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   descripcion varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
   idusuariosconadelanto BIGINT NOT NULL,
   total float NOT NULL,
   fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE 646f63756d656e746f73 (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    path VARCHAR(500) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE 646f63756d656e746f73 (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    path VARCHAR(500) DEFAULT NULL, -- Las carpetas no necesitan un path físico
    is_folder BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si es una carpeta
    parent_id BIGINT DEFAULT NULL, -- Referencia al padre
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES documents(id) ON DELETE CASCADE
);


-- Insertar roles en la tabla roles (nombre hexadecimal: 726f6c6573)
INSERT INTO 726f6c6573 (nombre) VALUES ('SUPERADMIN');
INSERT INTO 726f6c6573 (nombre) VALUES ('ADMIN');
INSERT INTO 726f6c6573 (nombre) VALUES ('CLIENTE');

-- Insertar un usuario en la tabla usuario (nombre hexadecimal: 7573756172696f)
INSERT INTO 7573756172696f (nombre, email, password, enabled) 
VALUES ('vmhuecas', 'victor.huecas@gmail.com', '$2a$10$X8O9eFtxFbPVO7RQ6XFl7uuM1KwBG34gqEj7Xo2PfYZ6RtLJ3DsXu', true);

-- Asumimos que el id del rol SUPERADMIN es 1
-- Insertar la relación en la tabla usuario_roles (nombre hexadecimal: 7573756172696f5f726f6c6573)
INSERT INTO 7573756172696f5f726f6c6573 (usuario_id, rol_id) 
VALUES (1, 1); -- Ajusta el usuario_id según el ID generado automáticamente

