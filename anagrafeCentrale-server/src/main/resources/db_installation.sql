-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'User'
-- contains all data related to the users
-- ---

DROP TABLE IF EXISTS `User`;
		
CREATE TABLE `User` (
  `id` VARCHAR(20) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `surname` VARCHAR(40) NOT NULL,
  `tax_id_code` VARCHAR(20) NOT NULL,
  `birthdate` DATE NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `active` bit NOT NULL DEFAULT true,
  `authorization` VARCHAR(6) NOT NULL DEFAULT '000000' COMMENT 'Campo utile a gestire le autorizzazioni di accesso ai serviz',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`tax_id_code`)
) COMMENT 'contains all data related to the users';

-- ---
-- Table 'Report'
-- contiene i referti ospedalieri
-- ---

DROP TABLE IF EXISTS `Report`;
		
CREATE TABLE `Report` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(20) NOT NULL,
  `portal_type` INTEGER NOT NULL,
  `file_path` VARCHAR(100) NOT NULL,
  `file_display_name` VARCHAR(50) NOT NULL,
  `file_title` VARCHAR(50) NULL,
  `file_content` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`)
) COMMENT 'contiene i referti ospedalieri';

-- ---
-- Table 'Request'
-- tabella che gestisce le richieste da parte degli utenti
-- ---

DROP TABLE IF EXISTS `Request`;
		
CREATE TABLE `Request` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `portal_type` INTEGER NOT NULL,
  `creator_user_id` VARCHAR(20) NOT NULL,
  `manager_user_id` VARCHAR(20) NULL,
  `request_name` VARCHAR(50) NOT NULL,
  `request_description` VARCHAR(200) NULL DEFAULT NULL,
  `request_type` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`)
) COMMENT 'tabella che gestisce le richieste da parte degli utenti';

-- ---
-- Table 'Notification'
-- tabella per la gestione delle notifiche
-- ---

DROP TABLE IF EXISTS `Notification`;
		
CREATE TABLE `Notification` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `portal_type` INTEGER NOT NULL,
  `request_id` INTEGER NULL DEFAULT NULL,
  `report_id` INTEGER NULL DEFAULT NULL,
  `notification_name` VARCHAR(50) NOT NULL,
  `notification_description` VARCHAR(200) NULL DEFAULT NULL,
  `notification_type` VARCHAR(10) NOT NULL,
  `unread` bit NOT NULL DEFAULT true,
  PRIMARY KEY (`id`)
) COMMENT 'tabella per la gestione delle notifiche';

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `Report` ADD FOREIGN KEY (user_id) REFERENCES `User` (`id`);
ALTER TABLE `Request` ADD FOREIGN KEY (creator_user_id) REFERENCES `User` (`id`);
ALTER TABLE `Request` ADD FOREIGN KEY (manager_user_id) REFERENCES `User` (`id`);
ALTER TABLE `Notification` ADD FOREIGN KEY (request_id) REFERENCES `Request` (`id`);
ALTER TABLE `Notification` ADD FOREIGN KEY (report_id) REFERENCES `Report` (`id`);

-- ---
-- Table Properties
-- ---

-- ALTER TABLE `User` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Report` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Request` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Notification` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ---
-- Test Data
-- ---

-- INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`) VALUES
-- ('','','','','','','','','');
-- INSERT INTO `Report` (`id`,`user_id`,`portal_type`,`file_path`,`file_display_name`,`file_title`,`file_content`) VALUES
-- ('','','','','','','');
-- INSERT INTO `Request` (`id`,`portal_type`,`creator_user_id`,`manager_user_id`,`request_name`,`request_description`,`request_type`) VALUES
-- ('','','','','','','');
-- INSERT INTO `Notification` (`id`,`portal_type`,`request_id`,`report_id`,`notification_name`,`notification_description`,`notification_type`,`unread`) VALUES
-- ('','','','','','','','');