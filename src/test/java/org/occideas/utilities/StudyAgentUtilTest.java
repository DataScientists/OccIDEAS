package org.occideas.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;

public class StudyAgentUtilTest {
	
    @Ignore
    public void whenConvertToQSF_givenValidModule_returnQSF() throws IOException{

        StudyAgentUtil studyAgentUtil = new StudyAgentUtil();

        String expected = "{\"SurveyEntry\":{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"SurveyName\":\"SimpleSurvey\",\"SurveyDescription\":null,\"SurveyOwnerID\":\"UR_es2Gulf6I7xN6p7\",\"SurveyBrandID\":\"curtin\"," +
                "\"DivisionID\":null,\"SurveyLanguage\":\"EN\",\"SurveyActiveResponseSet\":\"RS_cMiHcMFlPoWrvyl\",\"SurveyStatus\":\"Inactive\",\"SurveyStartDate\":\"0000-00-00 00:00:00\",\"SurveyExpirationDate\":\"0000-00-00 00:00:00\"," +
                "\"SurveyCreationDate\":\"2019-11-22 02:22:39\",\"CreatorID\":\"UR_es2Gulf6I7xN6p7\",\"LastModified\":" +
                "\"2019-11-22 02:25:12\",\"LastAccessed\":\"0000-00-00 00:00:00\",\"LastActivated\":\"0000-00-00 00:00:00\",\"Deleted\":null}," +
                "\"SurveyElements\":[{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"BL\",\"PrimaryAttribute\":\"Survey Blocks\",\"SecondaryAttribute\":null,\"TertiaryAttribute\":null,\"Payload\":[{\"Type\":\"Default\",\"Description\":\"Default Question Block\"," +
                "\"ID\":\"BL_agDgwnIMsiB1KJL\",\"BlockElements\":[{\"Type\":\"Question\",\"QuestionID\":\"QID1\"},{\"Type\":\"Page Break\"}," +
                "{\"Type\":\"Question\",\"QuestionID\":\"QID2\"},{\"Type\":\"Page Break\"},{\"Type\":\"Question\",\"QuestionID\":\"QID3\"}]}," +
                "{\"Type\":\"Trash\",\"Description\":\"Trash \\/ Unused Questions\",\"ID\":\"BL_4I380AxUd3s3gt7\"}]}," +
                "{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"FL\",\"PrimaryAttribute\":\"Survey Flow\",\"SecondaryAttribute\":null," +
                "\"TertiaryAttribute\":null,\"Payload\":{\"Flow\":[{\"ID\":\"BL_agDgwnIMsiB1KJL\",\"Type\":\"Block\",\"FlowID\":\"FL_2\"}]," +
                "\"Properties\":{\"Count\":2},\"FlowID\":\"FL_1\",\"Type\":\"Root\"}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"SO\"," +
                "\"PrimaryAttribute\":\"Survey Options\",\"SecondaryAttribute\":null,\"TertiaryAttribute\":null,\"Payload\":{\"BackButton\":" +
                "\"false\",\"SaveAndContinue\":\"true\",\"SurveyProtection\":\"PublicSurvey\",\"BallotBoxStuffingPrevention\":\"false\"," +
                "\"NoIndex\":\"Yes\",\"SecureResponseFiles\":\"true\",\"SurveyExpiration\":\"None\",\"SurveyTermination\":\"DefaultMessage\"," +
                "\"Header\":\"\",\"Footer\":\"\",\"ProgressBarDisplay\":\"None\",\"PartialData\":\"+1 week\",\"ValidationMessage\":\"\"," +
                "\"PreviousButton\":\" \\u2190 \",\"NextButton\":\" \\u2192 \",\"SkinLibrary\":\"curtin\",\"SkinType\":\"MQ\"," +
                "\"Skin\":\"curtin1\",\"NewScoring\":1}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"SCO\",\"PrimaryAttribute\":" +
                "\"Scoring\",\"SecondaryAttribute\":null,\"TertiaryAttribute\":null,\"Payload\":{\"ScoringCategories\":[]," +
                "\"ScoringCategoryGroups\":[],\"ScoringSummaryCategory\":null,\"ScoringSummaryAfterQuestions\":0,\"ScoringSummaryAfterSurvey\":0," +
                "\"DefaultScoringCategory\":null,\"AutoScoringCategory\":null}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"PROJ\"," +
                "\"PrimaryAttribute\":\"CORE\",\"SecondaryAttribute\":null,\"TertiaryAttribute\":\"1.1.0\",\"Payload\":{\"ProjectCategory\":\"CORE\"," +
                "\"SchemaVersion\":\"1.1.0\"}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"STAT\",\"PrimaryAttribute\":\"Survey Statistics\",\"SecondaryAttribute\":null," +
                "\"TertiaryAttribute\":null,\"Payload\":{\"MobileCompatible\":true,\"ID\":\"Survey Statistics\"}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"QC\"," +
                "\"PrimaryAttribute\":\"Survey Question Count\",\"SecondaryAttribute\":\"3\",\"TertiaryAttribute\":null,\"Payload\":null},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\"," +
                "\"Element\":\"RS\",\"PrimaryAttribute\":\"RS_cMiHcMFlPoWrvyl\",\"SecondaryAttribute\":\"Default Response Set\",\"TertiaryAttribute\":null,\"Payload\":null},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\"," +
                "\"Element\":\"SQ\",\"PrimaryAttribute\":\"QID1\",\"SecondaryAttribute\":\"This is the first Question\",\"TertiaryAttribute\":null,\"Payload\":{\"QuestionText\":\"This is the first Question\",\"DataExportTag\":\"Q1\",\"QuestionType\":\"MC\",\"Selector\":\"SAVR\"," +
                "\"SubSelector\":\"TX\",\"Configuration\":{\"QuestionDescriptionOption\":\"UseText\"},\"QuestionDescription\":\"This is the first Question\",\"Choices\":{\"1\":{\"Display\":\"1\"},\"2\":{\"Display\":\"2\"},\"3\":{\"Display\":\"3\"}},\"ChoiceOrder\":[\"1\",\"2\",\"3\"]," +
                "\"Validation\":{\"Settings\":{\"ForceResponse\":\"OFF\",\"ForceResponseType\":\"ON\",\"Type\":\"None\"}},\"Language\":[],\"NextChoiceId\":4,\"NextAnswerId\":1,\"QuestionID\":\"QID1\"}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"SQ\",\"PrimaryAttribute\":\"QID2\"," +
                "\"SecondaryAttribute\":\"this is the second question\",\"TertiaryAttribute\":null,\"Payload\":{\"QuestionText\":\"this is the second question\",\"DataExportTag\":\"Q2\",\"QuestionType\":\"MC\",\"Selector\":\"SAVR\",\"SubSelector\":\"TX\",\"Configuration\":{\"QuestionDescriptionOption\":\"UseText\"}," +
                "\"QuestionDescription\":\"this is the second question\",\"Choices\":{\"1\":{\"Display\":\"4\"},\"2\":{\"Display\":\"5\"},\"3\":{\"Display\":\"6\"}},\"ChoiceOrder\":[\"1\",\"2\",\"3\"],\"Validation\":{\"Settings\":{\"ForceResponse\":\"OFF\",\"ForceResponseType\":\"ON\",\"Type\":\"None\"}},\"Language\":[],\"NextChoiceId\":4,\"NextAnswerId\":1," +
                "\"QuestionID\":\"QID2\",\"DisplayLogic\":{\"0\":{\"0\":{\"LogicType\":\"Question\",\"QuestionID\":\"QID1\",\"QuestionIsInLoop\":\"no\",\"ChoiceLocator\":\"q:\\/\\/QID1\\/SelectableChoice\\/2\",\"Operator\":\"Selected\",\"QuestionIDFromLocator\":\"QID1\",\"LeftOperand\":\"q:\\/\\/QID1\\/SelectableChoice\\/2\",\"Type\":\"Expression\"," +
                "\"Description\":\"<span class=\\\"ConjDesc\\\">If<\\/span> <span class=\\\"QuestionDesc\\\">This is the first Question<\\/span> <span class=\\\"LeftOpDesc\\\">2<\\/span> <span class=\\\"OpDesc\\\">Is Selected<\\/span> \"},\"Type\":\"If\"},\"Type\":\"BooleanExpression\",\"inPage\":false}}},{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"Element\":\"SQ\"," +
                "\"PrimaryAttribute\":\"QID3\",\"SecondaryAttribute\":\"This is the third Question\",\"TertiaryAttribute\":null,\"Payload\":{\"QuestionText\":\"This is the third Question\",\"DataExportTag\":\"Q3\",\"QuestionType\":\"MC\",\"Selector\":\"SAVR\",\"SubSelector\":\"TX\",\"Configuration\":{\"QuestionDescriptionOption\":\"UseText\"}," +
                "\"QuestionDescription\":\"This is the third Question\",\"Choices\":{\"1\":{\"Display\":\"7\"},\"2\":{\"Display\":\"8\"},\"3\":{\"Display\":\"9\"}},\"ChoiceOrder\":[\"1\",\"2\",\"3\"],\"Validation\":{\"Settings\":{\"ForceResponse\":\"OFF\",\"ForceResponseType\":\"ON\",\"Type\":\"None\"}},\"Language\":[],\"NextChoiceId\":4,\"NextAnswerId\":1,\"QuestionID\":\"QID3\"}}]" +
                "}";
        ModuleVO module = new ModuleVO();
        List<PossibleAnswerVO> answerVOList1 = new ArrayList<>();
        answerVOList1.add(new PossibleAnswerVO("1A","1"));
        answerVOList1.add(new PossibleAnswerVO("2A","2"));
        answerVOList1.add(new PossibleAnswerVO("3A","3"));
        List<PossibleAnswerVO> answerVOList2 = new ArrayList<>();
        answerVOList2.add(new PossibleAnswerVO("4A","4"));
        List<QuestionVO> childQuestion = new ArrayList<>();
        childQuestion.add(new QuestionVO("99A","Child Question",new ArrayList<>()));
        answerVOList2.add(new PossibleAnswerVO("5A","5",childQuestion));
        answerVOList2.add(new PossibleAnswerVO("6A","6"));
        List<PossibleAnswerVO> answerVOList3 = new ArrayList<>();
        answerVOList3.add(new PossibleAnswerVO("7A","7"));
        answerVOList3.add(new PossibleAnswerVO("8A","8"));
        answerVOList3.add(new PossibleAnswerVO("9A","9"));
        List<QuestionVO> questionList = new ArrayList<>();
        questionList.add(new QuestionVO("1Q","This is the first Question",answerVOList1));
        questionList.add(new QuestionVO("2Q","This is the second Question",answerVOList2));
        questionList.add(new QuestionVO("2Q","This is the third Question",answerVOList3));
        module.setChildNodes(questionList);
        module.setName("Test");
        Assert.assertEquals(expected,studyAgentUtil.moduleToApplicationQSF(module).toString());

    }

}
