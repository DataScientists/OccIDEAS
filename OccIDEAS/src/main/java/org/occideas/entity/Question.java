package org.occideas.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
		childNode.setParentId(this.getParentId());
		this.setChildNodes(this.getChildNodes() == null ? new ArrayList<Node>() : this.getChildNodes());
		this.getChildNodes().add(childNode);
	}

	public Question(String idNode) {
		this.setIdNode(Integer.parseInt(idNode));
	}
	
	@Override
	@OneToMany(mappedBy="parent",fetch=FetchType.EAGER)
	public List<Node> getChildNodes() {
		return super.getChildNodes();
	}
}
