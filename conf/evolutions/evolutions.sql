-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sopra-2023WS-team01
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sopra-2023WS-team01
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sopra-2023WS-team01` DEFAULT CHARACTER SET utf8 ;
USE `sopra-2023WS-team01` ;

-- -----------------------------------------------------
-- Table `sopra-2023WS-team01`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sopra-2023WS-team01`.`user` (
                                                            `iduser` INT NOT NULL AUTO_INCREMENT,
                                                            `name` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`iduser`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sopra-2023WS-team01`.`quiz`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sopra-2023WS-team01`.`quiz` (
                                                            `idQuiz` INT NOT NULL AUTO_INCREMENT,
                                                            `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idQuiz`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sopra-2023WS-team01`.`questions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sopra-2023WS-team01`.`questions` (
                                                                `quiz_idQuiz` INT NOT NULL,
                                                                 `question` VARCHAR(45) NOT NULL,
    `correctAnswer` VARCHAR(45) NOT NULL,
    `wrongAnswer1` VARCHAR(45) NOT NULL,
    `wrongAnswer2` VARCHAR(45) NOT NULL,
    `wrongAnswer3` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`quiz_idQuiz`, `question`),
    INDEX `fk_questions_quiz1_idx` (`quiz_idQuiz` ASC) VISIBLE,
    CONSTRAINT `fk_questions_quiz1`
    FOREIGN KEY (`quiz_idQuiz`)
    REFERENCES `sopra-2023WS-team01`.`quiz` (`idQuiz`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sopra-2023WS-team01`.`friends`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sopra-2023WS-team01`.`friends` (
                                                               `id_user_1` INT NOT NULL,
                                                               `id_user_2` INT NOT NULL,
                                                               PRIMARY KEY (`id_user_1`, `id_user_2`),
    INDEX `user_1` (`id_user_2` ASC) VISIBLE,
    INDEX `user_2` (`id_user_1` ASC) VISIBLE,
    CONSTRAINT `fk_user_has_user_user1`
    FOREIGN KEY (`id_user_1`)
    REFERENCES `sopra-2023WS-team01`.`user` (`iduser`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_user_has_user_user2`
    FOREIGN KEY (`id_user_2`)
    REFERENCES `sopra-2023WS-team01`.`user` (`iduser`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sopra-2023WS-team01`.`highscores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sopra-2023WS-team01`.`highscores` (
                                                                  `quiz_idQuiz` INT NOT NULL,
                                                                  `user_iduser` INT NOT NULL,
                                                                  `highscore` INT NOT NULL,
                                                                  PRIMARY KEY (`quiz_idQuiz`, `user_iduser`),
    INDEX `fk_quiz_has_user_user1_idx` (`user_iduser` ASC) VISIBLE,
    INDEX `fk_quiz_has_user_quiz1_idx` (`quiz_idQuiz` ASC) VISIBLE,
    CONSTRAINT `fk_quiz_has_user_quiz1`
    FOREIGN KEY (`quiz_idQuiz`)
    REFERENCES `sopra-2023WS-team01`.`quiz` (`idQuiz`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_quiz_has_user_user1`
    FOREIGN KEY (`user_iduser`)
    REFERENCES `sopra-2023WS-team01`.`user` (`iduser`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
