SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 

DROP TABLE IF EXISTS studyagents;
CREATE TABLE studyagents AS (SELECT idAgent FROM occideas.AgentInfo where
name='Toluene'
or name='Styrene'
or name='p-xylene'
or name='Trichloroethylene'
or name='Ethyl benzene'
or name='n-Hexane'
or name='Lead'
or name='Mercury'
or name='Carbon disulphide'
or name='impact noise'
or name='noise2'
or name='vibration'
or name='Carbon monoxide'
);

DROP TABLE IF EXISTS occideas.studynodes;
CREATE TABLE studynodes AS
SELECT n.*
FROM occideas.Node n
INNER JOIN ModuleRule mr ON n.idNode=mr.idNode
AND mr.idAgent IN (SELECT idAgent FROM occideas.studyagents);

DROP TABLE IF EXISTS nonstudynodes;
CREATE TABLE nonstudynodes AS SELECT n.idNode FROM occideas.Node n 
LEFT JOIN occideas.studynodes sn 
ON sn.parent_idNode = n.idNode 
WHERE sn.parent_idNode IS NULL
AND n.node_discriminator='Q'
AND n.type NOT LIKE 'Q_link%'
AND n.type NOT LIKE 'Q_frequency';

UPDATE occideas.AgentInfo SET deleted=1 where idAgent>0
AND idAgent NOT IN (SELECT idAgent FROm studyagents);

UPDATE occideas.Node SET deleted=1
WHERE idNode>0 AND idNode IN
(SELECT idNode FROM occideas.nonstudynodes);

UPDATE occideas.Node SET deleted=0
WHERE idNode>0 AND topNodeId=15001;



SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 

DROP TABLE IF EXISTS brokenlinks;
CREATE TABLE brokenlinks AS SELECT m.idNode FROM Node m LEFT JOIN Node n ON n.parent_idNode=m.idNode 
WHERE (m.node_discriminator='M' OR m.node_discriminator='F')
AND n.idNode IS NULL;

UPDATE Node SET deleted=1 WHERE idNode>0 AND link IN (SELECT idNode FROM brokenlinks);
