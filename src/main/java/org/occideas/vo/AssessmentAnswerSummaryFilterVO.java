package org.occideas.vo;

import org.apache.commons.lang3.StringUtils;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.entity.AssessmentAnswerSummary_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssessmentAnswerSummaryFilterVO implements SearchFilter {

  private Long answerId;
  private String name;
  private String moduleName;
  private Long idParticipant;
  private String reference;
  private Long idinterview;
  private String assessedStatus;
  private String statusDescription;
  private Integer status;
  private int pageNumber;
  private int size;

  public Long getAnswerId() {
    return answerId;
  }

  public void setAnswerId(Long answerId) {
    this.answerId = answerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Long getIdParticipant() {
    return idParticipant;
  }

  public void setIdParticipant(Long idParticipant) {
    this.idParticipant = idParticipant;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public Long getIdinterview() {
    return idinterview;
  }

  public void setIdinterview(Long idinterview) {
    this.idinterview = idinterview;
  }

  public String getAssessedStatus() {
    return assessedStatus;
  }

  public void setAssessedStatus(String assessedStatus) {
    this.assessedStatus = assessedStatus;
  }

  public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus() {
    if ("Running".equals(statusDescription)) {
      this.status = 0;
      return;
    } else if ("Partial".equals(statusDescription)) {
      this.status = 1;
      return;
    } else if ("Completed".equals(statusDescription)) {
      this.status = 2;
      return;
    } else if ("To be excluded".equals(statusDescription)) {
      this.status = 3;
      return;
    }
    this.status = null;
  }

  @Override
  public List<Predicate> getListOfRestrictions(CriteriaBuilder builder,
                                               Root<AssessmentAnswerSummary> root) {
    List<Predicate> predicateList = new ArrayList<>();

    Predicate nameRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.name), this.getName()));
    Predicate answerIdRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.answerId), this.getAnswerId()));
    Predicate participantIdRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.idParticipant), this.getIdParticipant()));
    Predicate assessedStatusRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.assessedStatus), this.getAssessedStatus()));
    Predicate moduleNameRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.interviewModuleName), this.getModuleName()));
    Predicate referenceRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.reference), this.getReference()));
    Predicate interviewIdRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.idinterview), this.getIdinterview()));
    Predicate statusRestriction = builder.and(builder.equal(root.get(AssessmentAnswerSummary_.status), this.getStatus()));


    if(StringUtils.isNotEmpty(this.getName())) {
      predicateList.add(nameRestriction);
    }

    if(Objects.nonNull(this.getAnswerId())) {
      predicateList.add(answerIdRestriction);
    }

    if(Objects.nonNull(this.getIdParticipant())) {
      predicateList.add(participantIdRestriction);
    }

    if(StringUtils.isNotEmpty(this.getAssessedStatus())) {
      predicateList.add(assessedStatusRestriction);
    }

    if(StringUtils.isNotEmpty(this.getModuleName())) {
      predicateList.add(moduleNameRestriction);
    }

    if(StringUtils.isNotEmpty(this.getReference())) {
      predicateList.add(referenceRestriction);
    }

    if(Objects.nonNull(this.getIdinterview())) {
      predicateList.add(interviewIdRestriction);
    }

    if(StringUtils.isNotEmpty(this.getStatusDescription())) {
      predicateList.add(statusRestriction);
    }

    return predicateList;
  }

}
