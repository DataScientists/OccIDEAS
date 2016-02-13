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
@DiscriminatorValue("P")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class PossibleAnswer extends Node {

	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Question> childNodes;
	
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

	public List<Question> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Question> childNodes) {
		this.childNodes = childNodes;
	}
	
	

}
