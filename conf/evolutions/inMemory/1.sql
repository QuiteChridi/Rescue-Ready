
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Schema sopra-2023WS-team01
-- !Ups

CREATE TABLE IF NOT EXISTS `user` (
    `iduser` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `coins` INT NULL DEFAULT NULL,
    `profile_pic_path` VARCHAR(100) NULL DEFAULT 'images/profilePics/profilePic.png',
    PRIMARY KEY (`iduser`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC));



CREATE TABLE IF NOT EXISTS `friends` (
   `id_user_1` INT NOT NULL,
   `id_user_2` INT NOT NULL,
   PRIMARY KEY (`id_user_1`, `id_user_2`),
   CONSTRAINT `fk_user_has_user_user1`
       FOREIGN KEY (`id_user_1`)
           REFERENCES `user` (`iduser`)
           ON DELETE CASCADE
           ON UPDATE CASCADE,
   CONSTRAINT `fk_user_has_user_user2`
       FOREIGN KEY (`id_user_2`)
           REFERENCES `user` (`iduser`)
           ON DELETE CASCADE
           ON UPDATE CASCADE);



CREATE TABLE IF NOT EXISTS `quiz` (
        `idQuiz` INT NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(45) NOT NULL,
        PRIMARY KEY (`idQuiz`));



CREATE TABLE IF NOT EXISTS `highscores` (
  `quiz_idQuiz` INT NOT NULL,
  `user_iduser` INT NOT NULL,
  `highscore` INT NOT NULL,
  PRIMARY KEY (`quiz_idQuiz`, `user_iduser`),
  CONSTRAINT `fk_quiz_has_user_quiz1`
      FOREIGN KEY (`quiz_idQuiz`)
          REFERENCES `quiz` (`idQuiz`)
          ON DELETE CASCADE
          ON UPDATE CASCADE,
  CONSTRAINT `fk_quiz_has_user_user1`
      FOREIGN KEY (`user_iduser`)
          REFERENCES `user` (`iduser`)
          ON DELETE CASCADE
          ON UPDATE CASCADE);



CREATE TABLE IF NOT EXISTS `questions` (
 `quiz_idQuiz` INT NOT NULL,
 `question` VARCHAR(255) NOT NULL,
 `correctAnswer` VARCHAR(255) NULL DEFAULT NULL,
 `wrongAnswer1` VARCHAR(255) NULL DEFAULT NULL,
 `wrongAnswer2` VARCHAR(255) NULL DEFAULT NULL,
 `wrongAnswer3` VARCHAR(255) NULL DEFAULT NULL,
 PRIMARY KEY (`quiz_idQuiz`, `question`),
 CONSTRAINT `fk_questions_quiz1`
     FOREIGN KEY (`quiz_idQuiz`)
         REFERENCES `quiz` (`idQuiz`)
         ON DELETE CASCADE
         ON UPDATE CASCADE);

-- !Downs
DROP TABLE IF EXISTS `user` ;
DROP TABLE IF EXISTS `friends` ;
DROP TABLE IF EXISTS `highscores` ;
DROP TABLE IF EXISTS `quiz` ;
DROP TABLE IF EXISTS `questions` ;


