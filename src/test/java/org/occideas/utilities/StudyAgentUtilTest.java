package org.occideas.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.occideas.vo.ModuleVO;

import static org.junit.Assert.*;

public class StudyAgentUtilTest {

    @Test
    public void whenConvertToQSF_givenValidModule_returnQSF(){

        StudyAgentUtil studyAgentUtil = new StudyAgentUtil();

        String expected = "{\"SurveyEntry\":{\"SurveyID\":\"SV_3W2wSyxLDan2ZPD\",\"SurveyName\":\"SimpleSurvey\",\"SurveyDescription\":null,\"SurveyOwnerID\":\"UR_es2Gulf6I7xN6p7\",\"SurveyBrandID\":\"curtin\"," +
                "\"DivisionID\":null,\"SurveyLanguage\":\"EN\",\"SurveyActiveResponseSet\":\"RS_cMiHcMFlPoWrvyl\",\"SurveyStatus\":\"Inactive\",\"SurveyStartDate\":\"0000-00-00 00:00:00\",\"SurveyExpirationDate\":\"0000-00-00 00:00:00\"," +
                "\"SurveyCreationDate\":\"2019-11-22 02:22:39\",\"CreatorID\":\"UR_es2Gulf6I7xN6p7\",\"LastModified\":" +
                "\"2019-11-22 02:25:12\",\"LastAccessed\":\"0000-00-00 00:00:00\",\"LastActivated\":\"0000-00-00 00:00:00\",\"Deleted\":null}}";
        Assert.assertEquals(expected,studyAgentUtil.moduleToApplicationQSF(new ModuleVO()).toString());

    }

}
