INSERT INTO occideas.Participant (idParticipant,reference,status,lastUpdated,deleted) VALUES 
(7,"H005116",1,current_timestamp(),0),
(11,"H002929",1,current_timestamp(),0),
(12,"H000312",1,current_timestamp(),0),
(15,"H007912",1,current_timestamp(),0),
(16,"H001473",1,current_timestamp(),0)
;

UPDATE `occideasclean`.`Participant` SET `reference`='H005116' WHERE `idParticipant`='7';
UPDATE `occideasclean`.`Participant` SET `reference`='H002929' WHERE `idParticipant`='11';
UPDATE `occideasclean`.`Participant` SET `reference`='H000312' WHERE `idParticipant`='12';
UPDATE `occideasclean`.`Participant` SET `reference`='H007912' WHERE `idParticipant`='15';
UPDATE `occideasclean`.`Participant` SET `reference`='H001473' WHERE `idParticipant`='16';
