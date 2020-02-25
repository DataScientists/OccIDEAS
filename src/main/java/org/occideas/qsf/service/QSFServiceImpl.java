package org.occideas.qsf.service;

import org.occideas.qsf.dao.INodeQSFDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QSFServiceImpl implements IQSFService{

    @Autowired
    private INodeQSFDao dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String save(String surveyId, long idNode, String path) {
        return dao.save(surveyId,idNode,path);
    }

    @Override
    public String getByIdNode(long idNode) {
        return dao.getByIdNode(idNode);
    }

}
