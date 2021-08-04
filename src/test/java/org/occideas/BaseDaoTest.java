package org.occideas;

import org.occideas.entity.Node;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class BaseDaoTest {

    protected Node createNode(String name, int deleted) {
        Node node = new Node();
        node.setName(name);
        node.setNodeclass("N");
        node.setNodeDiscriminator("N");
        node.setType("N");
        node.setDeleted(deleted);
        return node;
    }
}
