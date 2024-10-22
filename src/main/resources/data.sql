-- H2 Compatible SQL Script

-- -----------------------------------------------------
-- Schema touristguidedb
-- -----------------------------------------------------


-- Drop tables if they already exist to avoid conflicts
DROP TABLE IF EXISTS AttractionsTags CASCADE;
DROP TABLE IF EXISTS Tags CASCADE;
DROP TABLE IF EXISTS Touristattractions CASCADE;
DROP TABLE IF EXISTS City CASCADE;

-- -----------------------------------------------------
-- Table `touristguidedb`.`City`
-- -----------------------------------------------------
CREATE TABLE City (
                                     Postcode INT NOT NULL,
                                     Name VARCHAR(45) UNIQUE  NOT NULL,
                                     PRIMARY KEY (Postcode)

);

-- -----------------------------------------------------
-- Table `touristguidedb`.`Touristattractions`
-- -----------------------------------------------------
CREATE TABLE Touristattractions (
                                                   ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                                   Name VARCHAR(45) UNIQUE NOT NULL,
                                                   Description VARCHAR(300) NOT NULL,
                                                   Postcode INT NOT NULL,

                                                   CONSTRAINT Description_UNIQUE UNIQUE (Description),
                                                   FOREIGN KEY (Postcode)
                                                       REFERENCES City (Postcode)
                                                       ON DELETE NO ACTION
                                                       ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `touristguidedb`.`Tags`
-- -----------------------------------------------------
CREATE TABLE Tags (
                                     ID INT NOT NULL AUTO_INCREMENT,
                                     Name VARCHAR(45) UNIQUE NOT NULL,
                                     PRIMARY KEY (ID)

);

-- -----------------------------------------------------
-- Table `touristguidedb`.`AttractionsTags`
-- -----------------------------------------------------
CREATE TABLE AttractionsTags (
                                                Touristattraction_ID INT NOT NULL,
                                                Tags_ID INT NOT NULL,
                                                PRIMARY KEY (Touristattraction_ID, Tags_ID),
                                                CONSTRAINT Tags_id FOREIGN KEY (Tags_ID)
                                                    REFERENCES Tags (ID)
                                                    ON DELETE NO ACTION
                                                    ON UPDATE NO ACTION,
                                                CONSTRAINT TourisAttraction_ID FOREIGN KEY (Touristattraction_ID)
                                                    REFERENCES Touristattractions (ID)
                                                    ON DELETE NO ACTION
                                                    ON UPDATE NO ACTION
);
-- Insert test data into City
INSERT INTO City (Postcode, Name) VALUES
                                      (12345, 'Springfield'),
                                      (54321, 'Shelbyville'),
                                      (98765, 'Capital City'),
                                      (24680, 'Smalltown');

-- Insert test data into Tags
INSERT INTO Tags (ID, Name) VALUES
                                (1, 'Museum'),
                                (2, 'Park'),
                                (3, 'Historic Site'),
                                (4, 'Landmark'),
                                (5, 'Nature');

-- Insert test data into Touristattractions
INSERT INTO Touristattractions (ID, Name, Description, Postcode) VALUES
                                                                     (1, 'Springfield Museum', 'A museum showcasing local history.', 12345),
                                                                     (2, 'Shelbyville Park', 'A beautiful park with lots of greenery.', 54321),
                                                                     (3, 'Capital City Library', 'The largest library in the capital.', 98765),
                                                                     (4, 'Smalltown Historic District', 'A district full of historic buildings.', 24680),
                                                                     (5, 'The Great Landmark', 'An iconic landmark of the area.', 54321);

-- Insert test data into AttractionsTags
INSERT INTO AttractionsTags (Touristattraction_ID, Tags_ID) VALUES
                                                                (1, 1),  -- Springfield Museum - Museum
                                                                (1, 4),  -- Springfield Museum - Landmark
                                                                (2, 2),  -- Shelbyville Park - Park
                                                                (3, 5),  -- Capital City Library - Nature
                                                                (4, 3),  -- Smalltown Historic District - Historic Site
                                                                (5, 4);  -- The Great Landmark - Landmark

