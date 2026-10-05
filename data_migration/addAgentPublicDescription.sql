-- Adds a participant-facing description per agent, used by the public individual exposure report:
-- "Your answers indicate that you may have been exposed to <agent name>, <publicDescription>."
-- The per-rule Rule.rationale stays as the assessor-view explanation.
-- Pre-fill it with apply_agent_public_description.sql.
ALTER TABLE AgentInfo ADD COLUMN publicDescription TEXT NULL;
