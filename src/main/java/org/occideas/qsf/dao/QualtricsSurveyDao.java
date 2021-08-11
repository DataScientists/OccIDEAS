package org.occideas.qsf.dao;

import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.entity.QualtricsSurvey_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class QualtricsSurveyDao extends GenericBaseDao<QualtricsSurvey, String> {

    public QualtricsSurveyDao() {
        super(QualtricsSurvey.class, QualtricsSurvey_.RESPONSE_ID);
    }


}
