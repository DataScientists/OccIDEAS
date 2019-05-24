DELETE FROM Interview_Answer WHERE idinterview NOT IN (SELECT idinterview FROM Interview);
DELETE FROM Interview_Question WHERE idinterview NOT IN (SELECT idinterview FROM Interview);
DELETE FROM Interview WHERE idinterview NOT IN (SELECT distinct idinterview FROM Interview_Question);
DELETE FROM Participant WHERE idParticipant NOT IN (SELECT distinct idParticipant FROM Interview);

UPDATE Interview_Question SET type='Q_linkedmodule' where nodeClass is Null and type is null and name like '\_%';
UPDATE Interview_Question SET type='Q_linkedajsm' where nodeClass is Null and type is null;

