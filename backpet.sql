CREATE DATABASE IF NOT EXISTS `backpet`;
USE `backpet`;

DROP TABLE IF EXISTS `members`;

CREATE TABLE `members`(
  `member_id` INT AUTO_INCREMENT PRIMARY KEY,	
  `email` VARCHAR(100) NOT NULL UNIQUE,	
  `password` VARCHAR(100) NOT NULL,		
  `name` VARCHAR(50) NOT NULL,  		
  `phone` VARCHAR(20) NOT NULL,					
  `birthdate` DATE NOT NULL,
  `gender` ENUM('M', 'F') DEFAULT NULL,
  `address` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `role` ENUM('user', 'admin') DEFAULT 'user'
);

-- -----------------------------------------------------
-- Table `backpet`.`members`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `locations` ;

CREATE TABLE IF NOT EXISTS `locations` (
	
  `location_id` INT AUTO_INCREMENT PRIMARY KEY,
  `sido` VARCHAR(50) NOT NULL,
  `gugun` VARCHAR(50) NOT NULL
  );
  
-- -----------------------------------------------------
-- Table `backpet`.`locations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `contenttypes`;

CREATE TABLE IF NOT EXISTS `locations` (
	
  `contenttype_id` INT AUTO_INCREMENT PRIMARY KEY,
  `contenttype` INT NOT NULL,
  `cat1` VARCHAR(20) NOT NULL,
  `cat2` VARCHAR(20) NOT NULL,
  `cat3` VARCHAR(20) NOT NULL
  );

