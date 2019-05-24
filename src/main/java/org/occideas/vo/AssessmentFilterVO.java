package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssessmentFilterVO extends GenericFilterVO {

  private Long idParticipant;
  private Long interviewId;
  private String reference;
  private String assessedStatus;
  private Integer status;
  private String interviewModuleName;

  public Long getIdParticipant() {
    return idParticipant;
  }

  public void setIdParticipant(Long idParticipant) {
    this.idParticipant = idParticipant;
  }

  public Long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(Long interviewId) {
    this.interviewId = interviewId;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInterviewModuleName() {
    return interviewModuleName;
  }

  public void setInterviewModuleName(String interviewModuleName) {
    this.interviewModuleName = interviewModuleName;
  }

  @Override
  public void applyFilter(GenericFilterVO filter, SQLQuery sqlQuery) {
    if (filter != null && filter instanceof AssessmentFilterVO) {
      AssessmentFilterVO assessmentFilter = (AssessmentFilterVO) filter;
      if (assessmentFilter.getIdParticipant() != null) {
        sqlQuery.setParameter("idParticipant", "%" + assessmentFilter.getIdParticipant() + "%");
      } else {
        sqlQuery.setParameter("idParticipant", "%%");
      }
      if (!StringUtils.isEmpty(assessmentFilter.getReference())) {
        sqlQuery.setParameter("reference", "%" + assessmentFilter.getReference() + "%");
      } else {
        sqlQuery.setParameter("reference", "%%");
      }
      if (assessmentFilter.getStatus() != null) {
        sqlQuery.setParameter("status", "%" + assessmentFilter.getStatus() + "%");
      } else {
        sqlQuery.setParameter("status", "%%");
      }
      if (assessmentFilter.getAssessedStatus() != null) {
        sqlQuery.setParameter("assessedStatus", "%" + assessmentFilter.getAssessedStatus() + "%");
      } else {
        sqlQuery.setParameter("assessedStatus", "%%");
      }
      if (assessmentFilter.getInterviewId() != null) {
        sqlQuery.setParameter("idinterview", "%" + assessmentFilter.getInterviewId() + "%");
      } else {
        sqlQuery.setParameter("idinterview", "%%");
      }
      if (assessmentFilter.getInterviewModuleName() != null) {
        sqlQuery.setParameter("interviewModuleName", "%" + assessmentFilter.getInterviewModuleName() + "%");
      } else {
        sqlQuery.setParameter("interviewModuleName", "%%");
      }
    }
  }

  public String getAssessedStatus() {
    return assessedStatus;
  }

  public void setAssessedStatus(String assessedStatus) {
    this.assessedStatus = assessedStatus;
  }

}
