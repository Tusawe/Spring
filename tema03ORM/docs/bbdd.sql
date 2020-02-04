
-- COMO ROOT PARA CREAR LA BBDD Y EL USUARIO

-- CREAMOS LA BBDD PARA UTF-8 COLLATION EN ESPAÑOL
CREATE DATABASE gestion_reservas  \
	CHARACTER SET utf16 COLLATE utf16_spanish_ci;

-- CAMBIAMOS LA BBDD ACTIVA
USE gestion_reservas;


-- CREAMOS LAS TABLAS
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(12) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE='InnoDB';

CREATE TABLE `instalacion` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nombre` varchar(50) NOT NULL
) ENGINE='InnoDB';

CREATE TABLE `horario` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `instalacion` int(11) NOT NULL,
  `inicio` time NOT NULL,
  `fin` time NOT NULL,
  FOREIGN KEY (`instalacion`) REFERENCES `instalacion` (`id`) ON DELETE CASCADE
) ENGINE='InnoDB';

CREATE TABLE `reserva` (
  `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `usuario` int(11) NOT NULL,
  `horario` int(11) NOT NULL,
  `fecha` date NOT NULL,
  FOREIGN KEY (`usuario`) REFERENCES `usuario` (`id`),
  FOREIGN KEY (`horario`) REFERENCES `horario` (`id`)
) ENGINE='InnoDB';



DROP TRIGGER IF EXISTS reserva_pasado;

DELIMITER $$
CREATE TRIGGER `reserva_pasado`
  BEFORE DELETE ON `reserva` FOR EACH ROW
  BEGIN
    IF ( OLD.`fecha` < CURDATE())
    THEN
      SIGNAL sqlstate '45004'
        SET message_text = 'No se permite eliminar una fecha pasada.';
    END IF;
  END;
$$


DROP TRIGGER IF EXISTS reserva_actualizar_pasado;

DELIMITER $$
CREATE TRIGGER `reserva_actualizar_pasado`
  BEFORE UPDATE ON `reserva` FOR EACH ROW
  BEGIN
    IF ( OLD.`fecha` <= CURDATE())
    THEN
      SIGNAL sqlstate '45004'
        SET message_text = 'No se permite actualizar una reserva ya pasada o en el día de la misma.';
    END IF;
  END;
$$



DROP TRIGGER IF EXISTS reserva_semanal;

DELIMITER $$
CREATE TRIGGER `reserva_semanal`
  BEFORE INSERT ON `reserva` FOR EACH ROW
  BEGIN
    IF ( NEW.`fecha` < CURDATE())
    THEN
      SIGNAL sqlstate '45002'
        SET message_text = 'No se permite reservar en una fecha anterior a la actual.';
    ELSEIF ( NEW.`fecha` > DATE_ADD(CURDATE(), INTERVAL 14 DAY) )
    THEN
        SIGNAL sqlstate '45003'
          SET message_text = 'No se permite reservar con más de dos semanas de antelación.';
    END IF;
  END;
$$

DROP TABLE user;

CREATE TABLE user (
  `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user` varchar(100),
  `pwd` varchar(100),
  `token` varchar(255),
  `token_valid_from` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `token_valid_until` TIMESTAMP NOT NULL DEFAULT (TIMESTAMPADD(DAY,21,CURRENT_TIMESTAMP()))
 -- `token_valid_until` TIMESTAMP NOT NULL DEFAULT (CURRENT_DATE + INTERVAL 21 DAY)
) ENGINE = 'InnoDB'; 

INSERT INTO user(user,pwd) VALUES ('admin','admin');
