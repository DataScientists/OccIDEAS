package org.occideas;

import java.util.ArrayList;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity 
@DiscriminatorValue("P")
public class PossibleAnswer extends Node {

	public PossibleAnswer() {
		super();
	}

	public PossibleAnswer(Node node) {
		super(node);
	}

	public PossibleAnswer(String idNode) {
		this.setIdNode(Integer.parseInt(idNode));
	}

	public PossibleAnswer(long idNode) {
		this.setIdNode(idNode);
	}

	public void addChild(Question childNode) {
		childNode.setParent(this);
		super.setChildNodes(getChildNodes() == null ? new ArrayList<Node>() : getChildNodes());
		getChildNodes().add(childNode);
	}
	@Override
	@JsonIgnore
	public Node getParent() {
		return super.getParent();
	}
}
