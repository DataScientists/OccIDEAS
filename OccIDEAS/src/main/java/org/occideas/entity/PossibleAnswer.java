package org.occideas.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity 
@DiscriminatorValue("P")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@SelectBeforeUpdate(value=true)
public class PossibleAnswer extends Node {

	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Question> childNodes;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idNode",referencedColumnName="idNode",updatable=false)
	protected List<ModuleRule> moduleRule; 
	
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

	public List<ModuleRule> getModuleRule() {
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRule> moduleRule) {
		this.moduleRule = moduleRule;
	}

	@Override
	public String toString() {
		return "PossibleAnswer [childNodes=" + childNodes + ", moduleRule=" + moduleRule + ", idNode=" + idNode
				+ ", type=" + type + "]";
	}
	
	

}
