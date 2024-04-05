-- !Ups
INSERT INTO quiz VALUES (1, 'Erste Hilfe bei Knochenbrüchen');
INSERT INTO quiz VALUES (2, 'Wiederbelebung (CPR)');
INSERT INTO quiz VALUES (3, 'Verbrennungen');

INSERT INTO questions VALUES (1,'Was ist bei der Ersten Hilfe für einen offenen Bruch zu beachten?' ,'Die Wunde steril verbinden ohne den Bruch zu berühren' ,'Leichter Druck auf die Wunde zur Blutstillung' ,'Hochlagern des verletzten Bereichs zur Schmerzlinderung' ,'Leichtes Kühlen der Wunde zur Vermeidung von Schwellungen' );
INSERT INTO questions VALUES (1,'Welche Maßnahme ist bei Verdacht auf einen Wirbelsäulenbruch sofort zu ergreifen?' ,'Die Person ruhig halten und nicht bewegen' ,'Leichte Neigung des Kopfes zur Atemerleichterung' ,'Vorsichtige Seitenlage zur Stabilisierung' ,'Sanftes Anheben der Beine zur Schockprävention' );
INSERT INTO questions VALUES (1,'Welches ist ein typisches Anzeichen eines Knochenbruchs?' ,'Deutliche Fehlstellung oder unnatürliche Beweglichkeit' ,'Anhaltende Taubheit im verletzten Bereich' ,'Starke Schwellung und Bluterguss' ,'Anhaltender dumpfer Schmerz' );
INSERT INTO questions VALUES (1,'Wie sollte ein gebrochener Arm vor der Ankunft des Rettungsdienstes gelagert werden?' ,'In einer Schlinge, um Bewegung zu minimieren' ,'In einer leicht gebeugten Position zur Schmerzlinderung' ,'Flach und gestützt um die Durchblutung zu fördern' ,'Leichtes Hochhalten zur Verringerung der Schwellung' );
INSERT INTO questions VALUES (1,'Wie sollten Sie reagieren, wenn Sie vermuten, dass jemand einen Knochenbruch hat?' ,'Den Bereich ruhigstellen und den Rettungsdienst alarmieren' ,'Leichter Druck auf die verletzte Stelle, um die Art des Bruchs zu beurteilen' ,'Vorsichtiges Bewegen des Bereichs zur Überprüfung der Beweglichkeit' ,'Anlegen eines festen Verbandes zur Stabilisierung' );
INSERT INTO questions VALUES (2,'In welchem Verhältnis sollten bei Erwachsenen Beatmung und Herzdruckmassage durchgeführt werden?' ,'2 Beatmungen zu 30 Herzdruckmassagen' ,'1 Beatmung zu 5 Herzdruckmassagen' ,'2 Beatmungen zu 15 Herzdruckmassagen' ,'1 Beatmung zu 10 Herzdruckmassagen' );
INSERT INTO questions VALUES (2,'Was ist der erste Schritt in der CPR (kardiopulmonalen Reanimation)?' ,'überprüfen des Bewusstseins' ,'Beatmung' ,'Herzdruckmassage' ,'Alarmieren des Rettungsdienstes' );
INSERT INTO questions VALUES (2,'Was sollten Sie tun, wenn eine Person bewusstlos ist aber noch normal atmet?' ,'Die Person in die stabile Seitenlage bringen' ,'Mit der Herzdruckmassage beginnen' ,'Die Person wecken und beruhigen' ,'Sofort mit Beatmung beginnen' );
INSERT INTO questions VALUES (2,'Welche Maßnahme ist bei einem Erstickungsunfall für eine bewusstlose Person geeignet?' ,'Überprüfung des Mundraums und Entfernung sichtbarer Blockaden vor der CPR' ,'Sofort mit Beatmungen beginnen' ,'Rückenlage und ruhiges Zuwarten auf Besserung' ,'Abdominal-Thrusts (Heimlich-Manöver) durchführen' );
INSERT INTO questions VALUES (2,'Wie oft sollten die Herzdruckmassagen bei einem Erwachsenen wÃ¤hrend der CPR durchgeführt werden?' ,'100-120 Mal pro Minute' ,'60-70 Mal pro Minute' ,'80-90 Mal pro Minute' ,'130-150 Mal pro Minute' );
INSERT INTO questions VALUES (3,'Warum sollte das Entfernen von in die Haut eingebrannten Materialien vermieden werden?' ,'Um eine Infektion zu verhindern und die Haut zu schützen' ,'Um die verbrannte Stelle nicht weiter zu reizen' ,'Um die Schmerzen des Betroffenen nicht zu verstärken' ,'Um die Brandwunde nicht weiter zu öffnen' );
INSERT INTO questions VALUES (3,'Warum sollte das Kühlen von großflächigen Verbrennungen vermieden werden?' ,'Es kann zu einer Unterkühlung des Körpers führen' ,'Es kann die Wundheilung verzögern' ,'Es kann zu Blasenbildung fÃ¼hren' ,'Es kann die Schmerzen verstÃ¤rken' );
INSERT INTO questions VALUES (3,'Was ist ein Zeichen dafür, dass eine Verbrennung möglicherweise schwerwiegender ist?' ,'Keine Schmerzen im verbrannten Bereich' ,'Bildung von Blasen auf der verbrannten Haut' ,'Rötung und Schwellung' ,'Rasche Abkühlung der verbrannten Stelle' );
INSERT INTO questions VALUES (3,'Welche Maßnahme ist bei einer Verbrennung mit heißem Wasser falsch?' ,'Sofort kaltes Wasser über die Verbrennung gießen' ,'Den verbrannten Bereich mit einem sauberen Tuch bedecken' ,'Die verbrannte Stelle mindestens 10 Minuten lang kühlen' ,'Entfernen von Schmuck oder eng anliegender Kleidung über der Verbrennung' );
INSERT INTO questions VALUES (3,'Wie können Verbrennungen durch heiße Flüssigkeiten behandelt werden, wenn sie kleinflächig sind?' ,'Die verbrannte Stelle sofort mit Wasser überspülen' ,'Die Wunde mit einer warmen Kompresse bedecken' ,'Die verbrannte Stelle mit einer Creme einreiben' ,'Die verbrannte Stelle mit Eis kühlen' );

-- !Downs
DELETE FROM quiz WHERE idQuiz = 1;
DELETE FROM quiz WHERE idQuiz = 2;
DELETE FROM quiz WHERE idQuiz = 3;

DELETE FROM questions WHERE quiz_idQuiz = 1;
DELETE FROM questions WHERE quiz_idQuiz = 2;
DELETE FROM questions WHERE quiz_idQuiz = 3;
