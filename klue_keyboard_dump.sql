-- MySQL dump 10.13  Distrib 9.3.0, for macos15.2 (arm64)
--
-- Host: localhost    Database: klue_keyboard
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Cable`
--

DROP TABLE IF EXISTS `Cable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `length` int DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `connector` varchar(100) DEFAULT NULL,
  `quality` float DEFAULT NULL,
  `flexibility` float DEFAULT NULL,
  `durability` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `detachable` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `cable_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cable`
--

LOCK TABLES `Cable` WRITE;
/*!40000 ALTER TABLE `Cable` DISABLE KEYS */;
INSERT INTO `Cable` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','Custom Coiled (USB-C)',NULL,NULL,2,NULL,'coiled','usb_c',0.9,0.8,0.85,0.8,1),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','Straight Cable (USB-C)',NULL,NULL,2,NULL,'straight','usb_c',0.8,0.85,0.9,0.5,1),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','Magnetic Cable (USB-C)',NULL,NULL,1,NULL,'magnetic','usb_c',0.85,0.9,0.8,0.7,1);
/*!40000 ALTER TABLE `Cable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Case`
--

DROP TABLE IF EXISTS `Case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Case` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `size` int DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `case_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Case`
--

LOCK TABLES `Case` WRITE;
/*!40000 ALTER TABLE `Case` DISABLE KEYS */;
/*!40000 ALTER TABLE `Case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `components`
--

DROP TABLE IF EXISTS `components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `components` (
  `id` int NOT NULL AUTO_INCREMENT,
  `component_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `components`
--

LOCK TABLES `components` WRITE;
/*!40000 ALTER TABLE `components` DISABLE KEYS */;
/*!40000 ALTER TABLE `components` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `id` int NOT NULL AUTO_INCREMENT,
  `components` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `explanization` varchar(255) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extra`
--

DROP TABLE IF EXISTS `extra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extra` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime(6) DEFAULT NULL,
  `enddate` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `explanization` varchar(255) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `extra_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  KEY `FK3n473yib6kpnqet3uadj2wvws` (`extra_id`),
  CONSTRAINT `extra_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`),
  CONSTRAINT `FK3n473yib6kpnqet3uadj2wvws` FOREIGN KEY (`extra_id`) REFERENCES `part_recommendation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extra`
--

LOCK TABLES `extra` WRITE;
/*!40000 ALTER TABLE `extra` DISABLE KEYS */;
/*!40000 ALTER TABLE `extra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Foam`
--

DROP TABLE IF EXISTS `Foam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Foam` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `thickness` float DEFAULT NULL,
  `density` float DEFAULT NULL,
  `sound_dampening` float DEFAULT NULL,
  `compression` float DEFAULT NULL,
  `durability` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `foam_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Foam`
--

LOCK TABLES `Foam` WRITE;
/*!40000 ALTER TABLE `Foam` DISABLE KEYS */;
INSERT INTO `Foam` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','Poron Foam',NULL,NULL,NULL,'poron',0.8,0.7,0.8,0.7,0.9,0.7),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','PE Foam',NULL,NULL,NULL,'pe',0.6,0.5,0.7,0.8,0.7,0.3),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','Neoprene Foam',NULL,NULL,NULL,'neoprene',0.7,0.6,0.75,0.75,0.8,0.5),(4,'2025-05-27 23:28:13','2025-12-31 23:59:59','EVA Foam',NULL,NULL,NULL,'eva',0.65,0.55,0.65,0.7,0.75,0.4),(5,'2025-05-27 23:28:13','2025-12-31 23:59:59','Sorbothane',NULL,NULL,NULL,'sorbothane',0.9,0.9,0.95,0.6,0.85,0.8);
/*!40000 ALTER TABLE `Foam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Gasket`
--

DROP TABLE IF EXISTS `Gasket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Gasket` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `typing` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `thickness` float DEFAULT NULL,
  `flexibility` float DEFAULT NULL,
  `dampening` float DEFAULT NULL,
  `durability` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `gasket_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Gasket`
--

LOCK TABLES `Gasket` WRITE;
/*!40000 ALTER TABLE `Gasket` DISABLE KEYS */;
INSERT INTO `Gasket` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','Poron Gasket',NULL,'poron',NULL,NULL,0.7,0.8,0.75,0.85,0.6),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','Silicone Gasket',NULL,'silicone',NULL,NULL,0.75,0.9,0.8,0.9,0.7),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','EPDM Gasket',NULL,'epdm',NULL,NULL,0.8,0.85,0.85,0.9,0.75);
/*!40000 ALTER TABLE `Gasket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hardware_connector`
--

DROP TABLE IF EXISTS `hardware_connector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hardware_connector` (
  `id` int NOT NULL AUTO_INCREMENT,
  `component_id` int DEFAULT NULL,
  `enddate` datetime(6) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `startdate` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hardware_connector`
--

LOCK TABLES `hardware_connector` WRITE;
/*!40000 ALTER TABLE `hardware_connector` DISABLE KEYS */;
/*!40000 ALTER TABLE `hardware_connector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HardwareConnector`
--

DROP TABLE IF EXISTS `HardwareConnector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HardwareConnector` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `hardwareconnector_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HardwareConnector`
--

LOCK TABLES `HardwareConnector` WRITE;
/*!40000 ALTER TABLE `HardwareConnector` DISABLE KEYS */;
/*!40000 ALTER TABLE `HardwareConnector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keyboard_case`
--

DROP TABLE IF EXISTS `keyboard_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keyboard_case` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keyboard_case`
--

LOCK TABLES `keyboard_case` WRITE;
/*!40000 ALTER TABLE `keyboard_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `keyboard_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KeyboardCase`
--

DROP TABLE IF EXISTS `KeyboardCase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KeyboardCase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(100) DEFAULT NULL,
  `mounting` varchar(100) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `acoustics` float DEFAULT NULL,
  `build_quality` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `rgb_support` tinyint(1) DEFAULT NULL,
  `angle` int DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KeyboardCase`
--

LOCK TABLES `KeyboardCase` WRITE;
/*!40000 ALTER TABLE `KeyboardCase` DISABLE KEYS */;
INSERT INTO `KeyboardCase` VALUES (1,'Tofu60','aluminum','tray_mount',0.8,0.7,0.8,0.6,1,6,'60%'),(2,'KBD67 Lite','plastic','gasket_mount',0.4,0.8,0.7,0.4,1,6,'65%'),(3,'Mode80','aluminum','top_mount',0.9,0.9,0.95,0.9,1,7,'TKL'),(4,'GMMK Pro','aluminum','gasket_mount',0.75,0.75,0.8,0.7,1,6,'75%'),(5,'Keychron Q1','aluminum','gasket_mount',0.7,0.8,0.75,0.6,1,6,'75%'),(6,'Bakeneko60','aluminum','o_ring_mount',0.6,0.85,0.7,0.5,0,6,'60%'),(7,'NK65','aluminum','gasket_mount',0.65,0.75,0.75,0.6,1,6,'65%'),(8,'Tofu84','aluminum','tray_mount',0.8,0.7,0.8,0.65,1,6,'75%'),(9,'Frog TKL','aluminum','top_mount',0.85,0.9,0.9,0.85,0,7,'TKL'),(10,'D65','aluminum','gasket_mount',0.75,0.8,0.85,0.7,1,6,'65%');
/*!40000 ALTER TABLE `KeyboardCase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Keycap`
--

DROP TABLE IF EXISTS `Keycap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Keycap` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `thickness` varchar(50) DEFAULT NULL,
  `profile` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `switch_compatibility` varchar(100) DEFAULT NULL,
  `sound_profile` float DEFAULT NULL,
  `build_quality` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `rgb_compatible` tinyint(1) DEFAULT NULL,
  `durability` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `keycap_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Keycap`
--

LOCK TABLES `Keycap` WRITE;
/*!40000 ALTER TABLE `Keycap` DISABLE KEYS */;
INSERT INTO `Keycap` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','GMK (ABS)','https://divinikey.com/products/gmk-keycaps','ABS','0.85','cherry',NULL,NULL,0.7,0.9,3,1,0.7),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','ePBT (PBT)','https://swagkeys.com/products/epbt-keycaps','PBT','0.8','cherry',NULL,NULL,0.8,0.85,1,1,0.9),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','MT3 (PBT)','https://drop.com/buy/drop-mt3-black-on-white-keycap-set','PBT','0.9','mt3',NULL,NULL,0.85,0.9,3,0,0.9),(4,'2025-05-27 23:28:13','2025-12-31 23:59:59','SA (ABS)','https://kbdfans.com/collections/sa-profile/products/sa-profile-dye-sub-keycaps','ABS','0.8','sa',NULL,NULL,0.75,0.85,3,1,0.7),(5,'2025-05-27 23:28:13','2025-12-31 23:59:59','KAT (PBT)','https://kbdfans.com/collections/keycaps','PBT','0.85','kat',NULL,NULL,0.8,0.85,2,1,0.85),(6,'2025-05-28 13:39:13','2025-12-31 23:59:59','GMK Red Samurai','https://drop.com/buy/gmk-red-samurai-keycap-set','ABS','1.5','Cherry',NULL,NULL,8,9,3,1,7.5),(7,'2025-05-28 13:39:13','2025-12-31 23:59:59','ePBT Black Japanese','https://kbdfans.com/products/epbt-black-japanese-keycaps-set','PBT','1.4','Cherry',NULL,NULL,7.5,8.5,2,1,9),(8,'2025-05-28 13:39:13','2025-12-31 23:59:59','MT3 Susuwatari','https://drop.com/buy/drop-mt3-susuwatari-custom-keycap-set','ABS','1.6','MT3',NULL,NULL,8.5,9,3,0,8),(9,'2025-05-28 13:39:13','2025-12-31 23:59:59','SA Laser','https://drop.com/buy/drop-sa-laser-keycap-set','ABS','1.5','SA',NULL,NULL,9,8.5,3,1,7.5),(10,'2025-05-28 13:39:13','2025-12-31 23:59:59','NP PBT Crayon','https://kbdfans.com/products/np-pbt-keycaps-set','PBT','1.4','NP',NULL,NULL,7,8,1,1,8.5),(11,'2025-05-28 13:39:13','2025-12-31 23:59:59','KAT Arctic','https://candykeys.com/product/kat-arctic','PBT','1.6','KAT',NULL,NULL,8,8.5,2,1,9),(12,'2025-05-28 13:39:13','2025-12-31 23:59:59','DSA Granite','https://pimpmykeyboard.com/dsa-granite-keyset/','PBT','1.4','DSA',NULL,NULL,7.5,8,2,1,8.5),(13,'2025-05-28 13:39:13','2025-12-31 23:59:59','GMK Olivia','https://novelkeys.com/products/gmk-olivia','ABS','1.5','Cherry',NULL,NULL,8,9,3,1,7.5),(14,'2025-05-28 13:39:13','2025-12-31 23:59:59','XDA Canvas','https://drop.com/buy/xda-canvas-keycap-set','PBT','1.4','XDA',NULL,NULL,7,8,2,1,8.5),(15,'2025-05-28 13:39:13','2025-12-31 23:59:59','OEM Profile PBT','https://kbdfans.com/collections/keycaps','PBT','1.3','OEM',NULL,NULL,6.5,7.5,1,1,8);
/*!40000 ALTER TABLE `Keycap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Lube`
--

DROP TABLE IF EXISTS `Lube`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Lube` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `viscosity` varchar(50) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `lube_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Lube`
--

LOCK TABLES `Lube` WRITE;
/*!40000 ALTER TABLE `Lube` DISABLE KEYS */;
/*!40000 ALTER TABLE `Lube` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `part_recommendation`
--

DROP TABLE IF EXISTS `part_recommendation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `part_recommendation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ai_model_version` varchar(255) DEFAULT NULL,
  `backlight_required` bit(1) NOT NULL,
  `budget` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `lube_required` bit(1) NOT NULL,
  `preferred_sound` varchar(255) NOT NULL,
  `purpose` varchar(255) NOT NULL,
  `recommendation_reason` varchar(1000) DEFAULT NULL,
  `recommendation_score` double DEFAULT NULL,
  `switch_type` varchar(255) NOT NULL,
  `cable_id` int DEFAULT NULL,
  `keyboard_case_id` int DEFAULT NULL,
  `foam_id` int DEFAULT NULL,
  `gasket_id` int DEFAULT NULL,
  `hardware_connector_id` int DEFAULT NULL,
  `keycap_id` int DEFAULT NULL,
  `lube_id` int DEFAULT NULL,
  `pcb_id` int DEFAULT NULL,
  `plate_id` int DEFAULT NULL,
  `sound_dampener_id` int DEFAULT NULL,
  `stabilizer_id` int DEFAULT NULL,
  `switch_id` int DEFAULT NULL,
  `weight_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKscyi6mnawo7c5clwh6k93xext` (`cable_id`),
  KEY `FKlbjpv38shw66aqa2gg8tx06kw` (`keyboard_case_id`),
  KEY `FK8bv8l4exkeukcdpitefrcs4tg` (`foam_id`),
  KEY `FK400crghi6haiy6no71xcq9wxs` (`gasket_id`),
  KEY `FK7r6vn08aive5fvvgiakrj37e9` (`hardware_connector_id`),
  KEY `FK90fktbvuk27mt5icxaavm3qy` (`keycap_id`),
  KEY `FKjvc1lwdurl8nwmo8sbtxwqvup` (`lube_id`),
  KEY `FK388ptq455y5ynop3gxvfn1gd1` (`pcb_id`),
  KEY `FKbvp4gtuiispnvfsefh80vx3tb` (`plate_id`),
  KEY `FKqs5a3tmaxrhv1h8fgrple1dg3` (`sound_dampener_id`),
  KEY `FK7o4s4404vpukabg639a0m5f8i` (`stabilizer_id`),
  KEY `FKphab5shdb4twajg858fvlrgyl` (`switch_id`),
  KEY `FKre2idh2e3688e9cj5dm8l0xp6` (`weight_id`),
  KEY `FK6p2adp2mmp9i63euesmh831o7` (`user_id`),
  CONSTRAINT `FK388ptq455y5ynop3gxvfn1gd1` FOREIGN KEY (`pcb_id`) REFERENCES `pcb` (`id`),
  CONSTRAINT `FK400crghi6haiy6no71xcq9wxs` FOREIGN KEY (`gasket_id`) REFERENCES `gasket` (`id`),
  CONSTRAINT `FK6p2adp2mmp9i63euesmh831o7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK7o4s4404vpukabg639a0m5f8i` FOREIGN KEY (`stabilizer_id`) REFERENCES `stabilizer` (`id`),
  CONSTRAINT `FK7r6vn08aive5fvvgiakrj37e9` FOREIGN KEY (`hardware_connector_id`) REFERENCES `hardware_connector` (`id`),
  CONSTRAINT `FK8bv8l4exkeukcdpitefrcs4tg` FOREIGN KEY (`foam_id`) REFERENCES `foam` (`id`),
  CONSTRAINT `FK90fktbvuk27mt5icxaavm3qy` FOREIGN KEY (`keycap_id`) REFERENCES `keycap` (`id`),
  CONSTRAINT `FKbvp4gtuiispnvfsefh80vx3tb` FOREIGN KEY (`plate_id`) REFERENCES `plate` (`id`),
  CONSTRAINT `FKjvc1lwdurl8nwmo8sbtxwqvup` FOREIGN KEY (`lube_id`) REFERENCES `lube` (`id`),
  CONSTRAINT `FKlbjpv38shw66aqa2gg8tx06kw` FOREIGN KEY (`keyboard_case_id`) REFERENCES `keyboard_case` (`id`),
  CONSTRAINT `FKphab5shdb4twajg858fvlrgyl` FOREIGN KEY (`switch_id`) REFERENCES `switches` (`id`),
  CONSTRAINT `FKqs5a3tmaxrhv1h8fgrple1dg3` FOREIGN KEY (`sound_dampener_id`) REFERENCES `sound_dampener` (`id`),
  CONSTRAINT `FKre2idh2e3688e9cj5dm8l0xp6` FOREIGN KEY (`weight_id`) REFERENCES `weight` (`id`),
  CONSTRAINT `FKscyi6mnawo7c5clwh6k93xext` FOREIGN KEY (`cable_id`) REFERENCES `cable` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `part_recommendation`
--

LOCK TABLES `part_recommendation` WRITE;
/*!40000 ALTER TABLE `part_recommendation` DISABLE KEYS */;
/*!40000 ALTER TABLE `part_recommendation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PCB`
--

DROP TABLE IF EXISTS `PCB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PCB` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `layout` varchar(100) DEFAULT NULL,
  `hotswap` tinyint(1) DEFAULT NULL,
  `wireless` tinyint(1) DEFAULT NULL,
  `rgb` varchar(50) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `case_Compatibility` int DEFAULT NULL,
  `usb_type` varchar(50) DEFAULT NULL,
  `firmware_type` varchar(50) DEFAULT NULL,
  `rgb_support` tinyint(1) DEFAULT NULL,
  `qmk_via` tinyint(1) DEFAULT NULL,
  `flex` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `build_quality` float DEFAULT NULL,
  `features` text,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  KEY `case_Compatibility` (`case_Compatibility`),
  CONSTRAINT `pcb_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`),
  CONSTRAINT `pcb_ibfk_2` FOREIGN KEY (`case_Compatibility`) REFERENCES `Case` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PCB`
--

LOCK TABLES `PCB` WRITE;
/*!40000 ALTER TABLE `PCB` DISABLE KEYS */;
INSERT INTO `PCB` VALUES (21,'2024-03-20 00:00:00','2025-12-31 00:00:00','KBD75 V3.1','https://kbdfans.com/collections/75-pcb/products/kbd75-v3-1-pcb',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7.5,2,8.5,'Hot-swap, USB Type-C, QMK/VIA support, RGB underglow'),(22,'2024-03-20 00:00:00','2025-12-31 00:00:00','DZ60RGB V2','https://kbdfans.com/collections/60-pcb/products/dz60rgb-v2-hot-swap-mechanical-keyboard-pcb',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7,2,8,'Hot-swap, USB Type-C, QMK/VIA support, Per-key RGB'),(23,'2024-03-20 00:00:00','2025-12-31 00:00:00','NK65 Entry Edition','https://novelkeys.com/products/nk65-entry-edition',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,6.5,1,7.5,'Hot-swap, USB Type-C, QMK/VIA support, RGB underglow'),(24,'2024-03-20 00:00:00','2025-12-31 00:00:00','GMMK Pro','https://www.pcgamingrace.com/products/gmmk-pro-75-barebone',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,8,3,9,'Hot-swap, USB Type-C, QMK/VIA support, Rotary encoder, RGB'),(25,'2024-03-20 00:00:00','2025-12-31 00:00:00','Tofu60 DZ60RGB','https://kbdfans.com/collections/60-pcb/products/tofu60-dz60rgb-v2-hot-swap-mechanical-keyboard-diy-kit',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7,2,8,'Hot-swap, USB Type-C, QMK/VIA support, RGB underglow'),(26,'2024-03-20 00:00:00','2025-12-31 00:00:00','Bakeneko60','https://cannonkeys.com/products/bakeneko60',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7.5,2,8.5,'Hot-swap, USB Type-C, QMK/VIA support, Unified Daughterboard'),(27,'2024-03-20 00:00:00','2025-12-31 00:00:00','KBD67 Lite R4','https://kbdfans.com/products/kbd67-lite-r4',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7,2,8.5,'Hot-swap, USB Type-C, QMK/VIA support, RGB underglow'),(28,'2024-03-20 00:00:00','2025-12-31 00:00:00','Zoom65','https://meletrix.com/products/zoom65',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,8,3,9,'Hot-swap, USB Type-C, QMK/VIA support, RGB, Knob option'),(29,'2024-03-20 00:00:00','2025-12-31 00:00:00','NK87 Aluminum Edition','https://novelkeys.com/products/nk87-aluminum-edition',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,7.5,3,8.5,'Hot-swap, USB Type-C, QMK/VIA support, RGB, Premium build'),(30,'2024-03-20 00:00:00','2025-12-31 00:00:00','Mode Eighty','https://modedesigns.com/products/eighty',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,8.5,4,9.5,'Hot-swap, USB Type-C, QMK/VIA support, Premium materials, Flex cuts');
/*!40000 ALTER TABLE `PCB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Plate`
--

DROP TABLE IF EXISTS `Plate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Plate` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `size` int DEFAULT NULL,
  `Compatibility` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `case_Compatibility` int DEFAULT NULL,
  `flex_level` int DEFAULT NULL,
  `thickness` decimal(5,2) DEFAULT NULL,
  `stiffness` float DEFAULT NULL,
  `sound_profile` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `flex` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  KEY `case_Compatibility` (`case_Compatibility`),
  CONSTRAINT `plate_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`),
  CONSTRAINT `plate_ibfk_2` FOREIGN KEY (`case_Compatibility`) REFERENCES `Case` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Plate`
--

LOCK TABLES `Plate` WRITE;
/*!40000 ALTER TABLE `Plate` DISABLE KEYS */;
INSERT INTO `Plate` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','Aluminum Plate','https://kbdfans.com/collections/65-layout-plate/products/65-cnc-aluminum-plate','Aluminum',60,NULL,NULL,NULL,4,1.50,0.8,0.6,2,250,0.3),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','Fr4 Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Fr4',60,NULL,NULL,NULL,5,1.50,0.6,0.7,2,200,0.6),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','Pom Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Pom',60,NULL,NULL,NULL,5,1.50,0.4,0.8,2,200,0.7),(4,'2025-05-27 23:28:13','2025-12-31 23:59:59','Brass Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Brass',60,NULL,NULL,NULL,2,1.60,0.9,0.7,3,350,0.2),(5,'2025-05-27 23:28:13','2025-12-31 23:59:59','Carbon Fiber Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Carbon_fiber',60,NULL,NULL,NULL,5,1.50,0.7,0.6,3,200,0.5),(6,'2025-05-27 23:28:13','2025-12-31 23:59:59','Polycarbonate Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Polycarbonate',60,NULL,NULL,NULL,8,1.50,0.5,0.8,2,150,0.7),(7,'2025-05-27 23:28:13','2025-12-31 23:59:59','Steel Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Steel',60,NULL,NULL,NULL,5,1.50,0.95,0.5,2,200,0.1),(8,'2025-05-27 23:28:13','2025-12-31 23:59:59','Copper Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Copper',60,NULL,NULL,NULL,5,1.50,0.85,0.75,4,200,0.25),(9,'2025-05-27 23:28:13','2025-12-31 23:59:59','Abs Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Abs',60,NULL,NULL,NULL,5,1.50,0.4,0.6,1,200,0.8),(10,'2025-05-27 23:28:13','2025-12-31 23:59:59','Petg Universal','https://cannonkeys.com/collections/plates/products/universal-60-plate','Petg',60,NULL,NULL,NULL,5,1.50,0.45,0.7,2,200,0.75),(11,'2025-05-28 13:38:31','2025-12-31 23:59:59','Aluminum 60%','https://kbdfans.com/collections/60-layout-plate/products/60-aluminum-plate','Aluminum',60,NULL,NULL,NULL,4,1.50,8.5,7,2,250,3),(12,'2025-05-28 13:38:31','2025-12-31 23:59:59','Brass 60%','https://kbdfans.com/collections/60-layout-plate/products/60-brass-plate','Brass',60,NULL,NULL,NULL,2,1.60,9.5,8.5,3,350,2),(13,'2025-05-28 13:38:31','2025-12-31 23:59:59','FR4 60%','https://keebsforall.com/collections/keyboard-plates/products/fr4-60-plate','Fr4',60,NULL,NULL,NULL,5,1.50,6,7.5,1,200,7),(14,'2025-05-28 13:38:31','2025-12-31 23:59:59','Carbon Fiber 60%','https://divinikey.com/products/carbon-fiber-plate','Carbon fiber',60,NULL,NULL,NULL,6,1.60,7.5,6.5,3,200,5),(15,'2025-05-28 13:38:31','2025-12-31 23:59:59','POM 60%','https://monstargears.com/products/pom-plate','Pom',60,NULL,NULL,NULL,5,1.50,5,8,2,200,6.5),(16,'2025-05-28 13:38:31','2025-12-31 23:59:59','Polycarbonate 60%','https://kbdfans.com/collections/65-layout-plate/products/65-polycarbonate-plate','Polycarbonate',60,NULL,NULL,NULL,8,1.50,4.5,8.5,2,150,7.5),(17,'2025-05-28 13:38:31','2025-12-31 23:59:59','Steel 60%','https://kbdfans.com/collections/plate','Steel',60,NULL,NULL,NULL,5,1.50,9,7.5,2,200,2.5),(18,'2025-05-28 13:38:31','2025-12-31 23:59:59','Aluminum 65%','https://kbdfans.com/collections/65-layout-plate/products/65-aluminum-plate','Aluminum',65,NULL,NULL,NULL,4,1.50,8.5,7,2,250,3),(19,'2025-05-28 13:38:31','2025-12-31 23:59:59','FR4 75%','https://keebsforall.com/collections/keyboard-plates/products/fr4-75-plate','Fr4',75,NULL,NULL,NULL,5,1.50,6,7.5,1,200,7),(20,'2025-05-28 13:38:31','2025-12-31 23:59:59','Brass 75%','https://kbdfans.com/collections/75-layout-plate/products/75-brass-plate','Brass',75,NULL,NULL,NULL,2,1.60,9.5,8.5,3,350,2);
/*!40000 ALTER TABLE `Plate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitepage`
--

DROP TABLE IF EXISTS `sitepage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitepage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `details` text,
  `product` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitepage`
--

LOCK TABLES `sitepage` WRITE;
/*!40000 ALTER TABLE `sitepage` DISABLE KEYS */;
/*!40000 ALTER TABLE `sitepage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sound_dampener`
--

DROP TABLE IF EXISTS `sound_dampener`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sound_dampener` (
  `id` int NOT NULL AUTO_INCREMENT,
  `component_id` int DEFAULT NULL,
  `enddate` datetime(6) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `size` varchar(50) DEFAULT NULL,
  `startdate` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sound_dampener`
--

LOCK TABLES `sound_dampener` WRITE;
/*!40000 ALTER TABLE `sound_dampener` DISABLE KEYS */;
/*!40000 ALTER TABLE `sound_dampener` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SoundDampener`
--

DROP TABLE IF EXISTS `SoundDampener`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SoundDampener` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `sounddampener_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SoundDampener`
--

LOCK TABLES `SoundDampener` WRITE;
/*!40000 ALTER TABLE `SoundDampener` DISABLE KEYS */;
/*!40000 ALTER TABLE `SoundDampener` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stabilizer`
--

DROP TABLE IF EXISTS `Stabilizer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Stabilizer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `rattle` float DEFAULT NULL,
  `smoothness` float DEFAULT NULL,
  `sound_profile` float DEFAULT NULL,
  `price_tier` float DEFAULT NULL,
  `build_quality` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `stabilizer_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stabilizer`
--

LOCK TABLES `Stabilizer` WRITE;
/*!40000 ALTER TABLE `Stabilizer` DISABLE KEYS */;
INSERT INTO `Stabilizer` VALUES (1,'2025-05-27 23:28:13','2025-12-31 23:59:59','Durock V2','https://divinikey.com/products/durock-v2-stabilizers',NULL,NULL,NULL,'screw_in',0.2,0.9,0.8,3,0.9),(2,'2025-05-27 23:28:13','2025-12-31 23:59:59','Cherry Clip-in','https://divinikey.com/products/cherry-clip-in-stabilizers',NULL,NULL,NULL,'clip_in',0.6,0.6,0.5,1,0.6),(3,'2025-05-27 23:28:13','2025-12-31 23:59:59','C³Equalz Screw-in','https://divinikey.com/products/c3-equalz-stabilizers',NULL,NULL,NULL,'screw_in',0.25,0.85,0.8,2,0.85),(4,'2025-05-27 23:28:13','2025-12-31 23:59:59','Zeal Screw-in','https://zealpc.net/products/zealstabilizers',NULL,NULL,NULL,'screw_in',0.2,0.9,0.85,4,0.95),(5,'2025-05-27 23:28:13','2025-12-31 23:59:59','GMK Screw-in','https://omnitype.com/products/gmk-stabilizers',NULL,NULL,NULL,'screw_in',0.3,0.8,0.75,2,0.8),(6,'2025-05-27 23:28:13','2025-12-31 23:59:59','Everglide Panda','https://kprepublic.com/products/everglide-panda-gold-plated-stabilizer',NULL,NULL,NULL,'screw_in',0.25,0.85,0.8,2,0.85),(7,'2025-05-27 23:28:13','2025-12-31 23:59:59','GOAT Stabilizers','https://kbdfans.com/collections/keyboard-stabilizer',NULL,NULL,NULL,'clip_in',0.4,0.7,0.7,1,0.75),(8,'2025-05-27 23:28:13','2025-12-31 23:59:59','NK Stabilizers','https://novelkeys.com/products/nk-stabilizers',NULL,NULL,NULL,'screw_in',0.3,0.8,0.75,1,0.8),(9,'2025-05-27 23:28:13','2025-12-31 23:59:59','Gateron Screw-in','https://kbdfans.com/collections/keyboard-stabilizer',NULL,NULL,NULL,'screw_in',0.35,0.75,0.7,1,0.75),(10,'2025-05-27 23:28:13','2025-12-31 23:59:59','Durock Piano','https://divinikey.com/products/durock-piano-stabilizers',NULL,NULL,NULL,'screw_in',0.15,0.95,0.9,4,0.95),(12,'2024-03-20 00:00:00','2025-12-31 00:00:00','C³Equalz X TKC','https://keebsforall.com/products/c3-equalz-stabilizers','POM','Standard',NULL,'Screw-in',8,8.5,8,2,8.5),(13,'2024-03-20 00:00:00','2025-12-31 00:00:00','TX Stabilizers Rev. 3','https://store.txkeyboards.com/products/tx-stabilizers','POM','Standard',NULL,'Screw-in',9,9,9,3,9.5),(14,'2024-03-20 00:00:00','2025-12-31 00:00:00','Cherry Original','https://divinikey.com/products/original-cherry-stabilizers','ABS','Standard',NULL,'Clip-in',6,6.5,6,1,7),(15,'2024-03-20 00:00:00','2025-12-31 00:00:00','Owlab V2','https://kbdfans.com/collections/keyboard-stabilizer','POM','Standard',NULL,'Screw-in',8.5,8.5,8.5,2,8.5),(16,'2024-03-20 00:00:00','2025-12-31 00:00:00','Staebies V2','https://cannonkeys.com/products/staebies-stabilizers-v2','POM','Standard',NULL,'Screw-in',9,9,8.5,3,9),(17,'2024-03-20 00:00:00','2025-12-31 00:00:00','WS Aurora','https://kbdfans.com/collections/keyboard-stabilizer','POM','Standard',NULL,'Plate-mount',7,7.5,7,1,7.5),(19,'2024-03-20 00:00:00','2025-12-31 00:00:00','Zeal V2','https://zealpc.net/products/zealstabilizers','POM','Standard',NULL,'Screw-in',8.5,9,8.5,4,9),(20,'2024-03-20 00:00:00','2025-12-31 00:00:00','NovelKeys','https://kbdfans.com/collections/keyboard-stabilizer','POM','Standard',NULL,'Screw-in',7.5,8,7.5,2,8);
/*!40000 ALTER TABLE `Stabilizer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Switches`
--

DROP TABLE IF EXISTS `Switches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Switches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `pressure` int DEFAULT NULL,
  `lubrication` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  `stem_material` varchar(100) DEFAULT NULL,
  `linear_score` float DEFAULT NULL,
  `tactile_score` float DEFAULT NULL,
  `sound_score` float DEFAULT NULL,
  `weight_score` float DEFAULT NULL,
  `smoothness_score` float DEFAULT NULL,
  `speed_score` float DEFAULT NULL,
  `stability_score` float DEFAULT NULL,
  `durability_score` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `switches_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Switches`
--

LOCK TABLES `Switches` WRITE;
/*!40000 ALTER TABLE `Switches` DISABLE KEYS */;
INSERT INTO `Switches` VALUES (1,'2025-05-28 13:32:38','2025-12-31 23:59:59','Cherry MX Red','https://swagkeys.com/products/cherry-mx-red-switches','Linear',NULL,NULL,NULL,'POM',9.5,2,6.5,7,8,9,8.5,9),(2,'2025-05-28 13:32:38','2025-12-31 23:59:59','Cherry MX Brown','https://swagkeys.com/products/cherry-mx-brown-switches','Tactile',NULL,NULL,NULL,'POM',3,8.5,7,7,7.5,8,8.5,9),(3,'2025-05-28 13:32:38','2025-12-31 23:59:59','Gateron Yellow','https://swagkeys.com/products/gateron-yellow-switches','Linear',NULL,NULL,NULL,'POM',9,2,7,7.5,8.5,8.5,8,8.5),(4,'2025-05-28 13:32:38','2025-12-31 23:59:59','Kailh Box White','https://swagkeys.com/products/kailh-box-white-switches','Clicky',NULL,NULL,NULL,'POM',2,8,9.5,7,7.5,7.5,8.5,8.5),(5,'2025-05-28 13:32:38','2025-12-31 23:59:59','Zealios V2 67g','https://zealpc.net/products/zealio-v2','Tactile',NULL,NULL,NULL,'POM',2.5,9.5,7.5,8,9,7.5,9,8.5),(6,'2025-05-28 13:32:38','2025-12-31 23:59:59','Alpaca Linear','https://kinetic.works/products/alpaca-linears','Linear',NULL,NULL,NULL,'POM',9.5,1.5,8.5,7.5,9.5,9,8.5,8),(7,'2025-05-28 13:32:38','2025-12-31 23:59:59','Holy Panda','https://drop.com/buy/drop-invyr-holy-panda-mechanical-switches','Tactile',NULL,NULL,NULL,'POM',2,9.5,8.5,8,8.5,7,8.5,8),(8,'2025-05-28 13:32:38','2025-12-31 23:59:59','Tangerine 67g','https://thekey.company/products/c3-tangerine-switches-r2','Linear',NULL,NULL,NULL,'POM',9.5,1.5,8,8,9.5,9,8.5,8.5),(9,'2025-05-28 13:32:38','2025-12-31 23:59:59','NK Cream','https://novelkeys.com/products/cream-switches','Linear',NULL,NULL,NULL,'POM',9,2,9,7.5,8.5,8.5,9,8.5),(10,'2025-05-28 13:32:38','2025-12-31 23:59:59','Boba U4T','https://gazzew.com/products/boba-u4t-switches','Tactile',NULL,NULL,NULL,'POM',2,9,8.5,7.5,8.5,7.5,9,9),(11,'2025-05-28 13:35:28','2025-12-31 23:59:59','Gateron KS-3 Linear','https://www.gateron.co/products/gateron-ks-3-linear-switch','Magnetic',45,NULL,NULL,'POM',9.5,1.5,8.5,7,9.5,9.5,9,9.5),(12,'2025-05-28 13:35:28','2025-12-31 23:59:59','Gateron KS-3X47 Linear','https://www.gateron.co/products/gateron-ks-3x47-linear-switch','Magnetic',47,NULL,NULL,'POM',9.5,1.5,8,7.5,9.5,9.5,9,9.5),(13,'2025-05-28 13:35:28','2025-12-31 23:59:59','Varmilo EC Rose V2','https://en.varmilo.com/keyboardproscenium/product_indexsubject?layout=80&model=Theme%20keyboard','Magnetic',45,NULL,NULL,'POM',9,2,8.5,7,9,9,9,9.5),(14,'2025-05-28 13:35:28','2025-12-31 23:59:59','Varmilo EC Sakura V2','https://en.varmilo.com/keyboardproscenium/product_indexsubject?layout=80&model=Theme%20keyboard','Magnetic',35,NULL,NULL,'POM',9,2,8,6.5,9,9,9,9.5),(15,'2025-05-28 13:35:28','2025-12-31 23:59:59','Gateron Jade','https://www.gateron.co/products/gateron-jade-switch','Clicky',50,NULL,NULL,'POM',2,8.5,9.5,7.5,8,7.5,8.5,8.5);
/*!40000 ALTER TABLE `Switches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_rank` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Weight`
--

DROP TABLE IF EXISTS `Weight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Weight` (
  `id` int NOT NULL AUTO_INCREMENT,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `material` varchar(100) DEFAULT NULL,
  `component_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `component_id` (`component_id`),
  CONSTRAINT `weight_ibfk_1` FOREIGN KEY (`component_id`) REFERENCES `components` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Weight`
--

LOCK TABLES `Weight` WRITE;
/*!40000 ALTER TABLE `Weight` DISABLE KEYS */;
/*!40000 ALTER TABLE `Weight` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10 20:42:05
