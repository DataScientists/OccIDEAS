package org.occideas.question.dao;

import org.occideas.base.dao.AbstractNodeDao;
import org.occideas.entity.Question;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewQuestionDao extends AbstractNodeDao<Question> {

    String GET_ALL_LINKING_QUESTION_BY_MOD_ID = "SELECT distinct n.* "
            + "FROM Node n, ModuleRule mr "
            + "WHERE n.topNodeId=?1 and n.link>0 and n.deleted=0 and n.link=mr.idModule "
            + "AND mr.idAgent in (select value from SYS_CONFIG where type='studyagent')";

    String GET_LINKING_QUESTION_BY_MOD_ID = "select * from Node where link "
            + "= ?1 and topNodeId = ?2 and deleted = 0";

    @Query(value=GET_LINKING_QUESTION_BY_MOD_ID, nativeQuery = true)
    Question getLinkingQuestionByModId(Long link, Long modId);

    @Query(value=GET_ALL_LINKING_QUESTION_BY_MOD_ID, nativeQuery = true)
    List<Question> getAllLinkingQuestionByModId(Long modId);

}
