-- DROP ALL TABLES TO AVOID DUPLICATES
DROP TABLE IF EXISTS `t_item_transaction`;
DROP TABLE IF EXISTS `t_transaction`;
DROP TABLE IF EXISTS `t_item`;
DROP TABLE IF EXISTS `t_hotel_active_menu`;
DROP TABLE IF EXISTS `t_hotel_menu`;
DROP TABLE IF EXISTS `t_menu`;
DROP TABLE IF EXISTS `t_user_role`;
DROP TABLE IF EXISTS `t_roles`;
DROP TABLE IF EXISTS `t_users`;
DROP TABLE IF EXISTS `t_hotel`;
DROP TABLE IF EXISTS `t_address`;

-- TABLE: t_address
CREATE TABLE `t_address` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `door_no` varchar(255),
  `line1` varchar(255),
  `line2` varchar(255),
  `land_mark` varchar(255),
  `city` varchar(255),
  `state` varchar(255),
  `pincode` varchar(255),
  `lat` varchar(255),
  `lng` varchar(255),
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_hotel
CREATE TABLE `t_hotel` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address_id` bigint(19) unsigned DEFAULT NULL,
  `phone` varchar(255),
  `menu_id` bigint(19) unsigned DEFAULT NULL,
  `website` varchar(255),
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fkx_address` (`address_id`),
  KEY `fkx_menu` (`menu_id`),
  CONSTRAINT `fkx_address` FOREIGN KEY (`address_id`) REFERENCES `t_address` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fkx_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_users
CREATE TABLE `t_users` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address_id` bigint(19) unsigned DEFAULT NULL,
  `phone` varchar(255),
  `hotel_id` bigint(19) unsigned DEFAULT NULL,
  `is_active` boolean DEFAULT true,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fkx_user_address` (`address_id`),
  CONSTRAINT `fkx_user_address` FOREIGN KEY (`address_id`) REFERENCES `t_address` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  KEY `fkx_user_hotel` (`hotel_id`),
  CONSTRAINT `fkx_user_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `t_hotel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_roles
CREATE TABLE `t_roles` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_user_role
CREATE TABLE `t_user_role` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(19) unsigned NOT NULL,
  `role_id` bigint(19) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fkx_user_role_user` (`user_id`),
  KEY `fkx_user_role_role` (`role_id`),
  CONSTRAINT `fkx_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`),
  CONSTRAINT `fkx_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `t_roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_menu
CREATE TABLE `t_menu` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `completed` boolean DEFAULT false,
  `hotel_id` bigint(19) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_hotel_menu
CREATE TABLE `t_hotel_menu` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `hotel_id` bigint(19) unsigned NOT NULL,
  `menu_id` bigint(19) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fkx_hotel_menu_hotel` (`hotel_id`),
  KEY `fkx_hotel_menu_menu` (`menu_id`),
  CONSTRAINT `fkx_hotel_menu_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `t_hotel` (`id`),
  CONSTRAINT `fkx_hotel_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_item
CREATE TABLE `t_item` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `alias` varchar(255) NOT NULL,
  `shortcut` varchar(255) NOT NULL,
  `is_available` boolean DEFAULT false,
  `is_veg` TINYINT(1) DEFAULT true;
  `price` bigint(19) DEFAULT NULL,
  `menu_id` bigint(19) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fkx_item_menu` (`menu_id`),
  CONSTRAINT `fkx_item_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_hotel_active_menu
--CREATE TABLE `t_hotel_active_menu` (
--  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
--  `hotel_id` bigint(19) unsigned NOT NULL,
--  `menu_id` bigint(19) unsigned NOT NULL,
--  `is_active` boolean NOT NULL DEFAULT true,
--  PRIMARY KEY (`id`),
--  UNIQUE KEY `unique_active_menu_per_hotel` (`hotel_id`, `menu_id`),
--  KEY `fkx_hotel_active_menu_hotel` (`hotel_id`),
--  KEY `fkx_hotel_active_menu_menu` (`menu_id`),
--  CONSTRAINT `fkx_hotel_active_menu_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `t_hotel` (`id`),
--  CONSTRAINT `fkx_hotel_active_menu_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
--) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_transaction
CREATE TABLE `t_transaction` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `total` decimal(10,2) NOT NULL,
  `payment_type` ENUM('cash', 'UPI', 'card') NOT NULL,
  `handler` varchar(255) NOT NULL,
  `note` varchar(255),
  `created_by` bigint(19) unsigned DEFAULT NULL,
  `hotel_id` bigint(19) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fkx_transaction_hotel` (`hotel_id`),
  CONSTRAINT `fkx_transaction_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `t_hotel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- TABLE: t_item_transaction
CREATE TABLE `t_item_transaction` (
  `id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `alias` varchar(255) NOT NULL,
  `shortcut` varchar(255) NOT NULL,
  `is_available` boolean DEFAULT false,
  `price` bigint(19) DEFAULT NULL,
  `transaction_id` bigint(19) unsigned NOT NULL,
  `menu_id` bigint(19) unsigned NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fkx_item_transaction_transaction` (`transaction_id`),
  KEY `fkx_item_transaction_menu` (`menu_id`),
  CONSTRAINT `fkx_item_transaction_transaction` FOREIGN KEY (`transaction_id`) REFERENCES `t_transaction` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fkx_item_transaction_menu` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `t_roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_BOSS');
