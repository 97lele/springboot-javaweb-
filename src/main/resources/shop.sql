/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.7.22-log : Database - shop
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shop` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `shop`;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `priority` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `order_detail` */

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `order_detail_id` varchar(64) NOT NULL,
  `order_id` varchar(64) NOT NULL,
  `product_count` int(11) NOT NULL,
  `product_name` varchar(64) NOT NULL,
  `product_price` float NOT NULL,
  `product_id` varchar(64) NOT NULL,
  `product_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `order_id` varchar(64) NOT NULL,
  `buyer_name` varchar(64) NOT NULL,
  `create_date` varchar(64) NOT NULL,
  `total_amount` float NOT NULL,
  `status` int(2) DEFAULT '0' COMMENT '1完结，0处理中，-1撤销,-2已失效',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `category` int(11) NOT NULL,
  `price` float(10,1) NOT NULL,
  `stock` int(11) NOT NULL,
  `is_new` int(11) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `description` text,
  `product_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `product_relation` */

DROP TABLE IF EXISTS `product_relation`;

CREATE TABLE `product_relation` (
  `product_master` varchar(64) NOT NULL,
  `product_slave` varchar(64) NOT NULL,
  `product_relate` double NOT NULL,
  PRIMARY KEY (`product_master`,`product_slave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `role` int(1) NOT NULL,
  `token` varchar(64) DEFAULT 'off',
  `address` varchar(255) DEFAULT NULL,
  `mobile` varchar(18) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
