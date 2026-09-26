package org.occideas.vo;

import java.util.List;

public class EmailReportVO {

  private Long interviewId;
  // Participant reference (e.g. LIVE00042), shown on the public report so the participant can quote it
  // in a privacy request - the privacy notice (privacy.html) tells them to find it there.
  private String participantId;
  private String email;
  // "flagged" (a PROBABLE_HIGH rule fired) or "clear" (nothing above the safety threshold).
  // Lower-confidence findings (otherFindings) are shown as supplementary detail regardless
  // of which verdict applies, not a third competing result.
  private String verdictState;
  private List<IndividualFindingVO> highFindings;
  private List<IndividualFindingVO> otherFindings;
  private List<ReportTreeNodeVO> tree;

  public Long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(Long interviewId) {
    this.interviewId = interviewId;
  }

  public String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(String participantId) {
    this.participantId = participantId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getVerdictState() {
    return verdictState;
  }

  public void setVerdictState(String verdictState) {
    this.verdictState = verdictState;
  }

  public List<IndividualFindingVO> getHighFindings() {
    return highFindings;
  }

  public void setHighFindings(List<IndividualFindingVO> highFindings) {
    this.highFindings = highFindings;
  }

  public List<IndividualFindingVO> getOtherFindings() {
    return otherFindings;
  }

  public void setOtherFindings(List<IndividualFindingVO> otherFindings) {
    this.otherFindings = otherFindings;
  }

  public List<ReportTreeNodeVO> getTree() {
    return tree;
  }

  public void setTree(List<ReportTreeNodeVO> tree) {
    this.tree = tree;
  }
}
