-- !Ups
INSERT INTO highscores VALUES (1, 1, 147);
INSERT INTO highscores VALUES (2, 1, 9);
INSERT INTO highscores VALUES (3, 1, 87);
INSERT INTO highscores VALUES (1, 2, 55);
INSERT INTO highscores VALUES (2, 2, 131);
INSERT INTO highscores VALUES (3, 2, 140);
INSERT INTO highscores VALUES (1, 3, 75);
INSERT INTO highscores VALUES (2, 3, 44);
INSERT INTO highscores VALUES (3, 3, 116);
INSERT INTO highscores VALUES (1, 4, 66);
INSERT INTO highscores VALUES (2, 4, 107);
INSERT INTO highscores VALUES (3, 4, 82);
INSERT INTO highscores VALUES (1, 5, 56);
INSERT INTO highscores VALUES (2, 5, 147);
INSERT INTO highscores VALUES (3, 5, 16);

-- !Downs
DELETE FROM highscores WHERE quiz_idQuiz = 1 AND user_iduser = 1;
DELETE FROM highscores WHERE quiz_idQuiz = 2 AND user_iduser = 1;
DELETE FROM highscores WHERE quiz_idQuiz = 3 AND user_iduser = 1;
DELETE FROM highscores WHERE quiz_idQuiz = 1 AND user_iduser = 2;
DELETE FROM highscores WHERE quiz_idQuiz = 2 AND user_iduser = 2;
DELETE FROM highscores WHERE quiz_idQuiz = 3 AND user_iduser = 2;
DELETE FROM highscores WHERE quiz_idQuiz = 1 AND user_iduser = 3;
DELETE FROM highscores WHERE quiz_idQuiz = 2 AND user_iduser = 3;
DELETE FROM highscores WHERE quiz_idQuiz = 3 AND user_iduser = 3;
DELETE FROM highscores WHERE quiz_idQuiz = 1 AND user_iduser = 4;
DELETE FROM highscores WHERE quiz_idQuiz = 2 AND user_iduser = 4;
DELETE FROM highscores WHERE quiz_idQuiz = 3 AND user_iduser = 4;
DELETE FROM highscores WHERE quiz_idQuiz = 1 AND user_iduser = 5;
DELETE FROM highscores WHERE quiz_idQuiz = 2 AND user_iduser = 5;
DELETE FROM highscores WHERE quiz_idQuiz = 3 AND user_iduser = 5;


