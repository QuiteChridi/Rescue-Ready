-- !Ups
INSERT INTO user (idUser, name, password,email, coins, profile_pic_path)
VALUES
    (1, 'MaxMustermann', 'RescueReady', 'max.mueller@example.com', 100, 'images/profilePics/shark.png'),
    (2, 'HannahSchmidt', 'Sonnenlicht22', 'hannah.schmidt@example.com', 150, 'images/profilePics/bunny.png'),
    (3, 'DavidMeier', 'dMeier123', 'david.meier@example.com', 200, 'images/profilePics/bear.png'),
    (4, 'SophieBecker', 'KaffeeLiebhaber', 'sophie.becker@example.com', 120, 'images/profilePics/cat.png'),
    (5, 'JuliusBraun', 'Br0wnie!', 'julius.braun@example.com', 180, 'images/profilePics/capybara.png'),
    (6, 'LauraWagner', 'T@yl0rL0ve', 'laura.wagner@example.com', 90, 'images/profilePics/crocodile.png'),
    (7, 'ChristianSchulz', 'SchulzC', 'christian.schulz@example.com', 300, 'images/profilePics/dog.png'),
    (8, 'JasminHoffmann', 'J4sm1nH4v', 'jasmin.hoffmann@example.com', 250, 'images/profilePics/lion.png'),
    (9, 'MatthiasLehmann', 'M@ttLehm', 'matthias.lehmann@example.com', 80, 'images/profilePics/man.png'),
    (10, 'EmiliaSchmidt', 'L33Em!ly', 'emilia.schmidt@example.com', 220, 'images/profilePics/prince.png'),
    (11, 'JakobKaiser', 'Kaiser007', 'jakob.kaiser@example.com', 160, 'images/profilePics/racoon.png'),
    (12, 'LenaHermann', 'L3n4H3rm', 'lena.hermann@example.com', 270, 'images/profilePics/snake.png'),
    (13, 'MoritzKeller', 'M0r1tzK', 'moritz.keller@example.com', 140,'images/profilePics/racoon.png'),
    (14, 'LuisaKoch', 'LuisaKoch3z', 'luisa.koch@example.com', 190, 'images/profilePics/prince.png'),
    (15, 'AnnaBauer', 'An3Bau', 'anna.bauer@example.com', 110, 'images/profilePics/man.png'),
    (16, 'FelixHartmann', 'F3lixH4rt', 'felix.hartmann@example.com', 230, 'images/profilePics/capybara.png'),
    (17, 'ClaraSchneider', 'ClaraSchneider!', 'clara.schneider@example.com', 200, 'images/profilePics/crocodile.png'),
    (18, 'JohannesFischer', 'J0hFisch', 'johannes.fischer@example.com', 260, 'images/profilePics/bear.png'),
    (19, 'LeaMeyer', 'L3aMey', 'lea.meyer@example.com', 170, 'images/profilePics/squirrel.png'),
    (20, 'PaulaWeber', 'PaulaWeber#', 'paula.weber@example.com', 210, 'images/profilePics/snake.png');


-- !Downs
DELETE FROM user;
