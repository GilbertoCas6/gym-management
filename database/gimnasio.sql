-- ============================================
-- BASE DE DATOS: GIMNASIO
-- Sistema de Gestión de Gimnasio (VERSIÓN FINAL)
-- ============================================

-- Eliminar base de datos si existe y crear nueva
DROP DATABASE IF EXISTS gimnasio;
CREATE DATABASE gimnasio CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE gimnasio;

-- ============================================
-- TABLA: usuarios
-- Almacena TODOS los usuarios del sistema
-- Roles: ADMIN, ENTRENADOR, CLIENTE
-- ============================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_ingreso DATE,
    objetivo VARCHAR(100),
    rol ENUM('ADMIN', 'ENTRENADOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    id_entrenador BIGINT NULL,  -- Para clientes: su entrenador asignado
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key: un cliente puede tener un entrenador
    CONSTRAINT fk_usuario_entrenador 
        FOREIGN KEY (id_entrenador) 
        REFERENCES usuarios(id) 
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: clases
-- Clases grupales del gimnasio
-- ============================================
CREATE TABLE clases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    id_entrenador BIGINT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,  -- Lunes, Martes, etc.
    hora_inicio TIME NOT NULL,
    duracion INT NOT NULL,  -- Duración en minutos
    capacidad_maxima INT NOT NULL DEFAULT 20,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_clase_entrenador 
        FOREIGN KEY (id_entrenador) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: inscripciones_clases
-- Relación entre clientes y clases
-- ============================================
CREATE TABLE inscripciones_clases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_clase BIGINT NOT NULL,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_inscripcion_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_inscripcion_clase 
        FOREIGN KEY (id_clase) 
        REFERENCES clases(id) 
        ON DELETE CASCADE,
    
    -- Un cliente no puede inscribirse dos veces a la misma clase
    UNIQUE KEY uk_cliente_clase (id_cliente, id_clase)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: rutinas
-- Rutinas de ejercicios asignadas a clientes
-- ============================================
CREATE TABLE rutinas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    id_cliente BIGINT NOT NULL,
    id_entrenador BIGINT NOT NULL,
    objetivo VARCHAR(255),
    dias_semana VARCHAR(255),  -- Ej: "Lunes,Miércoles,Viernes"
    duracion_semanas INT,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_rutina_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_rutina_entrenador 
        FOREIGN KEY (id_entrenador) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: pagos
-- Registro de pagos de membresías
-- ============================================
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    tipo_membresia VARCHAR(50) NOT NULL,  -- MENSUAL, TRIMESTRAL, ANUAL
    fecha_pago DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    metodo_pago VARCHAR(50),  -- EFECTIVO, TARJETA, TRANSFERENCIA
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',  -- ACTIVO, VENCIDO
    notas TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_pago_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: asistencias
-- Registro de entrada/salida del gimnasio
-- ============================================
CREATE TABLE asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora_entrada TIME NOT NULL,
    hora_salida TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_asistencia_cliente 
        FOREIGN KEY (id_cliente) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- INSERTAR DATOS DE PRUEBA
-- ============================================

-- USUARIOS (Admin, Entrenador, Cliente)
INSERT INTO usuarios (nombre, email, password, telefono, fecha_ingreso, objetivo, rol, activo) VALUES
-- Admin
('Gilberto Castillo', 'gilberto@gym.com', 'admin123', '809-111-1111', '2024-01-01', NULL, 'ADMIN', TRUE),

-- Entrenador
('Gaudi Valera', 'gaudi@gym.com', 'trainer123', '809-222-2222', '2024-01-05', NULL, 'ENTRENADOR', TRUE),

-- Cliente (asignado a Gaudi)
('Felix Sanchez', 'felix@gym.com', 'cliente123', '809-333-3333', '2024-01-15', 'Ganancia muscular', 'CLIENTE', TRUE);

-- Asignar Felix a Gaudi como su entrenador
UPDATE usuarios SET id_entrenador = 2 WHERE id = 3;

-- CLASES (2 clases de Gaudi)
INSERT INTO clases (nombre, descripcion, id_entrenador, dia_semana, hora_inicio, duracion, capacidad_maxima, activa) VALUES
('CrossFit Matutino', 'Entrenamiento funcional de alta intensidad', 2, 'Lunes', '07:00:00', 60, 15, TRUE),
('Funcional Vespertino', 'Entrenamiento funcional para todos los niveles', 2, 'Miércoles', '18:00:00', 60, 20, TRUE);

-- INSCRIPCIONES (Felix inscrito en las 2 clases)
INSERT INTO inscripciones_clases (id_cliente, id_clase) VALUES
(3, 1),  -- Felix en CrossFit Matutino
(3, 2);  -- Felix en Funcional Vespertino

-- RUTINA (Gaudi le asigna rutina a Felix)
INSERT INTO rutinas (nombre, descripcion, id_cliente, id_entrenador, objetivo, dias_semana, duracion_semanas, activa) VALUES
('Rutina de Hipertrofia', 
 'Programa de entrenamiento enfocado en ganancia de masa muscular. Incluye ejercicios compuestos y aislados.',
 3, 
 2, 
 'Aumentar 5kg de masa muscular', 
 'Lunes,Miércoles,Viernes',
 12,
 TRUE);

-- PAGO (Felix tiene membresía activa)
INSERT INTO pagos (id_cliente, monto, tipo_membresia, fecha_pago, fecha_vencimiento, metodo_pago, estado) VALUES
(3, 1500.00, 'MENSUAL', '2024-11-01', '2024-12-01', 'TARJETA', 'ACTIVO');

-- ASISTENCIAS (Felix ha asistido 2 veces)
INSERT INTO asistencias (id_cliente, fecha, hora_entrada, hora_salida) VALUES
(3, '2024-11-18', '07:00:00', '08:30:00'),
(3, '2024-11-20', '18:00:00', '19:15:00');

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT '✅ Base de datos creada exitosamente' AS Status;
SELECT 'Usuarios creados:' AS Info, COUNT(*) AS Total FROM usuarios;
SELECT 'Clases creadas:' AS Info, COUNT(*) AS Total FROM clases;
SELECT 'Rutinas creadas:' AS Info, COUNT(*) AS Total FROM rutinas;