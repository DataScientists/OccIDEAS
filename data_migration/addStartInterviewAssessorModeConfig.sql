-- Controls whether the public startInterview flow shows the full "Interview Responses" Q&A
-- tree and the click-to-see-answer condition dots on the individual report. Intended for
-- internal assessor testing only - set to 'false' (or delete the row) before general public use.
-- Defaults to hidden (false) if this row is missing, so an unconfigured environment fails safe.
INSERT INTO SYS_CONFIG (type, name, value, updatedBy)
VALUES ('config', 'startInterviewAssessorMode', 'true', 'system');
