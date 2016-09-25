package org.occideas.assessment.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.question.service.QuestionService;
import org.occideas.reporthistory.service.ReportHistoryService;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.ReportsEnum;
import org.occideas.utilities.ReportsStatusEnum;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.ReportHistoryVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opencsv.CSVWriter;

@Path("/assessment")
public class AssessmentRestController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private InterviewService interviewService;
	@Autowired
	private InterviewQuestionService interviewQuestionService;
	@Autowired
	private SystemPropertyService systemPropertyService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ReportHistoryService reportHistoryService;
	
	@GET
    @Path(value = "/exportInterviewsCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportInterviewsCSV() {
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO property = systemPropertyService.
				getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(property == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName();
		String fullPath = property.getValue()+exportFileCSV;
		ReportHistoryVO reportHistoryVO = 
				insertToReportHistory(exportFileCSV, fullPath,null,0);
		log.info("[Report] before getting unique interview questions ");
		List<InterviewQuestionVO> uniqueInterviewQuestions = interviewQuestionService.getUniqueInterviewQuestions();
		log.info("[Report] after getting unique interview questions ");
		ExportCSVVO csvVO = populateCSV(uniqueInterviewQuestions,reportHistoryVO);
		CSVWriter writer;
		try {
			File file = new File(fullPath);
			writer = new CSVWriter(new FileWriter(file), ',');
			// feed in your array (or convert your data to an array)
			String[] headers = Arrays.copyOf(csvVO.getHeaders().toArray(), 
					csvVO.getHeaders().toArray().length, String[].class);
			//write header
			writer.writeNext(headers);
			//write answers
			//iterate map
			Iterator listOfAnswers = csvVO.getAnswers().entrySet().iterator();
			while (listOfAnswers.hasNext()) {
			  Entry thisEntry = (Entry) listOfAnswers.next();
			  List<String> value = (List<String>) thisEntry.getValue();
			  String[] answers = Arrays.copyOf(value.toArray(), 
						value.toArray().length, String[].class);
			  writer.writeNext(answers);
			}
			writer.close();
			updateProgress(reportHistoryVO,ReportsStatusEnum.COMPLETED.getValue(), 
					100);
		} catch (IOException e) {
			e.printStackTrace();
			updateProgress(reportHistoryVO,ReportsStatusEnum.FAILED.getValue(), 
					0);
		}
		return Response.ok().build();
    }

	private ReportHistoryVO insertToReportHistory(String exportFileCSV, 
			String fullPath,
			Long id,
			int progress) {
		ReportHistoryVO reportHistoryVO = new ReportHistoryVO();
		if(id !=null){
			reportHistoryVO.setId(id);
		}
		String user = extractUserFromToken();
		reportHistoryVO.setName(exportFileCSV);
		reportHistoryVO.setPath(fullPath);
		reportHistoryVO.setRequestor(user);
		reportHistoryVO.setStatus(ReportsStatusEnum.IN_PROGRESS.getValue());
		reportHistoryVO.setType(ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());
		reportHistoryVO.setUpdatedBy(user);
		reportHistoryVO.setProgress(progress+"%");
		return reportHistoryService.save(reportHistoryVO);
	}
	
	private ReportHistoryVO updateProgress(ReportHistoryVO reportHistoryVO, 
			String status,double progress) {
		reportHistoryVO.setStatus(status);
		if(progress != 0){
		reportHistoryVO.setProgress(progress+"%");
		}
		return reportHistoryService.save(reportHistoryVO);
	}
	
	private String extractUserFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse)SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		return tokenManager.parseUsernameFromToken(token);
	}

	private String createFileName() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy-HHmm");
		String now = format.format(date);
		String exportFileCSV = "export_interviews_"+now+".csv";
		return exportFileCSV;
	}

	private ExportCSVVO populateCSV(List<InterviewQuestionVO> uniqueInterviewQuestions,ReportHistoryVO reportHistoryVO) {
		ExportCSVVO vo = new ExportCSVVO();
		TreeSet<String> headers = populateHeadersAndAnswers(uniqueInterviewQuestions,vo,reportHistoryVO);
		vo.setHeaders(headers);
		return vo;
	}

	private TreeSet<String> populateHeadersAndAnswers(List<InterviewQuestionVO> uniqueInterviewQuestions
				,ExportCSVVO exportCSVVO,ReportHistoryVO reportHistoryVO) {
		TreeSet<String> headers = new TreeSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		for(InterviewQuestionVO interviewQuestionVO:uniqueInterviewQuestions){
			log.info("[Report] adding header for "+interviewQuestionVO.getIdInterview());
			addHeaders(headers, interviewQuestionVO,exportCSVVO);
		}
		int intervalCountBeforeReportUpdate = uniqueInterviewQuestions.size()/6;
		double count = 0;
		for(InterviewQuestionVO interviewQuestionVO:uniqueInterviewQuestions){
			count++;
			if(intervalCountBeforeReportUpdate == count){
				double progress = Math.floor((count/uniqueInterviewQuestions.size())*100);
				updateProgress(reportHistoryVO,ReportsStatusEnum.IN_PROGRESS.getValue(), 
						progress);
			}
			List<InterviewVO> interviewQuestionAnswer = interviewService.
					getInterviewQuestionAnswer(interviewQuestionVO.getIdInterview());
			for(InterviewVO interviewVO:interviewQuestionAnswer){
				log.info("[Report] processing interview id "+interviewVO.getInterviewId());
				List<String> answers = new ArrayList<>();
				answers.add(String.valueOf(interviewVO.getReferenceNumber()));
				answers.add(String.valueOf(interviewVO.getInterviewId()));
				Set<String> questionIdList = exportCSVVO.getQuestionIdList();
				for(String questionId:questionIdList){
					if(questionId.contains("_")){
						String[] temp = questionId.split("_");
						long tempQId = Long.valueOf(temp[0]);
						String tempNumber = String.valueOf(temp[1]);
						InterviewQuestionVO questionAsked = null;
						for(InterviewQuestionVO mIntQuestionVO:interviewVO.getQuestionHistory()){
							if(mIntQuestionVO.getQuestionId() == tempQId){
								questionAsked = mIntQuestionVO;
								break;
							}
						}
						if(questionAsked != null){
							if(!questionAsked.getAnswers().isEmpty()){
								boolean numberExist = false;
								for(InterviewAnswerVO ans:questionAsked.getAnswers()){
									if(ans.getNumber().equals(tempNumber)
											&& !numberExist){
										if(!StringUtils.isEmpty(ans.getAnswerFreetext())){
											answers.add(ans.getAnswerFreetext());
											numberExist = true;
										}else{
											answers.add(ans.getName());
											numberExist = true;
										}
									}
								}
								if(!numberExist){
									answers.add("-- No Answer --");
								}
							}
						}else{
							answers.add("-- Question Not Asked --");
						}
					}else{
						InterviewQuestionVO questionAsked = null;
						for(InterviewQuestionVO mIntQuestionVO:interviewVO.getQuestionHistory()){
							if(mIntQuestionVO.getQuestionId() == Long.valueOf(questionId)){
								questionAsked = mIntQuestionVO;
								break;
							}
						}
						if(questionAsked != null){
							if(!questionAsked.getAnswers().isEmpty()){
								InterviewAnswerVO questionAskedAns = questionAsked.getAnswers().get(0);
								if(!StringUtils.isEmpty(questionAskedAns.getAnswerFreetext())){
									answers.add(questionAskedAns.getAnswerFreetext());
								}else{
									answers.add(questionAskedAns.getName());
								}
							}else{
								answers.add("-- No Answer --");
							}
						}else{
							answers.add("-- Question Not Asked --");
						}
					}
				}
				exportCSVVO.getAnswers().put(interviewVO, answers);
			}
		}
		return headers;
	}

	private void addHeaders(TreeSet<String> headers, 
			InterviewQuestionVO interviewQuestionVO, ExportCSVVO exportCSVVO) {
		if(isModuleOrAjsm(interviewQuestionVO)){
//				headers.add(interviewQuestionVO.getName().substring(0, 4));
		}
		else if("Q_multiple".equals(interviewQuestionVO.getType())){
				QuestionVO qVO = findInterviewQuestionInMultipleQuestionsNode
						(interviewQuestionVO.getQuestionId());
				if(qVO == null){
					System.out.println("Something wrong with our data");
					return;
				}
				NodeVO topModule = getTopModuleByTopNodeId(qVO.getTopNodeId());
				for(PossibleAnswerVO pVO:qVO.getChildNodes()){
					StringBuilder header = new StringBuilder();
					header.append(topModule.getName().substring(0, 4));
					header.append("_");
					header.append(interviewQuestionVO.getQuestionId());
					header.append("_");
					header.append(interviewQuestionVO.getNumber());
					header.append("_");
					header.append(pVO.getNumber());
					headers.add(header.toString());
					exportCSVVO.getQuestionIdList().add(
							String.valueOf(interviewQuestionVO.getQuestionId()
									+"_"+pVO.getNumber()));
				}
		}
		else{
			NodeVO topModule = getTopModuleByTopNodeId(interviewQuestionVO.getTopNodeId());
			StringBuilder header = new StringBuilder();
			header.append(topModule.getName().substring(0, 4));
			header.append("_");
			header.append(interviewQuestionVO.getQuestionId());
			header.append("_");
			header.append(interviewQuestionVO.getNumber());
			headers.add(header.toString());
			exportCSVVO.getQuestionIdList().add(String.valueOf(interviewQuestionVO.getQuestionId()));
		}
	}

	private NodeVO getTopModuleByTopNodeId(long topNodeId) {
		return questionService.getTopModuleByTopNodeId(topNodeId);
	}

	private QuestionVO findInterviewQuestionInMultipleQuestionsNode(
			long questionId) {
		return questionService.findMultipleQuestion(questionId);
	}

	private boolean isModuleOrAjsm(InterviewQuestionVO interviewQuestionVO) {
		return "M".equals(interviewQuestionVO.getNodeClass())
				|| "M_IntroModule".equals(interviewQuestionVO.getType())
				|| "Q_linkedajsm".equals(interviewQuestionVO.getType())
				|| "F_ajsm".equals(interviewQuestionVO.getType());
	}

	
}
