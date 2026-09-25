package org.occideas.entity;

public class Constant {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final String STUDY_INTRO = "activeIntro";
    // When "true", the public startInterview flow shows the full Interview Responses Q&A tree
    // and the click-to-see-answer dots on the report - for internal assessor testing only.
    // Defaults to hidden (safer default) when unset, since general public use should not show this.
    public static final String START_INTERVIEW_ASSESSOR_MODE = "startInterviewAssessorMode";
    // Optional prefix (e.g. "LIVE") stuck on the auto-assigned reference number of participants
    // created through the public startInterview flow only - e.g. "LIVE00001" - so entries from that
    // flow (a demo environment, a specific deployment) are easy to tell apart in the admin lists from
    // participants entered by an assessor. Admin-entered/imported participants are never affected,
    // whether or not this is set. Unset/blank falls back to the historical plain-numeric reference.
    public static final String START_INTERVIEW_ID_PREFIX = "startInterviewIdPrefix";
    // Comma-separated list of valid employer codes (e.g. "ACMEM,BUILD") for the public startInterview
    // flow. A participant directed by their employer enters one of these (or arrives with ?code=) and
    // it replaces startInterviewIdPrefix as their reference prefix, e.g. "ACMEM00042". Matched
    // case-insensitively; codes not in the list are ignored and the default prefix is used instead.
    public static final String START_INTERVIEW_EMPLOYER_CODES = "startInterviewEmployerCodes";
    public static final String FILTER_STUDY_AGENTS = "filterStudyAgent";
    public static final String AUTO_CREATE_STUDY_AGENT_JSON = "autoCreateJson";

    public static final String STUDY_AGENT_SYS_PROP = "studyagent";
    public static final String VOXCO_TOOLTIPS = "voxco_tooltips";

    public static final String NOT_ASSESSED = "Not Assessed";
    public static final String AUTO_ASSESSED = "Auto Assessed";


    public static final String SPAN_START_DISPLAY_NONE = "<span style=\"display:none;\">";
    public static final String SPAN_END = "</span>";

    public static final String DONT_KNOW = "Don't know";
    public static final String NONE_OF_THE_ABOVE = "None of the above";
}
