package org.occideas.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Entity 
@DiscriminatorValue("Q")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Question extends Node {

	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<PossibleAnswer> childNodes;
	
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

	public Question(String idNode) {
		this.setIdNode(Integer.parseInt(idNode));
	}

	public List<PossibleAnswer> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<PossibleAnswer> childNodes) {
		this.childNodes = childNodes;
	}
	
	
	
}
