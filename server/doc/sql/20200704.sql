ALTER TABLE `digitalmenu`.`indentdetail`
ADD COLUMN `isReady` TINYINT(1) NULL DEFAULT 0 COMMENT '菜是否已上桌' AFTER `operator`;
