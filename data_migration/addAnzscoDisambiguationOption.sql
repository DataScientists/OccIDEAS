-- Stores the multiple-choice disambiguation question shown when an ANZSCO code returned by
-- the ABS Coder lookup is too short/coarse to resolve to a single OccIDEAS job module (e.g.
-- a 3-digit "minor group" code like 133 spans several modules: construction, forestry,
-- mining, office work, store person). One row per (prefix, candidate module) option; all rows
-- sharing a prefix are shown together as one question. Admin-editable directly via SQL for now.
CREATE TABLE AnzscoDisambiguationOption (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  anzscoPrefix VARCHAR(6) NOT NULL,
  questionText VARCHAR(500) NOT NULL,
  moduleCode VARCHAR(10) NOT NULL,
  optionLabel VARCHAR(255) NOT NULL,
  sequence INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_anzsco_disambig_prefix ON AnzscoDisambiguationOption (anzscoPrefix);
