CREATE DATABASE  IF NOT EXISTS `testestpsit`
USE `testestpsit`;


DROP TABLE IF EXISTS `studenti`;

CREATE TABLE `studenti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

INSERT INTO `studenti` VALUES (1,'Leonardo','Mazzoni'),(2,'Andrea','Buono'),(3,'Andrea','Lotti'),(4,'Stefano','Caiazzo'),(5,'Francesco','Lazzarelli'),(6,'Lorenzo','Camigliano');

