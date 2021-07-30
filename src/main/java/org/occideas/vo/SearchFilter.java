package org.occideas.vo;

import org.occideas.entity.AssessmentAnswerSummary;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface SearchFilter {


    List<Predicate> getListOfRestrictions(CriteriaBuilder builder,
                                          Root<AssessmentAnswerSummary> root);
}
