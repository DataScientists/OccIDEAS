# OccIDEAS: our position on health data

Working summary • 25 September 2026 • Status: **draft, pending review by our legal/privacy advisor**

This is the position OccIDEAS takes on health data in the public interview (`/startInterview`). It is
based on the advisor's draft guide in this folder
([OccIDEAS_Website_Overview_and_Report_Guide.md](OccIDEAS_Website_Overview_and_Report_Guide.md)) and on
the decisions we have made about how the service runs. It is **not legal advice**. Where it goes beyond
what the advisor's guide covers, this is noted, and those points are listed in
[Open questions for the advisor](#8-open-questions-for-the-advisor).

## 1. Our overall position

1. **We treat interview answers and exposure results as potentially health information.** Whether they
   legally are health information, and whether a participant is "reasonably identifiable", are separate
   legal questions that the advisor's guide leaves open. Until the advisor says otherwise, we handle the
   data as if the Privacy Act's rules for sensitive information apply.
2. **We never describe the data as "anonymous".** "Anonymous" has a specific legal meaning, and we
   cannot guarantee it. For example, a job title and task description from a small workplace could point
   to one person. We describe what we actually do instead (see section 5).
3. **OccIDEAS estimates occupational exposure. It does not assess health.** We do not diagnose, predict
   disease, decide whether exposure has caused harm, or recommend treatment.
4. **We collect as little identifying information as we can.** The public interview has no login and
   collects no name, email or contact details.

## 2. What the public interview collects

| Data | Purpose | Where it goes |
| --- | --- | --- |
| Job title and description of usual tasks | Choose the right interview questions | Sent to the ABS occupation coding service (ANZSCO lookup) to find a matching occupation code |
| Interview answers (tasks, materials, conditions, protective measures) | Estimate exposure | Stored in OccIDEAS against the participant ID |
| Estimated exposure results | Participant's individual report | Stored in OccIDEAS; shown to the participant at the end |
| Participant ID (e.g. `LIVE00042`, or `ACMEM00042` with an employer code) | Keep answers and results together | Stored in OccIDEAS |
| Employer code, only if entered and consented to | Group results for the employer's report | Built into the participant ID |

Not collected: name, email address, phone number, address, date of birth, login details.

## 3. What results mean and how we describe them

### What the result categories mean

Each assessment rule gives a result of `probHigh`, `probMedium` or `probLow`. "Likely" describes where the
estimated exposure falls compared with the exposure limit. It does not describe whether exposure happens
at all. Confirmed 25 September 2026:

| Rule result | Report state | Wording |
| --- | --- | --- |
| `probHigh` | High ("flagged") | Your exposure is **likely to be above** the exposure limit |
| `probMedium` | Medium | Your exposure is **likely to be below** the exposure limit |
| `probLow` | Low | Your exposure is **estimated to be well below** the exposure limit |

- **probMedium** leaves some chance that exposure is above the limit, so a Medium result must not sound
  reassuring. The report says it "may still warrant attention" and suggests talking to a supervisor or
  OHS representative.
- **probLow** says "estimated to be" rather than "is", because it is the same kind of estimate as the
  others. It must not read as a measurement or a guarantee.

The guide says not to combine probability and level "unless the model explicitly supports both". This
model does, so it's allowed. The table in section 1 of the advisor's guide describes the categories by
level only, and should be updated to match (open question 8).

### How we describe results

We follow the advisor's guide for all wording that participants see:

- **"Estimated exposure", never measured exposure.** Results come from questionnaire answers. We never
  claim a measured concentration, a confirmed exceedance, or a legal breach.
- **"Exposure limit", never "safe limit".** Safe Work Australia says exposure standards are not a line
  between healthy and unhealthy workplaces.
- **High result:** we *recommend* that the participant sees a qualified health professional and shares
  their report with them, and we say this does not mean harm has been found.
- **Medium and Low results:** we never say "safe", "harmless", "nothing to worry about" or "no
  follow-up needed".
- **Not enough information / no exposure identified / Low** are three different results, and we
  describe them differently. A partly completed interview is labelled as partial.
- **General hazard information is kept separate from the participant's own result.** Health-effect
  statements about an agent are general background, not conclusions about the individual.

## 4. How participants can take part

### 4a. Taking part privately (no employer code)

The participant's answers and results are stored by OccIDEAS. The participant sees their own report at
the end. Their results are not shared with any employer.

### 4b. Taking part through an employer (employer code)

This is how we run it, as agreed with the employer before a code is issued:

- **The arrangement is made first.** OccIDEAS and the employer agree to the service, including how long
  data collection runs, before the employer receives a code.
- **The employer cannot link an ID to a person.** Employers are not given anything that links a
  participant ID to a worker. (Whether a worker could still be recognised from their answers is open
  question 2.)
- **OccIDEAS reports to the employer.** OccIDEAS itself prepares the report and gives it to the employer.
  Nothing is sent to the employer automatically.
- **The employer may see answers and will be told about exposures above the exposure limit,** so they
  can take steps to reduce the risk for everyone doing that work.
- **Participants choose.** Entering the code is optional. After a valid code is entered, the participant
  sees what sharing means and must tick a consent box before they can continue. Leaving the code blank
  means taking part privately (4a).
- **The server enforces the consent.** The system only uses an employer code if the consent box was
  ticked, so an ID with an employer's code shows that consent was given.

> **Note:** the advisor's guide describes a *different* arrangement, where a study organisation holds the
> link between ID and person and receives an exported results file. That description does not apply to
> employer codes. The employer-code arrangement above still needs to be reviewed by the advisor.

## 5. What we tell participants about identity

We use statements we can back up:

- "We don't collect your name or contact details."
- "Your employer won't be told which results are yours." (employer-code participants)

We do **not** say "anonymous", "confidential", "your data is not health information" or "the Privacy Act
does not apply".

## 6. Where this wording appears in the system

| Location | File |
| --- | --- |
| Start page introduction | `src/main/webapp/scripts/startInterview/view/startInterviewJobCoding.html` |
| Employer code help text, consent notice and checkbox | `src/main/webapp/scripts/startInterview/view/startInterviewJobCoding.html` |
| Report results, "What happens next" note, disclaimer | `src/main/webapp/scripts/interviews/partials/individualReport.html` |
| Rule rationale texts (draft, not yet in use) | `data_migration/rule_rationale_draft.csv` |
| ABS lookup notice under the job description field | `src/main/webapp/scripts/startInterview/view/startInterviewJobCoding.html` |
| Privacy notice (draft, not yet published) | `legal/privacy-notice-draft.md` |

Any change to this wording should be checked against this document and the advisor's guide.

## 7. Known gaps

Status as of 25 September 2026.

**Fixed**

1. ~~"Safe limits" wording.~~ The consent notice, consent checkbox and report now say "estimated
   exposure" and "exposure limit".
2. ~~Health framing on the start page.~~ The heading is now "Could you be exposed to hazards at work?",
   and both the start page and the report disclaimer say it is an estimate of exposure, not a health
   assessment or diagnosis.
3. ~~High-result advice is too weak.~~ Every High result now recommends seeing a doctor or other
   qualified health professional and sharing the report, and says this doesn't mean harm has been found.
4. ~~The no-exposure result reassures without support.~~ It now says "No exposures were identified from
   your answers" and that this isn't a guarantee of no exposure. The Low result says low exposure isn't
   the same as no exposure.
9. ~~Data sent to the ABS.~~ A note under the job description field tells participants their job title
   and description go to the ABS occupation coding service, and asks them not to include personal
   details. The draft privacy notice also covers it.

5. ~~Draft rationale texts.~~ The 128 texts with the flagged phrases were rewritten on 25 September 2026
   to use the scale in section 3 (`data_migration/rule_rationale_draft.csv`, applied by
   `data_migration/apply_rule_rationale.sql`). The texts are reviewed in the database, so **once the SQL
   is run, participants see these draft texts on their report.** The fallback texts used when a rule has
   no rationale were updated the same way.
6. ~~Probability or level?~~ Answered 25 September 2026. Each category combines both (see section 3).
   The report, the downloadable PDF report and the rationale texts all use that scale.

**Done**

8. ~~No privacy notice.~~ Finalised 25 September 2026 as a public page, `src/main/webapp/privacy.html`
   (served at `/privacy.html`, no login needed). Privacy contact privacy@occideas.org, general enquiries
   info@occideas.org. Linked from the site footer (`footer.html`, opens in a new tab) on every page.
   The questions to the advisor are kept in [privacy-notice-draft.md](privacy-notice-draft.md).

**Decided: set per project**

7. **Consent record.** Decided 25 September 2026: agreed with each employer as part of their
   arrangement. Today the system records only *that* consent was given (the employer code in the ID). If
   a project needs the time of consent or the wording shown, that needs a database change, which should
   be done before that project starts.
10. **Retention.** Decided 25 September 2026: set in each employer's arrangement, along with the data
    collection period. **Still needed:** a default retention period for people who take part *without*
    an employer code, since no arrangement covers them. The privacy notice has a placeholder for it.

**Also noticed**

- ~~The Medium and Low result headings say "occupational standards".~~ All report headings now say
  "exposure limit".
- ~~probHigh rationale drafts suggest an individual disease risk.~~ Rewritten 25 September 2026. The 59
  fallback texts per agent and the 1,822 texts written for individual rules now say "likely to be above
  the exposure limit" (`data_migration/apply_probhigh_rationale.sql`). As with the other rationale texts,
  participants see them as soon as the SQL is run.
- ~~Unknown-level rationale texts.~~ `probUnknown` and `possUnknown` rules have no rationale text, since
  they aren't shown to participants. `apply_rule_rationale.sql` clears any text already set on them.
- ~~Participants aren't shown their participant ID.~~ The public report and its PDF now show "Your
  participant ID: …. Keep this if you want to contact us about your data." The privacy notice tells
  participants to find it there.
- **7-year deletion needs a process.** Nothing deletes data automatically, so someone needs to delete
  interviews older than 7 years (participants without an employer code) and apply each employer's
  agreed period.

## 8. Open questions for the advisor

1. Does the employer-code arrangement (section 4b) meet the requirements for sensitive information,
   including consent through a tick box on the start page?
2. Could a participant be "reasonably identifiable" from their answers (job title, tasks, small
   workplace) even without a name? Does that change anything we should say or do?
3. Is recording consent through the employer code in the ID enough, or do we need to store the time of
   consent and the wording shown?
4. Is sending job titles and task descriptions to the ABS occupation coding service acceptable, and
   should participants be told about it?
5. What must a privacy notice for the public interview include, and where should it be shown?
6. How long should interview data be kept, and what should happen to it when an employer's data
   collection period ends?
7. Please review the current consent notice, consent checkbox and "What happens next" wording (quoted in
   the appendix).
8. The result categories combine likelihood and level (section 3). Please update the category table in
   section 1 of your guide to match, and confirm that the wording in section 3 is acceptable.

## Appendix: current participant-facing wording for review

**Start page introduction (heading and key point)**
> **Could you be exposed to hazards at work?**
> - An estimate of your exposure at work, not a health assessment or diagnosis

**Under the job description field**
> Your job title and description are sent to the Australian Bureau of Statistics' occupation coding
> service to match your occupation. Please don't include names or other personal details.

**Employer code help text**
> If your employer directed you to this site, enter the 5-character code they gave you. Leave it blank if
> you'd prefer to take part privately.

**Consent notice (shown after a valid code is entered)**
> **Because you've entered an employer code:**
> - Your employer may see your answers
> - Your employer will be told if your estimated exposure to a hazard is above the exposure limit, so they
>   can take steps to protect you and your co-workers
> - If you'd rather not share your results, remove the code to take part privately

**Consent checkbox**
> I understand my employer may see my answers and will be told if my estimated exposure is above an
> exposure limit.

**Report headings**
> - High: Your exposure to one or more hazards is likely to be above the exposure limit
> - Medium: Your exposure is likely to be below the exposure limit
> - Low: Your exposure is estimated to be well below the exposure limit
> - Nothing found: No exposures were identified from your answers

**Report: High result (all participants)**
> Based on your answers, your exposure to the agents below is likely to be above the exposure limit
> used by the assessment. This is an estimate from your answers, not a measurement at your workplace.
>
> **We recommend you talk to a doctor or other qualified health professional and share this report with
> them,** so they can decide whether any health check or follow-up is needed. This result does not mean
> that harm to your health has been found.

**Report: "What happens next" (High result, employer code)**
> Because you entered an employer code, OccIDEAS will let your employer know that your estimated exposure
> to one or more hazards was above the exposure limit, so they can take steps to reduce it for you and
> your co-workers. You don't need to raise this with them yourself.
>
> We don't collect your name or contact details, and your employer won't be told which results are yours.
> You can also talk to your workplace health and safety representative.

**Report: other results, employer code**
> As agreed, your results will be included in OccIDEAS's report to your employer. We don't collect your
> name or contact details, and your employer won't be told which results are yours.

**Report: no exposures identified**
> **No exposures were identified from your answers**
> Based on your answers, the assessment didn't identify exposure to any of the hazards it covers. This is
> an estimate from what you told us, not a guarantee that your work involves no exposure.

**Report disclaimer (all results)**
> **This is an estimate of your exposure at work, not a health assessment or medical diagnosis.**
