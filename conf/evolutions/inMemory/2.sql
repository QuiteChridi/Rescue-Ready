-- !Ups
INSERT INTO user VALUES (1, 'Willi', '1234', 'willi@stud-mail.de', 10, 'images/profilePic/ProfilePic.png');
INSERT INTO user VALUES (2, 'Hansi', '1234', 'hansi@stud-mail.de', 30, 'images/profilePic/ProfilePic.png');
INSERT INTO user VALUES (3, 'Paul', '1234', 'paul@stud-mail.de', 111, 'images/profilePic/ProfilePic.png');
INSERT INTO user VALUES (4, 'Sepp', '1234', 'sepp@stud-mail.de', 500, 'images/profilePic/ProfilePic.png');
INSERT INTO user VALUES (5, 'Felix', '1234', 'felix@stud-mail.de', 126, 'images/profilePic/ProfilePic.png');

-- !Downs
DELETE FROM user WHERE "iduser" = 1;
DELETE FROM user WHERE "iduser" = 2;
DELETE FROM user WHERE "iduser" = 3;
DELETE FROM user WHERE "iduser" = 4;
DELETE FROM user WHERE "iduser" = 5;