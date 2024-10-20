-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema touristguidedb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema touristguidedb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `touristguidedb` DEFAULT CHARACTER SET utf8 ;
USE `touristguidedb` ;

-- -----------------------------------------------------
-- Table `touristguidedb`.`City`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `touristguidedb`.`City` (
  `Postcode` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  UNIQUE INDEX `name_UNIQUE` (`Name` ASC) VISIBLE,
  PRIMARY KEY (`Postcode`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `touristguidedb`.`Touristattractions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `touristguidedb`.`Touristattractions` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(300) NOT NULL,
  `Postcode` INT NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC) VISIBLE,
  UNIQUE INDEX `Description_UNIQUE` (`Description` ASC) VISIBLE,
  INDEX `postcode_idx` (`Postcode` ASC) VISIBLE,
  CONSTRAINT `postcode`
    FOREIGN KEY (`Postcode`)
    REFERENCES `touristguidedb`.`City` (`Postcode`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `touristguidedb`.`Tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `touristguidedb`.`Tags` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `name_UNIQUE` (`Name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `touristguidedb`.`AttractionsTags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `touristguidedb`.`AttractionsTags` (
  `Touristattraction_ID` INT NOT NULL,
  `Tags_ID` INT NOT NULL,
  PRIMARY KEY (`Touristattraction_ID`, `Tags_ID`),
  INDEX `Tags_id_idx` (`Tags_ID` ASC) VISIBLE,
  CONSTRAINT `TourisAttraction_ID`
    FOREIGN KEY (`Touristattraction_ID`)
    REFERENCES `touristguidedb`.`Touristattractions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Tags_id`
    FOREIGN KEY (`Tags_ID`)
    REFERENCES `touristguidedb`.`Tags` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
