UPDATE occideasAMR.Participant SET reference = SUBSTRING(reference, 5);
UPDATE occideasAMR.Participant SET reference = CONCAT(reference, '-P0');
UPDATE occideasAMR.Interview SET referenceNumber = SUBSTRING(reference, 5);
UPDATE occideasAMR.Interview SET referenceNumber = CONCAT(reference, '-P0');

UPDATE occideasAMR.Participant SET status = 1 WHERE idParticipant>20;
UPDATE occideasAMR.Participant SET status = 2 WHERE idParticipant BETWEEN 20 and 40;
UPDATE occideasAMR.Participant SET status = 3 WHERE idParticipant BETWEEN 40 and 50;
UPDATE occideasAMR.Participant SET status = 4 WHERE idParticipant BETWEEN 50 and 60;
UPDATE occideasAMR.Participant SET status = 5 WHERE idParticipant BETWEEN 60 and 70;