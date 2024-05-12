package org.occideas.amr;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.agent.dao.AgentDao;
import org.occideas.entity.JobModule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.module.dao.ModuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class AmrExtractTransformLoad {

	@Autowired
	ModuleDao jobModuleDao;
	@Autowired
	AgentDao agentDao;

	@Test
	void etl() {
		/*
		try {
			if (JNDIUnitTestHelper.notInitialized()) {
				JNDIUnitTestHelper.init("/home/heyzeus/eclipse-workspace/OccIdeasLib/JavaSource/com/researchit/io/occideasalpha.properties");

				JobSpecificModule jsms = new JobSpecificModule();
				String errors = "";

				for (JobSpecificModule jsm : jsms.getStudySpecificJSMs(errors)) {
					String jsmName = jsm.getName();
					jsmName = jsmName.replace("\"", "_");
					jsmName = jsmName.replace(".", "_");
					jsmName = jsmName.replace("/", "_");
					JobModule m = new JobModule();
					m.setDescription(jsmName);
					for(Question q:jsm.getQuestions()) {
						org.occideas.entity.Question oq = new org.occideas.entity.Question();
						oq.setDescription(q.getDescription());
						for(com.researchit.PossibleAnswer pa:q.getPossibleAnswers()) {
							org.occideas.entity.PossibleAnswer opa = new PossibleAnswer();
							opa.setDescription(pa.getName());
							for(Question q1:pa.getChildQuestions()) {
								org.occideas.entity.Question oq1 = new org.occideas.entity.Question();
								oq1.setDescription(q1.getDescription());
								if(opa.getChildNodes()==null) {
									opa.setChildNodes(new ArrayList<org.occideas.entity.Question>());
								}
								opa.getChildNodes().add(oq1);
								for(com.researchit.PossibleAnswer pa1:q1.getPossibleAnswers()) {
									org.occideas.entity.PossibleAnswer opa1 = new PossibleAnswer();
									opa1.setDescription(pa1.getName());
									if(oq1.getChildNodes()==null) {
										oq1.setChildNodes(new ArrayList<org.occideas.entity.PossibleAnswer>());
									}
									oq1.getChildNodes().add(opa1);
								}
							}
							if(oq.getChildNodes()==null) {
								oq.setChildNodes(new ArrayList<org.occideas.entity.PossibleAnswer>());
							}
							oq.getChildNodes().add(opa);
						}
						if(m.getChildNodes()==null) {
							m.setChildNodes(new ArrayList<org.occideas.entity.Question>());
						}
						m.getChildNodes().add(oq);
					}
					System.out.println(m.getDescription());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
