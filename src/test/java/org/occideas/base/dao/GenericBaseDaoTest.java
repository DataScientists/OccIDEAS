package org.occideas.base.dao;

import org.junit.jupiter.api.Test;
import org.occideas.BaseDaoTest;
import org.occideas.entity.Node;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

class GenericBaseDaoTest extends BaseDaoTest {

    @Resource
    GenericBaseDao<Node, Long> genericBaseDao;

    void givenEntity_whenSave_shouldSuccess() {
        Node newNode = createNode("New Node", 0);

        Node actual = genericBaseDao.save(newNode);

        assertNotNull(actual);
        assertEquals(1, actual.getIdNode());
    }

    void saveAll() {
    }

    void delete() {
    }

    void deleteAll() {
    }

    void testDeleteAll() {
    }
}