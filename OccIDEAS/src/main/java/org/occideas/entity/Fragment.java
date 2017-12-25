package org.occideas.entity;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity 
@DiscriminatorValue("F")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
public class Fragment extends Node<Question>{

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idModule",referencedColumnName="idNode",updatable=false)
	protected List<ModuleRule> moduleRule; 
	
	public Fragment() {
		super();
	}
	
	public Fragment(Long idNode) {
		super();
		this.setIdNode(idNode);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public List<ModuleRule> getModuleRule() {
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRule> moduleRule) {
		this.moduleRule = moduleRule;
	}
}
