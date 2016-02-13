package org.occideas.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Entity 
@DiscriminatorValue("F")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Fragment extends Node{

	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Question> childNodes;

	public Fragment() {
		super();
	}
	
	public Fragment(Long idNode) {
		super();
		this.setIdNode(idNode);
	}
	
	public List<Question> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Question> childNodes) {
		this.childNodes = childNodes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
