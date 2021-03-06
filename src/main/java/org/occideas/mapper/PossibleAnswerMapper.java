package org.occideas.mapper;

import org.occideas.entity.PossibleAnswer;
import org.occideas.vo.PossibleAnswerVO;

import java.util.List;

public interface PossibleAnswerMapper {

  PossibleAnswer convertToPossibleAnswerExModRule(PossibleAnswerVO answerVO);

  List<PossibleAnswer> convertToPossibleAnswerExModRuleList(List<PossibleAnswerVO> answerVO);

  PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO);

  List<PossibleAnswer> convertToPossibleAnswerList(List<PossibleAnswerVO> answerVO);

  PossibleAnswerVO convertToPossibleAnswerVO(PossibleAnswer answerEntity, boolean includeChildNode);

  PossibleAnswerVO convertToPossibleAnswerWithModuleRuleVO(PossibleAnswer answerEntity);

  PossibleAnswerVO convertToInterviewPossibleAnswerVO(PossibleAnswer answerEntity);

  List<PossibleAnswerVO> convertToPossibleAnswerVOList(List<PossibleAnswer> answerEntity, boolean includeChildNodes);

  List<PossibleAnswerVO> convertToInterviewPossibleAnswerVOList(List<PossibleAnswer> answerEntity);

  PossibleAnswerVO convertToPossibleAnswerVOExModRule(PossibleAnswer answerEntity);

  List<PossibleAnswerVO> convertToPossibleAnswerVOExModRuleList(List<PossibleAnswer> answerEntity);

  PossibleAnswerVO convertToPossibleAnswerVOWithFlag(PossibleAnswer answerEntity,
                                                     boolean includeChildNode,
                                                     boolean includeRules);

  List<PossibleAnswerVO> convertToPossibleAnswerVOWithFlagList(List<PossibleAnswer> answerEntity,
                                                               boolean includeChildNode,
                                                               boolean includeRules);

  PossibleAnswerVO convertToPossibleAnswerVOExcQuestionAnsChild(PossibleAnswer answerEntity);

  PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO, boolean includeChild);

  List<PossibleAnswerVO> convertToPossibleAnswerListVO(List<PossibleAnswer> answer, boolean includeChild);

  PossibleAnswerVO convertToPossibleAnswerVOOnly(PossibleAnswer answerEntity, boolean includeChildNode);

  List<PossibleAnswerVO> convertToPossibleAnswerVOOnlyList(List<PossibleAnswer> answerEntity, boolean includeChildNodes);
}
