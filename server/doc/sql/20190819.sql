INSERT INTO `digitalmenu`.`configs` (`name`, `value`) VALUES ('REFUNDCODE', '111');
ALTER TABLE `digitalmenu`.`discounttemplate`
CHANGE COLUMN `rate` `rate` DOUBLE NOT NULL DEFAULT 0 ,
CHANGE COLUMN `value` `value` DOUBLE NOT NULL DEFAULT 0 ;
