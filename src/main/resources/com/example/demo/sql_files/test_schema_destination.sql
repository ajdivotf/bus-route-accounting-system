CREATE DATABASE  IF NOT EXISTS `test_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `test_schema`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: test_schema
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `destination`
--

DROP TABLE IF EXISTS `destination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `destination` (
  `destination_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`destination_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `destination`
--

LOCK TABLES `destination` WRITE;
/*!40000 ALTER TABLE `destination` DISABLE KEYS */;
INSERT INTO `destination` VALUES (1,'Анапа'),(2,'Астрахань'),(3,'Белая Калитва'),(4,'Белгород, автовокзал'),(5,'Боковская'),(6,'Вешенская'),(7,'Владикавказ'),(8,'Волгоград'),(9,'Волгодонск'),(10,'Геленджик'),(11,'Глубокий'),(12,'Горняцкий'),(13,'Городовиковск'),(14,'Дербент'),(15,'Донецк'),(16,'Ейск'),(17,'Зверево'),(18,'Зерноград'),(19,'Зимовники'),(20,'Кабардинка'),(21,'Каменск-Шахтинский'),(22,'Каневская'),(23,'Кашары'),(24,'Краснодар-2, автостанция'),(25,'Красный Луч (Хрустальный)'),(26,'Кропоткин'),(27,'Куйбышево'),(28,'Курганинск'),(29,'Луганск'),(30,'Махачкала'),(31,'Миллерово'),(32,'Морозовск'),(33,'Москва'),(34,'Мостовской'),(35,'Нальчик'),(36,'Новошахтинск'),(37,'Орловский'),(38,'Покровское'),(39,'Приморско-Ахтарск'),(40,'Пролетарск'),(41,'Ростов-на-Дону'),(42,'Саратов'),(43,'Свердловск (Должанск)'),(44,'Севастополь'),(45,'Снежное'),(46,'Ставрополь'),(47,'Стаханов (Кадиевка)'),(48,'Сухум'),(49,'Таганрог'),(50,'Тацинская'),(51,'Темрюк'),(52,'Тихорецк'),(53,'Торез (Чистяково)'),(54,'Цимлянск'),(55,'Шахты'),(56,'Ялта');
/*!40000 ALTER TABLE `destination` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-21 13:20:14
