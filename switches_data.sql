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
