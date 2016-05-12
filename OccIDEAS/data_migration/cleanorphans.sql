DROP TABLE IF EXISTS orphanNodes;
CREATE TABLE orphanNodes AS SELECT idNode FROM occideas.Node where parent_idNode NOT IN (SELECT idNode FROM occideas.Node);

UPDATE Node SET deleted=1 WHERE idNode IN (SELECT idNode FROM orphanNodes);
DROP TABLE IF EXISTS orphanNodes;

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 


DROP TABLE IF EXISTS orphanNodes;
CREATE TABLE orphanNodes AS SELECT idNode FROM occideas.Node where parent_idNode NOT IN (SELECT idNode FROM occideas.Node);

UPDATE Node SET deleted=1 WHERE idNode IN (SELECT idNode FROM orphanNodes);
DROP TABLE IF EXISTS orphanNodes;

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 
DROP TABLE IF EXISTS orphanNodes;
CREATE TABLE orphanNodes AS SELECT idNode FROM occideas.Node where parent_idNode NOT IN (SELECT idNode FROM occideas.Node);

UPDATE Node SET deleted=1 WHERE idNode IN (SELECT idNode FROM orphanNodes);
DROP TABLE IF EXISTS orphanNodes;

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 
DROP TABLE IF EXISTS orphanNodes;
CREATE TABLE orphanNodes AS SELECT idNode FROM occideas.Node where parent_idNode NOT IN (SELECT idNode FROM occideas.Node);

UPDATE Node SET deleted=1 WHERE idNode IN (SELECT idNode FROM orphanNodes);
DROP TABLE IF EXISTS orphanNodes;

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 
DROP TABLE IF EXISTS orphanNodes;
CREATE TABLE orphanNodes AS SELECT idNode FROM occideas.Node where parent_idNode NOT IN (SELECT idNode FROM occideas.Node);

UPDATE Node SET deleted=1 WHERE idNode IN (SELECT idNode FROM orphanNodes);
DROP TABLE IF EXISTS orphanNodes;

SET FOREIGN_KEY_CHECKS=0; 
DELETE FROM Node WHERE deleted=1 AND idNode>0;
SET FOREIGN_KEY_CHECKS=1; 