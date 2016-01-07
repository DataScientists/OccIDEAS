package org.occideas;

import java.util.ArrayList;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity 
@DiscriminatorValue("Q")
public class Question extends Node {

	public Question() {
		super();
	}

	public Question(Node node) {
		super(node);
	}

	public Question(long idNode) {
		super();
		this.setIdNode(idNode);
	}

	public void addChild(PossibleAnswer childNode) {
		childNode.setParent(this);
		this.setChildNodes(this.getChildNodes() == null ? new ArrayList<Node>() : this.getChildNodes());
		this.getChildNodes().add(childNode);
	}

	public Question(String idNode) {
		this.setIdNode(Integer.parseInt(idNode));
	}
}
