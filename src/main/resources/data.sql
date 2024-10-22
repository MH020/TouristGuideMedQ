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

    -- Insert test data into `City` table
INSERT INTO `touristguidedb`.`City` (`Postcode`, `Name`) VALUES
                                                             (10001, 'New York'),
                                                             (20001, 'Washington DC'),
                                                             (94105, 'San Francisco'),
                                                             (60601, 'Chicago'),
                                                             (30301, 'Atlanta');

-- Insert test data into `Touristattractions` table
INSERT INTO `touristguidedb`.`Touristattractions` (`Name`, `Description`, `Postcode`) VALUES
                                                                                          ('Statue of Liberty', 'Iconic national monument located on Liberty Island in New York City.', 10001),
                                                                                          ('Golden Gate Bridge', 'Famous suspension bridge in San Francisco.', 94105),
                                                                                          ('Lincoln Memorial', 'National monument honoring Abraham Lincoln, located in Washington DC.', 20001),
                                                                                          ('Millennium Park', 'Public park with outdoor artwork in Chicago.', 60601),
                                                                                          ('Georgia Aquarium', 'One of the largest aquariums in the world, located in Atlanta.', 30301);

-- Insert test data into `Tags` table
INSERT INTO `touristguidedb`.`Tags` (`Name`) VALUES
                                                 ('Historic'),
                                                 ('Architecture'),
                                                 ('Cultural'),
                                                 ('Family-friendly'),
                                                 ('Outdoor');

-- Insert test data into `AttractionsTags` table
-- Assuming Tag IDs and Tourist Attraction IDs are sequential starting from 1
INSERT INTO `touristguidedb`.`AttractionsTags` (`Touristattraction_ID`, `Tags_ID`) VALUES
                                                                                       (1, 1), -- Statue of Liberty -> Historic
                                                                                       (1, 2), -- Statue of Liberty -> Architecture
                                                                                       (2, 2), -- Golden Gate Bridge -> Architecture
                                                                                       (2, 5), -- Golden Gate Bridge -> Outdoor
                                                                                       (3, 1), -- Lincoln Memorial -> Historic
                                                                                       (3, 2), -- Lincoln Memorial -> Architecture
                                                                                       (4, 5), -- Millennium Park -> Outdoor
                                                                                       (5, 4), -- Georgia Aquarium -> Family-friendly
                                                                                       (5, 5); -- Georgia Aquarium -> Outdoor


