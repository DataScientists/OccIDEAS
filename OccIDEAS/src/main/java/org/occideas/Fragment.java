package org.occideas;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity 
@DiscriminatorValue("F")
public class Fragment extends Node{

	//private List<Question> questions;
	
	//private List<SimplerRule> moduleRules = new ArrayList<SimplerRule>();
	

	public Fragment() {
		super();
	}
	
	public Fragment(Long idNode) {
		super();
		this.setIdNode(idNode);
	}

	/*public List<Question> getQuestions() {
		return questions;
	}


	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}*/
	/*public List<SimplerRule> getModuleRules() {
		return moduleRules;
	}

	public void setModuleRules(List<SimplerRule> moduleRules) {
		this.moduleRules = moduleRules;
	}*/
}
