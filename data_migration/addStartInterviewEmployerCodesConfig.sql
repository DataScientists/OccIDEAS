-- Comma-separated list of valid employer codes for the public startInterview flow. A participant
-- directed by their employer enters one of these (or arrives with /startInterview?code=XXXXX), ticks
-- the employer-sharing consent box, and gets the code as their reference prefix, e.g. TESTA00042.
-- Matched case-insensitively. Codes not in this list are rejected on the start page.
--
-- Keep codes to 5 letters and never start one with the startInterviewIdPrefix default (e.g. LIVE),
-- or prefix searches will mix the groups. The value column is varchar(128), so the list holds
-- about 21 five-letter codes at most.
--
-- The codes below are EXAMPLES FOR TESTING - replace them with real employer codes before go-live.
-- Safe to re-run: removes any existing row first (getByName expects exactly one row per name).
DELETE FROM SYS_CONFIG WHERE name = 'startInterviewEmployerCodes';

INSERT INTO SYS_CONFIG (type, name, value, updatedBy)
VALUES ('config', 'startInterviewEmployerCodes', 'TESTA,TESTB,DEMOX', 'system');
