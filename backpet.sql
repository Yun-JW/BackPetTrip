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
  `gender` ENUM('M', 'F') NOT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
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
DROP TABLE IF EXISTS `cat1`;

CREATE TABLE IF NOT EXISTS `cat1` (
	
  `cat1_id` INT AUTO_INCREMENT PRIMARY KEY,
  `cat1` VARCHAR(20) NOT NULL UNIQUE,
  `name` VARCHAR(20) NOT NULL
  );

-- -----------------------------------------------------
-- Table `backpet`.`cat1`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `cat2`;

CREATE TABLE IF NOT EXISTS `cat2` (
	
  `cat2_id` INT AUTO_INCREMENT PRIMARY KEY,
  `cat1_id` INT NOT NULL,
  `cat2` VARCHAR(20) NOT NULL UNIQUE,
  `name` VARCHAR(20) NOT NULL,
  FOREIGN KEY (`cat1_id`) REFERENCES `cat1` (`cat1_id`) ON DELETE CASCADE
  );

-- -----------------------------------------------------
-- Table `backpet`.`cat2`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `cat3`;

CREATE TABLE IF NOT EXISTS `cat3` (
	
  `cat3_id` INT AUTO_INCREMENT PRIMARY KEY,
  `cat2_id` INT NOT NULL,
  `cat3` VARCHAR(20) NOT NULL UNIQUE,
  `name` VARCHAR(20) NOT NULL,
  FOREIGN KEY (`cat2_id`) REFERENCES `cat2` (`cat2_id`) ON DELETE CASCADE
  );

-- -----------------------------------------------------
-- Table `backpet`.`cat3`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `contents`;

CREATE TABLE IF NOT EXISTS `contents` (
  `content_id` INT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,  /* 관광지 이름 */
  `addr1` VARCHAR(200),           /* 기본 주소 */
  `addr2` VARCHAR(100),           /* 상세 주소 */
  `areacode` INT,                 /* 지역 코드 */
  `cat1` VARCHAR(10) NOT NULL,    /* 대분류 코드 */
  `cat2` VARCHAR(20) NOT NULL,    /* 중분류 코드 */
  `cat3` VARCHAR(30) NOT NULL,    /* 소분류 코드 */
  `contenttypeid` INT,            /* 관광타입 ID */
  `firstimage` VARCHAR(300),      /* 원본 대표 이미지 */
  `firstimage2` VARCHAR(300),     /* 썸네일 대표 이미지 */
  `mapx` DOUBLE,                  /* GPS X좌표(경도) */
  `mapy` DOUBLE,                  /* GPS Y좌표(위도) */
  `tel` VARCHAR(50),              /* 전화번호 */
  `zipcode` VARCHAR(10),          /* 우편번호 */
  `relaAcdntRiskMtr` VARCHAR(200),/* 관련 사고 대비사항 */
  `acmpyTypeCd` VARCHAR(100),     /* 동방유형코드 */
  `relaPosesFclty` VARCHAR(100),  /* 관련 구비 시설 */
  `relaFmshPrdlst` VARCHAR(100),  /* 관련 비치 품목 */
  `etcAcmpyInfo` VARCHAR(300),    /* 기타 동반 정보 */
  `relaPurcPrdlst` VARCHAR(100),  /* 관련 구매 품목 */
  `acmpyPsblCpam` VARCHAR(100),   /* 동반가능동물 */
  `relaRntlPrdlst` VARCHAR(100),  /* 관련 렌탈 품목 */
  `acmpyNeedMtr` VARCHAR(200)     /* 동반시 필요사항 */
);
  
-- -----------------------------------------------------
-- Table `backpet`.`contents`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `content_id` BIGINT NOT NULL,     -- 관광지 ID
  `member_id` INT NOT NULL,         -- 회원 ID
  `rating` INT NOT NULL CHECK (rating BETWEEN 1 AND 5),  -- 평점 (1~5점)
  `comment` TEXT,                   -- 리뷰 내용
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,       -- 작성 시각
  PRIMARY KEY (`content_id`, `member_id`),  -- 복합키로 1인 1리뷰 제한
  FOREIGN KEY (`content_id`) REFERENCES `contents`(`content_id`) ON DELETE CASCADE,
  FOREIGN KEY (`member_id`) REFERENCES `members`(`member_id`) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table `backpet`.`reviews`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `favorites`;

CREATE TABLE `favorites` (
  `content_id` INT NOT NULL,
  `member_id` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`content_id`, `member_id`),  -- 하나의 관광지에 하나의 찜만 허용
  FOREIGN KEY (`content_id`) REFERENCES `contents`(`content_id`) ON DELETE CASCADE,
  FOREIGN KEY (`member_id`) REFERENCES `members`(`member_id`) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table `backpet`.`favorites`
-- -----------------------------------------------------
