INSERT INTO `user` VALUES (1,'D033E22AE348AEB5660FC2140AEC35850C4DA997','admin'),(26,'7110EDA4D09E062AA5E4A390B0A572AC0D2C0220','cash1');
INSERT INTO `permission` VALUES (1,'CREATE_USER','for create/update/delete account; for change account\'s permission',2),(2,'EDIT_MENU','add/update/delete menu(including category1/category2/dish/dishconfiggroup/dishconfig), set dish SOLDOUT/PROMOTION',10),(3,'QUERY_ORDER','query order data',20),(4,'QUERY_USER','query account',1),(5,'CHANGE_CONFIG','change the configurations of system',3),(6,'QUERY_DESK','query desk data',4),(7,'EDIT_DESK','create/update/delete desk data',5),(8,'EDIT_PRINTER','create/update/delete printer data',8),(9,'UPDATE_ORDER','create/update/delete the order, including add dish, cancel order, checkout order, etc',21),(10,'EDIT_DISCOUNTTEMPLATE','add/update/delete the discount template',7),(11,'QUERY_SHIFTWORK','query shift work data',50),(12,'EDIT_PAYWAY','add/update/delete the payway for checkout order',6),(13,'STATISTICS','statistics the income report',51),(14,'RAWMATERIAL','raw material',12),(15,'QUERY_MEMBER','query member data, including query the member\'s score log and balance log',40),(16,'UPDATE_MEMBER','create/update member, not including update the member\'s score & balance',41),(17,'UPDATE_MEMBERSCORE','update member\'s score',42),(18,'UPDATE_MEMBERBALANCE','update member\'s balance, including recharge',43),(19,'UPDATE_MEMBERPASSWORD','update member\'s password',44),(20,'DELETE_MEMBER','delete member',45);
INSERT INTO `user_permission` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(25,5,1),(26,6,1),(27,7,1),(28,8,1),(29,9,1),(34,10,1),(35,11,1),(62,12,1),(74,13,1),(75,14,1),(76,2,26),(77,3,26),(78,4,26),(79,6,26),(80,9,26),(81,11,26),(82,15,1),(83,16,1),(84,17,1),(85,18,1),(86,19,1),(131,20,1);



