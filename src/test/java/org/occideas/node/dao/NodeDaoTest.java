package org.occideas.node.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.HibernateConfigH2;
import org.occideas.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(HibernateConfigH2.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/occideas.properties")
class NodeDaoTest {

    @Autowired
    private NodeDao nodeDao;

    @AfterEach
    void cleanup(){
        nodeDao.deleteAll();
    }

    @Test
    void givenListOfNodes_whenSaveBatchNodes_shouldSaveAll() {
        Node node1 = createNode("Node 1", 0);
        Node node2 = createNode("Node 2", 0);
        List<Node> nodes = Arrays.asList(node1, node2);

        nodeDao.saveAll(nodes);

        assertTrue(node1.getIdNode() > 0);
        assertTrue(node2.getIdNode() > 0);
        assertFalse(nodeDao.findAll().isEmpty());
    }

    @Test
    void givenMultipleNodes_whenFindMaxId_shouldReturnMax(){
        Node node1 = createNode("Node 1", 0);
        Node node2 = createNode("Node 2", 0);
        List<Node> nodes = Arrays.asList(node1, node2);
        nodeDao.saveAll(nodes);

        long topNode = nodeDao.generateIdNode();

        assertEquals(node2.getIdNode(),topNode);
    }

    @Test
    void givenNoNodes_whenFindMaxId_shouldReturn0(){

        long topNode = nodeDao.generateIdNode();

        assertEquals(0,topNode);
    }

    private Node createNode(String name, int deleted) {
        Node node = new Node();
        node.setName(name);
        node.setNodeclass("N");
        node.setNodeDiscriminator("N");
        node.setType("N");
        node.setDeleted(deleted);
        return node;
    }
}