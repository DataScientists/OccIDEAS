package org.occideas.assessment.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import org.occideas.utilities.QuestionComparator;
import org.occideas.utilities.ReportsEnum;
import org.occideas.utilities.ReportsStatusEnum;
import org.occideas.vo.AgentVO;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.FilterModuleVO;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewIntroModuleModuleVO;
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

	//Default duration in minutes
	private static final float INITIAL_DURATION_MIN = 60;

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
		
	private DecimalFormat df = new DecimalFormat("#.0");
	
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
		
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		
		//check if we have the directory TreeSet ins sys prop
		if(csvDir == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		
		ReportHistoryVO reportHistoryVO = 
				insertToReportHistory(exportFileCSV, "",null,0, ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());
		// filename saved on the report would be different from the one saved in dir
		// this is to avoid overwritten reports
		String fullPath = csvDir.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, 
					fullPath, reportHistoryVO.getId(),0, ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());
		
		Long count = interviewQuestionService.getUniqueInterviewQuestionCount(filterModuleVO.getFilterModule());
		
		reportHistoryVO.setRecordCount(count);
		
		updateProgress(reportHistoryVO, 
				ReportsStatusEnum.IN_PROGRESS.getValue(), 
				0.11, new Date(), INITIAL_DURATION_MIN);	
		
		log.info("[Report] before getting unique interview questions ");
		List<InterviewQuestionVO> uniqueInterviewQuestions = interviewQuestionService.getUniqueInterviewQuestions(filterModuleVO.getFilterModule());
		log.info("[Report] after getting unique interview questions ");
		
		ExportCSVVO csvVO = populateCSV(uniqueInterviewQuestions,reportHistoryVO,filterModuleVO.getFilterModule());
		
		writeReport(fullPath, reportHistoryVO, csvVO);
		
		return Response.ok().build();
    }

	private void writeReport(String fullPath, ReportHistoryVO reportHistoryVO, ExportCSVVO csvVO) {
		CSVWriter writer;
		try {
			SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
			File csvDirFile = new File(csvDir.getValue());
			
			if(!csvDirFile.exists()){				
				csvDirFile.mkdir();
			}
			File file = new File(fullPath);
			writer = new CSVWriter(new FileWriter(file), ',');
			// feed in your array (or convert your data to an array)
			String[] headers = Arrays.copyOf(csvVO.getHeaders().toArray(), csvVO.getHeaders().toArray().length,
					String[].class);
			// write header
			writer.writeNext(headers);
			// write answers
			// iterate map
			Iterator<Entry<InterviewVO, List<String>>> listOfAnswers = csvVO.getAnswers().entrySet().iterator();
			while (listOfAnswers.hasNext()) {
				
				List<String> value = (List<String>) listOfAnswers.next().getValue();
				String[] answers = Arrays.copyOf(value.toArray(), value.toArray().length, String[].class);
				writer.writeNext(answers);
			}
			writer.close();			
			updateProgress(reportHistoryVO, ReportsStatusEnum.COMPLETED.getValue(), 100);
		} catch (IOException e) {
			e.printStackTrace();
			updateProgress(reportHistoryVO, ReportsStatusEnum.FAILED.getValue(), 0);
		}
	}
	@POST
    @Path(value = "/exportAssessmentsCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(csvDir == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "",null,0, ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0, ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());
		
		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());
		
		reportHistoryVO.setRecordCount(count);
		
		updateProgress(reportHistoryVO, 
				ReportsStatusEnum.IN_PROGRESS.getValue(), 
				0.11, new Date(), INITIAL_DURATION_MIN);
		
		long msPerInterview = getMsPerInterview(count.intValue(), 
				reportHistoryVO, ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue(), 0.2);		
		
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithAssessments(filterModuleVO.getFilterModule());
		
		ExportCSVVO csvVO = populateAssessmentCSV(uniqueInterviews,reportHistoryVO, msPerInterview, filterModuleVO.getFilterModule());
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
    }
	@POST
    @Path(value = "/exportAssessmentsNoiseCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsNoiseCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop		
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(csvDir == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null,0, ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0, ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue());
		
		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());
		
		reportHistoryVO.setRecordCount(count);
		
		updateProgress(reportHistoryVO, 
				ReportsStatusEnum.IN_PROGRESS.getValue(), 
				0.11, new Date(), INITIAL_DURATION_MIN);		
		
		long msPerInterview = getMsPerInterview(count.intValue(), 
				reportHistoryVO, ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue(), 0.2);	
						
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
		
		ExportCSVVO csvVO = populateAssessmentNoiseCSV(uniqueInterviews,reportHistoryVO, msPerInterview);
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
    }
	@POST
    @Path(value = "/exportAssessmentsVibrationCSV")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response exportAssessmentsVibrationCSV(FilterModuleVO filterModuleVO) {
		
		//check if we have the directory TreeSet ins sys prop		
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if(csvDir == null){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());
		
		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "",null,0, ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue()+reportHistoryVO.getId()+"_"+exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath,reportHistoryVO.getId(),0, ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue());
		
		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());
		
		reportHistoryVO.setRecordCount(count);
		
		updateProgress(reportHistoryVO, 
				ReportsStatusEnum.IN_PROGRESS.getValue(), 
				0.11, new Date(), INITIAL_DURATION_MIN);
				
		long msPerInterview = getMsPerInterview(count.intValue(), 
				reportHistoryVO, ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue(), 0.2);
		
		List<InterviewVO> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
		ExportCSVVO csvVO = populateAssessmentVibrationCSV(uniqueInterviews,reportHistoryVO, msPerInterview);
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
    }
	private ReportHistoryVO insertToReportHistory(String exportFileCSV, 
			String fullPath,
			Long id,
			int progress,
			String type) {
		
		ReportHistoryVO reportHistoryVO = new ReportHistoryVO();
		if(id !=null){
			reportHistoryVO.setId(id);
		}
		String user = extractUserFromToken();
		reportHistoryVO.setName(exportFileCSV);
		reportHistoryVO.setPath(fullPath);
		reportHistoryVO.setRequestor(user);
		reportHistoryVO.setStatus(ReportsStatusEnum.IN_PROGRESS.getValue());
		reportHistoryVO.setType(type);
		reportHistoryVO.setUpdatedBy(user);
		reportHistoryVO.setProgress(progress+"%");
		return reportHistoryService.save(reportHistoryVO);
	}
	
	private ReportHistoryVO updateProgress(ReportHistoryVO reportHistoryVO, 
			String status,double progress, Date startDate, float duration) {
		
		reportHistoryVO.setStatus(status);
		
		if(progress != 0){
			reportHistoryVO.setProgress(df.format(progress) +"%");
			if(startDate != null){
				reportHistoryVO.setStartDt(startDate);
			}
			
			reportHistoryVO.setDuration(Float.parseFloat(df.format(duration)));
			
		}
		return reportHistoryService.save(reportHistoryVO);
	}
	
	private ReportHistoryVO updateProgress(ReportHistoryVO reportHistoryVO, 
			String status, double progress) {
				
		Date endDate = null;
		
		if(!status.equalsIgnoreCase(ReportsStatusEnum.IN_PROGRESS.getValue())){
			endDate = new Date();
			
			reportHistoryVO.setDuration(
					(float) (endDate.getTime() - reportHistoryVO.getStartDt().getTime())
					/60/1000);
			
			reportHistoryVO.setEndDt(endDate);
		}
		return updateProgress(reportHistoryVO, status, progress, 
				reportHistoryVO.getStartDt(), reportHistoryVO.getDuration());
	}
	
	private String extractUserFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse)SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		return tokenManager.parseUsernameFromToken(token);
	}

	private String createFileName(String filename) {
		
		return filename+".csv";
	}

	private ExportCSVVO populateCSV(List<InterviewQuestionVO> uniqueInterviewQuestions,ReportHistoryVO reportHistoryVO, String[] modules) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswers(uniqueInterviewQuestions,vo,reportHistoryVO,modules);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO, 
			long msPerInterview, String[] modules) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessment(uniqueInterviews,vo,reportHistoryVO, msPerInterview, modules);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentNoiseCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO,
			long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentNoise(uniqueInterviews,vo,reportHistoryVO, msPerInterview);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentVibrationCSV(List<InterviewVO> uniqueInterviews,ReportHistoryVO reportHistoryVO,
			long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentVibration(uniqueInterviews,vo,reportHistoryVO,msPerInterview);
		vo.setHeaders(headers);
		return vo;
	}
	private Set<String> populateHeadersAndAnswersAssessmentVibration(List<InterviewVO> uniqueInterviews,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO, long msPerInterview) {
		
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.3);
		
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		headers.add("Intro Module Name");		
		headers.add("Job Module Name");		
		headers.add("Shift Length");
		headers.add("Daily Vibration");
		
		AgentVO vibrationAgent = getAgent("VIBRATIONAGENT");
		
		for (InterviewVO interviewVO : uniqueInterviews) {
			
			boolean bFoundVibrationRules = false;
			List<RuleVO> vibrationRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			
			addModuleNames(interviewVO, answers);
			
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
				
				Float level = new Float(0);
				Float totalPartialExposure = new Float(0);
				for (RuleVO noiseRule : vibrationRules) {											
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

	private void addModuleNames(InterviewVO interviewVO, List<String> answers) {
		
		if(interviewVO.getModuleList() != null){
			answers.add(getIntroModuleName(interviewVO.getModuleList().get(0)));
			answers.add(getModuleName((interviewVO.getModuleList().size() > 1) ? interviewVO.getModuleList().get(1) : null));	
		}		
	}
	private Set<String> populateHeadersAndAnswersAssessmentNoise(List<InterviewVO> uniqueInterviews,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO, long msPerInterview) {
	
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.2);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");		
		headers.add("Module");
		headers.add("Job Module Name");
		headers.add("Shiftlength");
		headers.add("Total Exposure");
		headers.add("LAEQ8");
		headers.add("Peak Noise");
		headers.add("Ratio");
		
		AgentVO noiseAgent = getAgent("NOISEAGENT");		
		
		for (InterviewVO interviewVO : uniqueInterviews) {
			
			boolean bFoundNoiseRules = false;
			List<RuleVO> noiseRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			
			addModuleNames(interviewVO, answers);
			
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

	private AgentVO getAgent(String type) {
		List<SystemPropertyVO> properties = systemPropertyService.getByType(type);
		AgentVO noiseAgent = new AgentVO();
		
		if(properties != null && properties.size() > 0){
			noiseAgent.setIdAgent(Long.valueOf(properties.get(0).getValue()));
			noiseAgent.setName(properties.get(0).getName());	
		}
			
		return noiseAgent;
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
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO, long msPerInterview, String[] modules) {
	
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.1);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		headers.add("Intro Module Name");
		headers.add("Job Module Name");
		
		List<SystemPropertyVO> properties = systemPropertyService.getByType("STUDYAGENT");
		List<AgentVO> agents = new ArrayList<AgentVO>();
		
		for (SystemPropertyVO prop : properties) {			
			AgentVO agent = new AgentVO();
			agent.setIdAgent(Long.valueOf(prop.getValue()));
			agent.setName(prop.getName());
			agents.add(agent);
			
			headers.add(agent.getName()+"_MANUAL");
			headers.add(agent.getName()+"_AUTO");
		}
		
		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		//Initialize map to prevent multiple re-queries
		for(String module : modules){
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));			
		}
		
		for (InterviewVO interviewVO : uniqueInterviews) {			
			
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
						
			addModuleNames(interviewVO, answers);
			
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
	private String getModuleName(InterviewIntroModuleModuleVO interviewIntroModuleModuleVO) {
		
		return (interviewIntroModuleModuleVO == null) ? 
				"" : 
				(interviewIntroModuleModuleVO.getInterviewModuleName() == null)
					? " "
					: interviewIntroModuleModuleVO.getInterviewModuleName().substring(0, 4);
	}

	private String getIntroModuleName(InterviewIntroModuleModuleVO interviewIntroModuleModuleVO) {
		
		return (interviewIntroModuleModuleVO == null) ? 
				"" : 
				(interviewIntroModuleModuleVO.getIntroModuleNodeName() == null)
					? " "
					: interviewIntroModuleModuleVO.getIntroModuleNodeName().substring(0, 4);
	}

	private Set<String> populateHeadersAndAnswers(List<InterviewQuestionVO> uniqueInterviewQuestions
				,ExportCSVVO exportCSVVO,ReportHistoryVO reportHistoryVO, String[] modules) {
		
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.2);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		ArrayList<Long> uniqueInterviews = new ArrayList<Long>();			
		
		if(uniqueInterviewQuestions.size() > 0){
			//sort interview question
			uniqueInterviewQuestions = sortInterviewQuestions(uniqueInterviewQuestions);
		}
		
		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		//Initialize map to prevent multiple re-queries
		for(String module : modules){
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));			
		}	
		List<InterviewVO> interviewQuestionAnswer = null;
		
		for(InterviewQuestionVO interviewQuestionVO:uniqueInterviewQuestions){
		
			//Build header and answers
			addHeadersAndAnswers(headers, interviewQuestionVO, exportCSVVO, 
					nodeVoList.get(Long.valueOf(interviewQuestionVO.getTopNodeId())));
		
			if(!uniqueInterviews.contains(interviewQuestionVO.getIdInterview())){ 
				uniqueInterviews.add(interviewQuestionVO.getIdInterview());
				reportHistoryVO.setRecordCount(uniqueInterviews.size());
			}
		}

		//Consolidate answers for all interviews, fill not answered and not asked questions
		for(Long interviewId : uniqueInterviews){
						
			interviewQuestionAnswer = interviewService.getInterviewQuestionAnswer(interviewId);			
			
			InterviewVO interviewVO = interviewQuestionAnswer.get(0);
				
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getInterviewId()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(interviewVO.getParticipant().getStatusDescription()));
			
			Set<String> questionIdList = exportCSVVO.getQuestionIdList();
			
			for(String questionId:questionIdList){
				
				if(questionId.contains("_")){
					
					handleMultiQuestion(interviewVO, answers, questionId);
					
				}else{					
					handleSimpleQuestion(interviewVO, answers, questionId);
				}				
			}
			
			exportCSVVO.getAnswers().put(interviewVO, answers);			
		}
		
		return headers;
	}

	private void handleSimpleQuestion(InterviewVO interviewVO, List<String> answers, String questionId) {
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

	private void handleMultiQuestion(InterviewVO interviewVO, List<String> answers, String questionId) {
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
	}

	/**
	 * Estimate duration from the past completed report
	 * @param size
	 * @param reportHistoryVO
	 * @param msPerInterview
	 * @return
	 */
	private long getMsPerInterview(int size, ReportHistoryVO reportHistoryVO,
			String type, double progress) {
		
		long msPerInterview = 0;
		ReportHistoryVO historicalReport = reportHistoryService.getLatestByType(type);
		if(historicalReport != null){
			msPerInterview = (long) ((historicalReport.getDuration() * 60 * 1000)/historicalReport.getRecordCount());									
			reportHistoryVO.setDuration((size * msPerInterview)/(60*1000));
			updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), progress);
		}
		return msPerInterview;
	}	

	private List<InterviewQuestionVO> sortInterviewQuestions(List<InterviewQuestionVO> uniqueInterviewQuestions) {
		Map<String,List<InterviewQuestionVO>> map = new LinkedHashMap<>();
		// Consolidate the questions by its top node id
		for(InterviewQuestionVO iqv:uniqueInterviewQuestions){
			if(!map.containsKey(String.valueOf(iqv.getTopNodeId()))){
				map.put(String.valueOf(iqv.getTopNodeId()), 
						new LinkedList<InterviewQuestionVO>());
			}
			map.get(String.valueOf(iqv.getTopNodeId())).add(iqv);
		}
		// Sort and merge by number
		List<InterviewQuestionVO> resultList = new LinkedList<>();
		for (Map.Entry<String,List<InterviewQuestionVO>> entry : map.entrySet()) {
			List<InterviewQuestionVO> value = entry.getValue();
			InterviewQuestionVO firstElement = value.remove(0);
			Collections.sort(value, new QuestionComparator());
			value.add(0, firstElement);			
			resultList.addAll(value);
		}
		
		return resultList;
	}

	private void addHeadersAndAnswers(Set<String> headers, 
			InterviewQuestionVO interviewQuestionVO, ExportCSVVO exportCSVVO, NodeVO topModule) {

		if(isModuleOrAjsm(interviewQuestionVO)){
			//Do nothing?
			//headers.add(interviewQuestionVO.getName().substring(0, 4));
		}
		else if("Q_multiple".equals(interviewQuestionVO.getType())){
				QuestionVO qVO = findInterviewQuestionInMultipleQuestionsNode
						(interviewQuestionVO.getQuestionId());
				if(qVO == null){
					System.out.println("Something wrong with our data");
					return;
				}
				
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
