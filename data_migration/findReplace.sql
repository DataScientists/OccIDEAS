UPDATE occideas.Node 
SET name = REPLACE(name, ' usually?', '?')
WHERE name LIKE '% usually?';