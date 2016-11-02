package org.occideas.assessment.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.POST;
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
import org.occideas.vo.AgentVO;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.FilterModuleVO;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.ReportHistoryVO;
import org.occideas.vo.RuleVO;
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
	
	@POST
    @Path(value = "/exportInterviewsCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportInterviewsCSV(FilterModuleVO filterModuleVO) {
		
		if(filterModuleVO.getFilterModule() == null || 
				filterModuleVO.getFilterModule().length <1){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("No module was selected in the filter dialog.").build();
		}
		
		if(StringUtils.isEmpty(filterModuleVO.getFileName())){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("Filename cannot be empty.").build();
		}
		
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO property = systemPropertyService.
				getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(property == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		String fullPath = "";
		ReportHistoryVO reportHistoryVO = 
				insertToReportHistory(exportFileCSV, fullPath,null,0);
		// filename saved on the report would be different from the one saved in dir
		// this is to avoid overwritten reports
		fullPath = property.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = 	insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0);
		log.info("[Report] before getting unique interview questions ");
		List<InterviewQuestionVO> uniqueInterviewQuestions = interviewQuestionService.getUniqueInterviewQuestions(filterModuleVO.getFilterModule());
		log.info("[Report] after getting unique interview questions ");
		uniqueInterviewQuestions.removeAll(Collections.singleton(null));
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
	@POST
    @Path(value = "/exportAssessmentsCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO property = systemPropertyService.
				getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(property == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		String fullPath = "";
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,null,0);
		fullPath = property.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0);
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithAssessments();
		ExportCSVVO csvVO = populateAssessmentCSV(uniqueInterviews,reportHistoryVO);
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
	@POST
    @Path(value = "/exportAssessmentsNoiseCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsNoiseCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO property = systemPropertyService.
				getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(property == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		String fullPath = "";
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,null,0);
		fullPath = property.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0);
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithRules();
		ExportCSVVO csvVO = populateAssessmentNoiseCSV(uniqueInterviews,reportHistoryVO);
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
	@POST
    @Path(value = "/exportAssessmentsVibrationCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsVibrationCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO property = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(property == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		String fullPath = "";
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,null,0);
		fullPath = property.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0);
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithRules();
		ExportCSVVO csvVO = populateAssessmentVibrationCSV(uniqueInterviews,reportHistoryVO);
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

	private String createFileName(String filename) {
		String exportFileCSV = filename+".csv";
		return exportFileCSV;
	}

	private ExportCSVVO populateCSV(List<InterviewQuestionVO> uniqueInterviewQuestions,ReportHistoryVO reportHistoryVO) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswers(uniqueInterviewQuestions,vo,reportHistoryVO);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessment(uniqueInterviews,vo,reportHistoryVO);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentNoiseCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentNoise(uniqueInterviews,vo,reportHistoryVO);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentVibrationCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentVibration(uniqueInterviews,vo,reportHistoryVO);
		vo.setHeaders(headers);
		return vo;
	}
	private Set<String> populateHeadersAndAnswersAssessmentVibration(List<InterviewVO> uniqueInterviews,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO) {
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 1.11);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		int iSize = uniqueInterviews.size();
		List<SystemPropertyVO> properties = systemPropertyService.getAll();
		AgentVO vibrationAgent = new AgentVO();
		for (SystemPropertyVO prop : properties) {
			if (prop.getType().equalsIgnoreCase("VIBRATIONAGENT")) {
				vibrationAgent.setIdAgent(Long.valueOf(prop.getValue()));
				vibrationAgent.setName(prop.getName());
			}
		}
		headers.add("Shift Length");
		headers.add("Daily Vibration");

		double count = 0;
		for (InterviewVO interviewVO : uniqueInterviews) {
			boolean bFoundVibrationRules = false;
			List<RuleVO> vibrationRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			for (RuleVO rule : interviewVO.getFiredRules()) {
				if (vibrationAgent.getIdAgent() == rule.getAgentId()) {
					vibrationRules.add(rule);
					bFoundVibrationRules = true;
				}
			}
			String shiftHours = "-NA-";
			String dailyvibration = "-NA-";
			
			for (InterviewAnswerVO ia : interviewVO.getAnswerHistory()) {
				if (ia.getType().equalsIgnoreCase("P_frequencyshifthours")) {
					shiftHours = ia.getAnswerFreetext();
					break;
				}
			}
			answers.add(shiftHours);
			if (bFoundVibrationRules) {
				Float totalFrequency = new Float(0);
				Float maxBackgroundPartialExposure = new Float(0);
				Float maxBackgroundHours = new Float(0);
				
				Float level = new Float(0);
				Integer iPeakNoise = 0;
				Float totalPartialExposure = new Float(0);
				for (RuleVO noiseRule : vibrationRules) {											
					Float hours = new Float(0);
					Float frequencyhours = new Float(0);
					PossibleAnswerVO parentNode = noiseRule.getConditions().get(0);
					InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

					if (actualAnswer != null) {
						InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

						if (frequencyHoursNode != null) {
							try{
								frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
							}catch(Exception e){
								System.err.println("Invalid frequency! Check interview "+interviewVO.getInterviewId());
							}
						}
					}
					
					hours = frequencyhours;
					
					try{
						level = Float.valueOf(noiseRule.getRuleAdditionalfields().get(0).getValue());
					}catch(Exception e){
						System.err.println("Invalid noise rule! Check rule "+noiseRule.getIdRule());
					}
					Float partialExposure = (float) Math.sqrt((float)(frequencyhours)*(float)(frequencyhours)*(float)(level)/8);

					totalPartialExposure = ((totalPartialExposure) + (partialExposure));
					totalFrequency += frequencyhours;									
				}

				Float dailyVibration = (float)Math.sqrt((float)(totalFrequency)*(float)(totalFrequency)*(float)(totalPartialExposure)/8);
				dailyvibration = dailyVibration.toString();
				
			}
			answers.add(dailyvibration);
			
			exportCSVVO.getAnswers().put(interviewVO, answers);
		}
		return headers;
	}
	private Set<String> populateHeadersAndAnswersAssessmentNoise(List<InterviewVO> uniqueInterviews,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO) {
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 1.11);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		int iSize = uniqueInterviews.size();
		List<SystemPropertyVO> properties = systemPropertyService.getAll();
		AgentVO noiseAgent = new AgentVO();
		for (SystemPropertyVO prop : properties) {
			if (prop.getType().equalsIgnoreCase("NOISEAGENT")) {
				noiseAgent.setIdAgent(Long.valueOf(prop.getValue()));
				noiseAgent.setName(prop.getName());
			}
		}
		headers.add("Shiftlength");
		headers.add("Total Exposure");
		headers.add("LAEQ8");
		headers.add("Peak Noise");
		headers.add("Ratio");

		double count = 0;
		for (InterviewVO interviewVO : uniqueInterviews) {
			boolean bFoundNoiseRules = false;
			List<RuleVO> noiseRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			for (RuleVO rule : interviewVO.getFiredRules()) {
				if (noiseAgent.getIdAgent() == rule.getAgentId()) {
					noiseRules.add(rule);
					bFoundNoiseRules = true;
				}
			}
			String shiftHours = "-NA-";
			String totalExposure = "-NA-";
			String laeq8 = "-NA-";
			String peakNoise = "-NA-";
			String strRatio = "-NA-";

			for (InterviewAnswerVO ia : interviewVO.getAnswerHistory()) {
				if (ia.getType().equalsIgnoreCase("P_frequencyshifthours")) {
					shiftHours = ia.getAnswerFreetext();
					break;
				}
			}
			answers.add(shiftHours);
			if (bFoundNoiseRules) {
				Float totalFrequency = new Float(0);
				Float maxBackgroundPartialExposure = new Float(0);
				Float maxBackgroundHours = new Float(0);
				for (RuleVO noiseRule : noiseRules) {
					if (!noiseRule.getType().equalsIgnoreCase("BACKGROUND")) {
						PossibleAnswerVO parentNode = noiseRule.getConditions().get(0);
						InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

						String answeredValue = "0";
						if (actualAnswer != null) {
							InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

							if (frequencyHoursNode != null) {
								answeredValue = frequencyHoursNode.getAnswerFreetext();
							}
						}
						try{
							totalFrequency += Float.valueOf(answeredValue);	
						}catch(Exception e){
							System.err.println("Invalid not bg frequency! Check interview "+interviewVO.getInterviewId()+" Rule "+noiseRule.getIdRule());
						}
						
					}
				}
				boolean useRatio = false;
				Float ratio = new Float(1);
				Float fShiftHours = Float.valueOf(shiftHours);
				if (totalFrequency > fShiftHours) {
					useRatio = true;
					ratio = totalFrequency / fShiftHours;
				}
				Integer level = 0;
				Integer iPeakNoise = 0;
				Float totalPartialExposure = new Float(0);
				for (RuleVO noiseRule : noiseRules) {

					if (noiseRule.getType().equalsIgnoreCase("BACKGROUND")) {
						Float hoursbg = fShiftHours - totalFrequency;
						if (hoursbg < 0) {
							hoursbg = new Float(0);
						}
						Float partialExposure = (float) (4 * hoursbg * (Math.pow(10, (level - 100) / 10)));
						try{
							String sLevel = noiseRule.getRuleAdditionalfields().get(0).getValue();
							level = Integer.valueOf(sLevel);
						}catch(Exception e){
							System.err.println("Invalid noise rule! Check rule "+noiseRule.getIdRule());
						}
						if (partialExposure > maxBackgroundPartialExposure) {
							maxBackgroundPartialExposure = partialExposure;
							maxBackgroundHours = hoursbg;
						}
					} else {
						
						Float hours = new Float(0);
						Float frequencyhours = new Float(0);
						PossibleAnswerVO parentNode = noiseRule.getConditions().get(0);
						InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

						if (actualAnswer != null) {
							InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

							if (frequencyHoursNode != null) {
								try{
									frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
								}catch(Exception e){
									System.err.println("Invalid frequency! Check interview "+interviewVO.getInterviewId());
								}
							}
						}
						if (useRatio) {
							hours = frequencyhours / ratio;
						} else {
							hours = frequencyhours;
						}
						try{
							level = Integer.valueOf(noiseRule.getRuleAdditionalfields().get(0).getValue());
						}catch(Exception e){
							System.err.println("Invalid noise rule! Check rule "+noiseRule.getIdRule());
						}
						Float partialExposure = (float) ((float) 4 * hours * (Math.pow(10, (float)(level - 100) / (float)10)));
						//System.out.println(parentNode.getNumber() + " " + parentNode.getIdNode() + " "
						//		+ parentNode.getName() + " " + level + " " + hours + " " + partialExposure);
						totalPartialExposure = ((totalPartialExposure) + (partialExposure));

					}
					if (iPeakNoise < level) {
						iPeakNoise = level;
					}
				}
				totalPartialExposure = ((totalPartialExposure) + (maxBackgroundPartialExposure));
				// totalPartialExposure = totalPartialExposure.toFixed(4);
				totalFrequency += maxBackgroundHours;

				Float autoExposureLevel = (float) (10 * (Math.log10(totalPartialExposure / (3.2 * (Math.pow(10, -9))))));
				// autoExposureLevel = autoExposureLevel.toFixed(4);

				totalExposure = totalPartialExposure.toString();
				laeq8 = autoExposureLevel.toString();
				peakNoise = iPeakNoise.toString();
				strRatio = ratio.toString();
			}
			answers.add(totalExposure);
			answers.add(laeq8);
			answers.add(peakNoise);
			answers.add(strRatio.toString());
			exportCSVVO.getAnswers().put(interviewVO, answers);
		}
		return headers;
	}
	private InterviewAnswerVO findFrequencyInterviewAnswer(InterviewVO interview,InterviewAnswerVO actualAnswer) {
		InterviewAnswerVO retValue = null;
		InterviewQuestionVO piq = null;
		for(InterviewQuestionVO iq: interview.getQuestionHistory()){
			if(iq.getParentAnswerId()>0){
				if(iq.getParentAnswerId()==actualAnswer.getAnswerId()){
					piq = iq;
					break;
				}
			}		
		}
		if(piq!=null){
			for(InterviewAnswerVO ia: interview.getAnswerHistory()){
				if(ia.getParentQuestionId()==piq.getQuestionId()){
					retValue = ia;
				}
			}
		}
		return retValue;
	}
	private InterviewAnswerVO findInterviewAnswer(List<InterviewAnswerVO> answerHistory, PossibleAnswerVO parentNode) {
		InterviewAnswerVO retValue = null;
		for(InterviewAnswerVO ia: answerHistory){
			if(ia.getAnswerId()==parentNode.getIdNode()){
				retValue = ia;
			}
		}
		return retValue;
	}
	private Set<String> populateHeadersAndAnswersAssessment(List<InterviewVO> uniqueInterviews,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO) {
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 1.11);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");		
		int iSize = uniqueInterviews.size();
		List<SystemPropertyVO> properties = systemPropertyService.getAll();
		List<AgentVO> agents = new ArrayList<AgentVO>();
		for (SystemPropertyVO prop : properties) {
			if (prop.getType().equalsIgnoreCase("STUDYAGENT")) {
				AgentVO agent = new AgentVO();
				agent.setIdAgent(Long.valueOf(prop.getValue()));
				agent.setName(prop.getName());
				agents.add(agent);
				
			}
		}
		for(AgentVO agent:agents){
			headers.add(agent.getName()+"_MANUAL");
			headers.add(agent.getName()+"_AUTO");
		}
		double count = 0;
		for (InterviewVO interviewVO : uniqueInterviews) {
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			for(AgentVO agent:agents){
				boolean aAgentFound = false;
				for(RuleVO rule:interviewVO.getManualAssessedRules()){
					if(agent.getIdAgent()==rule.getAgentId()){
						answers.add(rule.getLevel());
						aAgentFound = true;
						break;
					}
				}
				if(!aAgentFound){
					answers.add("-NA-");
				}
				aAgentFound = false;
				for(RuleVO rule:interviewVO.getAutoAssessedRules()){
					if(agent.getIdAgent()==rule.getAgentId()){
						answers.add(rule.getLevel());
						aAgentFound = true;
						break;
					}
				}
				if(!aAgentFound){
					answers.add("-NA-");
				}
			}
			exportCSVVO.getAnswers().put(interviewVO, answers);
		}
		return headers;
	}
	private Set<String> populateHeadersAndAnswers(List<InterviewQuestionVO> uniqueInterviewQuestions
				,ExportCSVVO exportCSVVO,ReportHistoryVO reportHistoryVO) {
		updateProgress(reportHistoryVO,ReportsStatusEnum.IN_PROGRESS.getValue(),1.11);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		ArrayList<Long> uniqueInterviews = new ArrayList<Long>();
		int iUniqueColumeCount = 0;
		int iSize = uniqueInterviewQuestions.size();
		if(iSize > 0){
			//sort interviewquestion
			uniqueInterviewQuestions = sortInterviewQuestions(uniqueInterviewQuestions);
		}
		for(InterviewQuestionVO interviewQuestionVO:uniqueInterviewQuestions){
			addHeaders(headers, interviewQuestionVO,exportCSVVO);
			iUniqueColumeCount++;
			if(!uniqueInterviews.contains(interviewQuestionVO.getIdInterview())){
				uniqueInterviews.add(interviewQuestionVO.getIdInterview());
			}
		}
		updateProgress(reportHistoryVO,ReportsStatusEnum.IN_PROGRESS.getValue(),2.22);
		double count = 0;	
		int interviewCount = 0;
		int interviewSize = uniqueInterviews.size();
		List<InterviewVO> interviewQuestionAnswer = null;
		List<InterviewVO> runningInterviews = new ArrayList<>();
		for(InterviewQuestionVO interviewQuestionVO:uniqueInterviewQuestions){
			count++;
			
			long interviewId = interviewQuestionVO.getIdInterview();
			InterviewVO interview = new InterviewVO();
			interview.setInterviewId(interviewId);
			if(!runningInterviews.contains(interview)){
				interviewQuestionAnswer = interviewService.getInterviewQuestionAnswer(interviewId);
				runningInterviews.addAll(interviewQuestionAnswer);
				interviewCount++;
				System.out.println("[Report] New interview "+interviewCount+" of "+interviewSize+" at "+new Date());
				double progress = (Float.valueOf(interviewCount)/Float.valueOf(interviewSize))*100;
				updateProgress(reportHistoryVO,ReportsStatusEnum.IN_PROGRESS.getValue(),progress);
			}else{
				interviewQuestionAnswer = new ArrayList<InterviewVO>();
				for(InterviewVO interviewVO:runningInterviews){
					if(interviewVO.getInterviewId()==interviewId){
						interviewQuestionAnswer.add(interviewVO);
					}
				}
			}
			
			for(InterviewVO interviewVO:interviewQuestionAnswer){
				List<String> answers = new ArrayList<>();
				answers.add(String.valueOf(interviewVO.getInterviewId()));
				answers.add(String.valueOf(interviewVO.getReferenceNumber()));
				answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
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
									answers.add("-NoA-");
								}
							}
						}else{
							answers.add("-QNotAsked-");
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
								answers.add("-NoA-");
							}
						}else{
							answers.add("-QNotAsked-");
						}
					}				
				}
				exportCSVVO.getAnswers().put(interviewVO, answers);
			}
		}
		return headers;
	}

	private List<InterviewQuestionVO> sortInterviewQuestions(List<InterviewQuestionVO> uniqueInterviewQuestions) {
		Map<String,List<InterviewQuestionVO>> map = new LinkedHashMap<>();
		// sort the questions by its top node id
		for(InterviewQuestionVO iqv:uniqueInterviewQuestions){
			if(!map.containsKey(String.valueOf(iqv.getTopNodeId()))){
				map.put(String.valueOf(iqv.getTopNodeId()), 
						new LinkedList<InterviewQuestionVO>());
			}
			map.get(String.valueOf(iqv.getTopNodeId())).add(iqv);
		}
		// sort and merge by number
		List<InterviewQuestionVO> resultList = new LinkedList<>();
		for (Map.Entry<String,List<InterviewQuestionVO>> entry : map.entrySet()) {
			List<InterviewQuestionVO> value = entry.getValue();
			InterviewQuestionVO firstElement = value.remove(0);
			Collections.sort(value, new Comparator<InterviewQuestionVO>(){
			     public int compare(InterviewQuestionVO o1, InterviewQuestionVO o2){
			    	 
			    	 	StringBuilder sbO1 = new StringBuilder();
			    	    for (char c : o1.getNumber().toCharArray()){
			    	    	sbO1.append((int)c);
			    	    }
			    	    BigInteger o1Int = new BigInteger(sbO1.toString());
			    	    
			    	    StringBuilder sbO2 = new StringBuilder();
			    	    for (char c : o2.getNumber().toCharArray()){
			    	    	sbO2.append((int)c);
			    	    }
			    	    BigInteger o2Int = new BigInteger(sbO2.toString());
			    	 if(o1Int.intValue() == o2Int.intValue()){
			             return 0;
			         }
			         return o1Int.intValue() < o2Int.intValue() ? -1 : 1;
			     }
			});
			value.add(0, firstElement);
			resultList.addAll(value);
		}
		
		return resultList;
	}

	private void addHeaders(Set<String> headers, 
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
				|| "Q_linkedmodule".equals(interviewQuestionVO.getType())
				|| "F_ajsm".equals(interviewQuestionVO.getType());
	}

	
}
