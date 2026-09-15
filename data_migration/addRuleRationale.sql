-- Adds a per-rule, participant-facing explanation field used by the individual
-- self-assessment report. For PROBABLE_HIGH rules, this holds the evidence backing
-- the finding; for lower-confidence rules (probMedium/probLow/probUnknown/possUnknown)
-- it holds calibrated, non-alarmist language explaining why the finding isn't flagged
-- as a concern.
ALTER TABLE Rule ADD COLUMN rationale TEXT NULL;
