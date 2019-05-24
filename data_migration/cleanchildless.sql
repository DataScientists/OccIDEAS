DROP TABLE IF EXISTS childlessNodes;
CREATE TABLE childlessNodes AS SELECT m.idNode FROM Node m LEFT JOIN Node n ON n.parent_idNode=m.idNode 
WHERE m.node_discriminator!='P' 
AND m.type NOT LIKE 'Q_linked%'
AND n.idNode IS NULL;

UPDATE Node SET deleted=1 WHERE idNode>0 AND link IN (SELECT idNode FROM childlessNodes);
UPDATE Node SET deleted=1 WHERE idNode>0 AND idNode IN (SELECT idNode FROM childlessNodes);

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 

DROP TABLE IF EXISTS childlessNodes;
CREATE TABLE childlessNodes AS SELECT m.idNode FROM Node m LEFT JOIN Node n ON n.parent_idNode=m.idNode 
WHERE m.node_discriminator!='P' 
AND m.type NOT LIKE 'Q_linked%'
AND n.idNode IS NULL;

UPDATE Node SET deleted=1 WHERE idNode>0 AND link IN (SELECT idNode FROM childlessNodes);
UPDATE Node SET deleted=1 WHERE idNode>0 AND idNode IN (SELECT idNode FROM childlessNodes);

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 

DROP TABLE IF EXISTS childlessNodes;
CREATE TABLE childlessNodes AS SELECT m.idNode FROM Node m LEFT JOIN Node n ON n.parent_idNode=m.idNode 
WHERE m.node_discriminator!='P' 
AND m.type NOT LIKE 'Q_linked%'
AND n.idNode IS NULL;

UPDATE Node SET deleted=1 WHERE idNode>0 AND link IN (SELECT idNode FROM childlessNodes);
UPDATE Node SET deleted=1 WHERE idNode>0 AND idNode IN (SELECT idNode FROM childlessNodes);

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 
