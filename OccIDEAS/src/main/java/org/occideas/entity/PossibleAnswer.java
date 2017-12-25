package org.occideas.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity 
@DiscriminatorValue("P")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@SelectBeforeUpdate(value=true)
public class PossibleAnswer extends Node<Question> {

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idNode",referencedColumnName="idNode",updatable=false)
	protected List<ModuleRule> moduleRule; 
	
	public PossibleAnswer() {
		super();
	}

	public PossibleAnswer(Node<Question> node) {
		super(node);
	}

	public PossibleAnswer(String idNode) {
		this.setIdNode(Integer.parseInt(idNode));
	}

	public PossibleAnswer(long idNode) {
		this.setIdNode(idNode);
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
