CREATE DATABASE `digitalmenu`;

USE `digitalmenu`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hashed_password` varchar(40) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  KEY `username_idx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'D033E22AE348AEB5660FC2140AEC35850C4DA997','admin'),(26,'7110EDA4D09E062AA5E4A390B0A572AC0D2C0220','cash1');
UNLOCK TABLES;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(400) DEFAULT NULL,
  `sequence` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

LOCK TABLES `permission` WRITE;
INSERT INTO `permission` VALUES (1,'CREATE_USER','for create/update/delete account; for change account\'s permission',2),(2,'EDIT_MENU','add/update/delete menu(including category1/category2/dish/dishconfiggroup/dishconfig), set dish SOLDOUT/PROMOTION',10),(3,'QUERY_ORDER','query order data',20),(4,'QUERY_USER','query account',1),(5,'CHANGE_CONFIG','change the configurations of system',3),(6,'QUERY_DESK','query desk data',4),(7,'EDIT_DESK','create/update/delete desk data',5),(8,'EDIT_PRINTER','create/update/delete printer data',8),(9,'UPDATE_ORDER','create/update/delete the order, including add dish, cancel order, checkout order, etc',21),(10,'EDIT_DISCOUNTTEMPLATE','add/update/delete the discount template',7),(11,'QUERY_SHIFTWORK','query shift work data',50),(12,'EDIT_PAYWAY','add/update/delete the payway for checkout order',6),(13,'STATISTICS','statistics the income report',51),(14,'RAWMATERIAL','raw material',12),(15,'QUERY_MEMBER','query member data, including query the member\'s score log and balance log',40),(16,'UPDATE_MEMBER','create/update member, not including update the member\'s score & balance',41),(17,'UPDATE_MEMBERSCORE','update member\'s score',42),(18,'UPDATE_MEMBERBALANCE','update member\'s balance, including recharge',43),(19,'UPDATE_MEMBERPASSWORD','update member\'s password',44),(20,'DELETE_MEMBER','delete member',45);
UNLOCK TABLES;

DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k6j8r050y1kxdu3rjg4ji6a1y` (`permission_id`),
  KEY `FK_hsesj1sxjqummghhxb5ayo4os` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;

LOCK TABLES `user_permission` WRITE;
INSERT INTO `user_permission` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(25,5,1),(26,6,1),(27,7,1),(28,8,1),(29,9,1),(34,10,1),(35,11,1),(62,12,1),(74,13,1),(75,14,1),(76,2,26),(77,3,26),(78,4,26),(79,6,26),(80,9,26),(81,11,26),(82,15,1),(83,16,1),(84,17,1),(85,18,1),(86,19,1),(87,2,28),(107,2,30),(108,3,30),(109,9,30),(110,13,30),(111,15,30),(112,16,30),(113,2,27),(114,3,27),(115,4,27),(116,6,27),(117,8,27),(118,9,27),(119,11,27),(120,12,27),(121,13,27),(122,14,27),(123,15,27),(124,16,27),(125,2,31),(126,3,31),(127,9,31),(128,13,31),(129,15,31),(130,16,31),(131,20,1),(132,4,29),(133,1,29),(134,5,29),(135,6,29),(136,7,29),(137,12,29),(138,10,29),(139,8,29),(140,2,29),(141,14,29),(142,3,29),(143,9,29),(144,15,29),(145,16,29),(146,17,29),(147,18,29),(148,19,29),(149,20,29),(150,11,29),(151,13,29);
UNLOCK TABLES;
