package org.occideas.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.occideas.agent.rest.AgentRestController;
import org.occideas.assessment.rest.AssessmentRestController;
import org.occideas.book.controller.BookController;
import org.occideas.exceptions.CustomExceptionHandler;
import org.occideas.fragment.rest.FragmentRestController;
import org.occideas.interview.rest.InterviewRestController;
import org.occideas.interviewanswer.rest.InterviewAnswerRestController;
import org.occideas.interviewautoassessment.rest.InterviewAutoAssessmentRestController;
import org.occideas.interviewdisplay.rest.InterviewDisplayRestController;
import org.occideas.interviewfiredrules.rest.InterviewFiredRulesRestController;
import org.occideas.interviewintromodulemodule.rest.InterviewIntroModuleModuleRestController;
import org.occideas.interviewmanualassessment.rest.InterviewManualAssessmentRestController;
import org.occideas.interviewmodulefragment.rest.InterviewModuleFragmentRestController;
import org.occideas.interviewquestion.rest.InterviewQuestionRestController;
import org.occideas.jmx.rest.JMXRestController;
import org.occideas.module.rest.ModuleRestController;
import org.occideas.modulefragment.rest.ModuleFragmentRestController;
import org.occideas.moduleintromodule.rest.ModuleIntroModuleRestController;
import org.occideas.modulerule.rest.ModuleRuleRestController;
import org.occideas.nodelanguage.rest.NodeLanguageRestController;
import org.occideas.note.rest.NoteRestController;
import org.occideas.participant.rest.ParticipantRestController;
import org.occideas.possibleanswer.rest.PossibleAnswerRestController;
import org.occideas.qsf.subscriber.listener.QualtricsSurveyResponseListener;
import org.occideas.question.rest.QuestionRestController;
import org.occideas.reporthistory.rest.ReportHistoryRestController;
import org.occideas.rule.rest.RuleRestController;
import org.occideas.security.rest.AdminRestController;
import org.occideas.security.rest.UserLoginRestController;
import org.occideas.securityproperty.rest.SystemPropertyController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/web/rest")
@EnableSwagger2
public class JerseyConfig extends ResourceConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }


    public JerseyConfig() {
        register(AgentRestController.class);
        register(AssessmentRestController.class);
        register(FragmentRestController.class);
        register(InterviewRestController.class);
        register(InterviewAnswerRestController.class);
        register(InterviewAutoAssessmentRestController.class);
        register(InterviewDisplayRestController.class);
        register(InterviewFiredRulesRestController.class);
        register(InterviewIntroModuleModuleRestController.class);
        register(InterviewManualAssessmentRestController.class);
        register(InterviewModuleFragmentRestController.class);
        register(InterviewQuestionRestController.class);
        register(JMXRestController.class);
        register(ModuleRestController.class);
        register(ModuleFragmentRestController.class);
        register(ModuleIntroModuleRestController.class);
        register(ModuleRuleRestController.class);
        register(NodeLanguageRestController.class);
        register(NoteRestController.class);
        register(ParticipantRestController.class);
        register(PossibleAnswerRestController.class);
        register(QuestionRestController.class);
        register(ReportHistoryRestController.class);
        register(RuleRestController.class);
        register(AdminRestController.class);
        register(UserLoginRestController.class);
        register(SystemPropertyController.class);
        register(QualtricsSurveyResponseListener.class);
        register(BookController.class);
        register(CustomExceptionHandler.class);
        register(MultiPartFeature.class);
    }
}
