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
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.entity.Rule;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.mapper.RuleMapperImpl;
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
import org.occideas.vo.NodeVO;
import org.occideas.vo.ReportHistoryVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opencsv.CSVWriter;

@Path("/assessment")
public class AssessmentRestController {

	//Default duration in minutes
	private static final float INITIAL_DURATION_MIN = 60;
	
	private static final long MS_PER_MIN = 60 * 1000;

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
		List<InterviewQuestion> uniqueInterviewQuestions = interviewQuestionService.getUniqueInterviewQuestions(filterModuleVO.getFilterModule());
		log.info("[Report] after getting unique interview questions ");
		
		ExportCSVVO csvVO = populateCSV(uniqueInterviewQuestions,reportHistoryVO,filterModuleVO.getFilterModule(), count);
		
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
			Iterator<Entry<Interview, List<String>>> listOfAnswers = csvVO.getAnswers().entrySet().iterator();
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
		
		List<Interview> uniqueInterviews = interviewService.listAllWithAssessments(filterModuleVO.getFilterModule());
		
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
						
		List<Interview> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
		
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
		
		List<Interview> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
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
			if(startDate != null && reportHistoryVO.getStartDt() == null){
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
					/MS_PER_MIN);
			
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

	private ExportCSVVO populateCSV(List<InterviewQuestion> uniqueInterviewQuestions,ReportHistoryVO reportHistoryVO, String[] modules, 
			Long count) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswers(uniqueInterviewQuestions,vo,reportHistoryVO,modules, count);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentCSV(List<Interview> uniqueInterviews,ReportHistoryVO reportHistoryVO, 
			long msPerInterview, String[] modules) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessment(uniqueInterviews,vo,reportHistoryVO, msPerInterview, modules);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentNoiseCSV(List<Interview> uniqueInterviews,ReportHistoryVO reportHistoryVO,
			long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentNoise(uniqueInterviews,vo,reportHistoryVO, msPerInterview);
		vo.setHeaders(headers);
		return vo;
	}
	private ExportCSVVO populateAssessmentVibrationCSV(List<Interview> uniqueInterviews,ReportHistoryVO reportHistoryVO,
			long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentVibration(uniqueInterviews,vo,reportHistoryVO,msPerInterview);
		vo.setHeaders(headers);
		return vo;
	}
	private Set<String> populateHeadersAndAnswersAssessmentVibration(List<Interview> uniqueInterviews,
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
		
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0; 
		int currentCount = 0;
		
		AgentVO vibrationAgent = getAgent("VIBRATIONAGENT");
		
		for (Interview interviewVO : uniqueInterviews) {
			
			currentCount++;
			
			updateProgress(uniqueInterviews.size(), 
					reportHistoryVO, 
					currentCount, 
					elapsedTime, 
					msPerInterview);
			
			boolean bFoundVibrationRules = false;
			List<Rule> vibrationRules = new ArrayList<Rule>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus())));
			
			addModuleNames(interviewVO, answers);
			
			for (Rule rule : interviewVO.getFiredRules()) {
				if (vibrationAgent.getIdAgent() == rule.getAgentId()) {
					vibrationRules.add(rule);
					bFoundVibrationRules = true;
				}
			}
			String shiftHours = "-NA-";
			String dailyvibration = "-NA-";
			
			for (InterviewAnswer ia : interviewVO.getAnswerHistory()) {
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
				for (Rule noiseRule : vibrationRules) {											
					Float frequencyhours = new Float(0);
					PossibleAnswer parentNode = noiseRule.getConditions().get(0);
					InterviewAnswer actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

					if (actualAnswer != null) {
						InterviewAnswer frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

						if (frequencyHoursNode != null) {
							try{
								frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
							}catch(Exception e){
								System.err.println("Invalid frequency! Check interview "+interviewVO.getIdinterview());
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
			
			elapsedTime = System.currentTimeMillis() - startTime;
		}
		
		return headers;
	}

	private void addModuleNames(Interview interviewVO, List<String> answers) {
		
		if(interviewVO.getModuleList() != null){
			answers.add(getIntroModuleName(interviewVO.getModuleList().get(0)));
			answers.add(getModuleName((interviewVO.getModuleList().size() > 1) ? interviewVO.getModuleList().get(1) : null));	
		}		
	}
	private Set<String> populateHeadersAndAnswersAssessmentNoise(List<Interview> uniqueInterviews,
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

		long startTime = System.currentTimeMillis();
		long elapsedTime = 0; 
		int currentCount = 0;
		
		AgentVO noiseAgent = getAgent("NOISEAGENT");		
		
		for (Interview interviewVO : uniqueInterviews) {
			
			currentCount++;
			
			updateProgress(uniqueInterviews.size(), 
					reportHistoryVO, 
					currentCount, 
					elapsedTime,
					msPerInterview);
			
			boolean bFoundNoiseRules = false;
			List<Rule> noiseRules = new ArrayList<Rule>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus())));
			
			addModuleNames(interviewVO, answers);
			
			for (Rule rule : interviewVO.getFiredRules()) {
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

			for (InterviewAnswer ia : interviewVO.getAnswerHistory()) {
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
				for (Rule noiseRule : noiseRules) {
					if (!noiseRule.getType().equalsIgnoreCase("BACKGROUND")) {
						PossibleAnswer parentNode = noiseRule.getConditions().get(0);
						InterviewAnswer actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

						String answeredValue = "0";
						if (actualAnswer != null) {
							InterviewAnswer frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

							if (frequencyHoursNode != null) {
								answeredValue = frequencyHoursNode.getAnswerFreetext();
							}
						}
						try{
							totalFrequency += Float.valueOf(answeredValue);	
						}catch(Exception e){
							System.err.println("Invalid not bg frequency! Check interview "+interviewVO.getIdinterview()+" Rule "+noiseRule.getIdRule());
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
				for (Rule noiseRule : noiseRules) {

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
						PossibleAnswer parentNode = noiseRule.getConditions().get(0);
						InterviewAnswer actualAnswer = findInterviewAnswer(interviewVO.getAnswerHistory(),parentNode);

						if (actualAnswer != null) {
							InterviewAnswer frequencyHoursNode = findFrequencyInterviewAnswer(interviewVO,actualAnswer);

							if (frequencyHoursNode != null) {
								try{
									frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
								}catch(Exception e){
									System.err.println("Invalid frequency! Check interview "+interviewVO.getIdinterview());
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
			
			elapsedTime = System.currentTimeMillis() - startTime;
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
	private InterviewAnswer findFrequencyInterviewAnswer(Interview interview, InterviewAnswer actualAnswer) {
		InterviewAnswer retValue = null;
		InterviewQuestion piq = null;
		for(InterviewQuestion iq: interview.getQuestionHistory()){
			if(iq.getParentAnswerId()>0){
				if(iq.getParentAnswerId()==actualAnswer.getAnswerId()){
					piq = iq;
					break;
				}
			}		
		}
		if(piq!=null){
			for(InterviewAnswer ia: interview.getAnswerHistory()){
				if(ia.getParentQuestionId()==piq.getQuestionId()){
					retValue = ia;
				}
			}
		}
		return retValue;
	}
	private InterviewAnswer findInterviewAnswer(List<InterviewAnswer> answerHistory, PossibleAnswer parentNode) {
		InterviewAnswer retValue = null;
		for(InterviewAnswer ia: answerHistory){
			if(ia.getAnswerId()==parentNode.getIdNode()){
				retValue = ia;
			}
		}
		return retValue;
	}
	private Set<String> populateHeadersAndAnswersAssessment(List<Interview> uniqueInterviews,
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
		
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0; 
		int currentCount = 0;
		
		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		//Initialize map to prevent multiple re-queries
		for(String module : modules){
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));			
		}
		
		for (Interview interviewVO : uniqueInterviews) {
			
			currentCount++;
			
			updateProgress(uniqueInterviews.size(), 
					reportHistoryVO, 
					currentCount, 
					elapsedTime,
					msPerInterview);
			
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus())));
						
			addModuleNames(interviewVO, answers);
			
			for(AgentVO agent:agents){
				boolean aAgentFound = false;
				for(Rule rule:interviewVO.getManualAssessedRules()){
					if(agent.getIdAgent()==rule.getAgentId()){
						answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
						aAgentFound = true;
						break;
					}
				}
				if(!aAgentFound){
					answers.add("-NA-");
				}
				aAgentFound = false;
				for(Rule rule:interviewVO.getAutoAssessedRules()){
					if(agent.getIdAgent()==rule.getAgentId()){
						answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
						aAgentFound = true;
						break;
					}
				}
				if(!aAgentFound){
					answers.add("-NA-");
				}
			}
			exportCSVVO.getAnswers().put(interviewVO, answers);
			
			elapsedTime = System.currentTimeMillis() - startTime;
		}
		
		return headers;
	}
	private String getModuleName(InterviewIntroModuleModule interviewIntroModuleModuleVO) {
		
		return (interviewIntroModuleModuleVO == null) ? 
				"" : 
				(interviewIntroModuleModuleVO.getInterviewModuleName() == null)
					? " "
					: interviewIntroModuleModuleVO.getInterviewModuleName().substring(0, 4);
	}

	private String getIntroModuleName(InterviewIntroModuleModule interviewIntroModuleModuleVO) {
		
		return (interviewIntroModuleModuleVO == null) ? 
				"" : 
				(interviewIntroModuleModuleVO.getIntroModuleNodeName() == null)
					? " "
					: interviewIntroModuleModuleVO.getIntroModuleNodeName().substring(0, 4);
	}

	private Set<String> populateHeadersAndAnswers(List<InterviewQuestion> uniqueInterviewQuestions
				,ExportCSVVO exportCSVVO,ReportHistoryVO reportHistoryVO, String[] modules, Long uniqueInterviewSize) {
		
		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.2);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		ArrayList<Long> uniqueInterviews = new ArrayList<Long>();
				
		long msPerInterview = getMsPerInterview(uniqueInterviewSize.intValue(), 
					reportHistoryVO, ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue(), 0.3);				
		
		if(uniqueInterviewQuestions.size() > 0){
			//sort interview question
			uniqueInterviewQuestions = sortInterviewQuestions(uniqueInterviewQuestions);
		}
		
		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		//Initialize map to prevent multiple re-queries
		for(String module : modules){
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));			
		}		
		
		int currentCount = 0;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0; 
		List<Interview> interviewQuestionAnswer = null;
		
		if(msPerInterview != 0){
			reportHistoryVO.setDuration((uniqueInterviewSize * msPerInterview)/MS_PER_MIN);	
		}		
		
		int size = uniqueInterviewQuestions.size()*2;
		for(InterviewQuestion interviewQuestionVO : uniqueInterviewQuestions){

			currentCount++;			
			updateProgress(size, 
					reportHistoryVO, 
					currentCount, 
					elapsedTime,
					msPerInterview);			
		
			//Build header and answers
			addHeadersAndAnswers(headers, interviewQuestionVO, exportCSVVO, 
					nodeVoList);
		
			if(!uniqueInterviews.contains(interviewQuestionVO.getIdInterview())){ 
				uniqueInterviews.add(interviewQuestionVO.getIdInterview());
				reportHistoryVO.setRecordCount(uniqueInterviews.size());
			}
			
			elapsedTime = System.currentTimeMillis() - startTime;
		}

		size = uniqueInterviews.size() + uniqueInterviewQuestions.size();
		//Consolidate answers for all interviews, fill not answered and not asked questions
		for(Long interviewId : uniqueInterviews){
			
			currentCount++;			
			updateProgress(size, 
					reportHistoryVO, 
					currentCount, 
					elapsedTime,
					msPerInterview);
			
			interviewQuestionAnswer = interviewService.getInterviewQuestionAnswer(interviewId);
			
			Interview interviewVO = interviewQuestionAnswer.get(0);
				
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus())));
			
			Set<String> questionIdList = exportCSVVO.getQuestionIdList();
			
			for(String questionId:questionIdList){
				
				if(questionId.contains("_")){
					
					handleMultiQuestion(interviewVO, answers, questionId);
					
				}else{					
					handleSimpleQuestion(interviewVO, answers, questionId);
				}				
			}
			
			exportCSVVO.getAnswers().put(interviewVO, answers);
			
			elapsedTime = System.currentTimeMillis() - startTime;
		}
		
		return headers;
	}

	private void handleSimpleQuestion(Interview interviewVO, List<String> answers, String questionId) {
		InterviewQuestion questionAsked = null;
		for(InterviewQuestion mIntQuestionVO:interviewVO.getQuestionHistory()){
			if(mIntQuestionVO.getQuestionId() == Long.valueOf(questionId)){
				questionAsked = mIntQuestionVO;
				break;
			}
		}
		if(questionAsked != null){
			if(!questionAsked.getAnswers().isEmpty()){
				InterviewAnswer questionAskedAns = questionAsked.getAnswers().get(0);
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

	private void handleMultiQuestion(Interview interviewVO, List<String> answers, String questionId) {
		String[] temp = questionId.split("_");
		long tempQId = Long.valueOf(temp[0]);
		String tempNumber = String.valueOf(temp[1]);
		InterviewQuestion questionAsked = null;
		for(InterviewQuestion mIntQuestionVO:interviewVO.getQuestionHistory()){
			if(mIntQuestionVO.getQuestionId() == tempQId){
				questionAsked = mIntQuestionVO;
				break;
			}
		}
		if(questionAsked != null){
			if(!questionAsked.getAnswers().isEmpty()){
				boolean numberExist = false;
				for(InterviewAnswer ans:questionAsked.getAnswers()){
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
			else {
				answers.add("-QNotAsked-");
			}
		}else{							
			answers.add("-QNotAsked-");
		}
	}

	/**
	 * Estimate duration from the past completed report if available
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
			msPerInterview = (long) ((historicalReport.getDuration() * MS_PER_MIN)/historicalReport.getRecordCount());									
			reportHistoryVO.setDuration((size * msPerInterview)/MS_PER_MIN);
			updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), progress);
		}
		return msPerInterview;
	}
	
	/**
	 * This method is supposed to dynamically estimate the duration given the processed record count and elapsed time
	 * @param interviewSize
	 * @param reportHistoryVO
	 * @param currentCount
	 * @param elapsedTime
	 * @param progress
	 * @param msPerInterview - Estimate from latest completed record
	 */
	private void updateProgress(int interviewSize, ReportHistoryVO reportHistoryVO,
			int currentCount, long elapsedTime, long msPerInterview) {
		
		//Check processed count and estimate duration
		if(msPerInterview == 0){
			msPerInterview = (long) (elapsedTime/currentCount);
			reportHistoryVO.setDuration((interviewSize * msPerInterview)/MS_PER_MIN);			
		}
		
		updateProgress(reportHistoryVO, 
				reportHistoryVO.getStatus(), 
				(double) currentCount/interviewSize * 100, 
				reportHistoryVO.getStartDt(), 
				reportHistoryVO.getDuration());
	}

	private List<InterviewQuestion> sortInterviewQuestions(List<InterviewQuestion> uniqueInterviewQuestions) {
		Map<String,List<InterviewQuestion>> map = new LinkedHashMap<>();
		// Consolidate the questions by its top node id
		for(InterviewQuestion iqv:uniqueInterviewQuestions){
			if(!map.containsKey(String.valueOf(iqv.getTopNodeId()))){
				map.put(String.valueOf(iqv.getTopNodeId()), 
						new LinkedList<InterviewQuestion>());
			}
			map.get(String.valueOf(iqv.getTopNodeId())).add(iqv);
		}
		// Sort and merge by number
		List<InterviewQuestion> resultList = new LinkedList<>();
		for (Map.Entry<String,List<InterviewQuestion>> entry : map.entrySet()) {
			List<InterviewQuestion> value = entry.getValue();
			InterviewQuestion firstElement = value.remove(0);
			Collections.sort(value, new QuestionComparator());
			value.add(0, firstElement);			
			resultList.addAll(value);
		}
		
		return resultList;
	}

	private void addHeadersAndAnswers(Set<String> headers, 
			InterviewQuestion interviewQuestionVO, ExportCSVVO exportCSVVO, Map<Long, NodeVO> nodeVoList) {
		
		long topNodeId = Long.valueOf(interviewQuestionVO.getTopNodeId());
		NodeVO topModule = nodeVoList.get(topNodeId);
		if(topModule == null){
			topModule = getTopModuleByTopNodeId(topNodeId);
			nodeVoList.put(topNodeId, topModule);
		}
		
		if(isModuleOrAjsm(interviewQuestionVO)){
			//Do nothing?
			//headers.add(interviewQuestionVO.getName().substring(0, 4));
		}
		else if("Q_multiple".equals(interviewQuestionVO.getType())){
				Question qVO = findInterviewQuestionInMultipleQuestionsNode
						(interviewQuestionVO.getQuestionId());
				if(qVO == null){
					System.out.println("Something wrong with our data");
					return;
				}
				
				for(PossibleAnswer pVO:qVO.getChildNodes()){
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

	private Question findInterviewQuestionInMultipleQuestionsNode(
			long questionId) {
		return questionService.findMultipleQuestion(questionId);
	}

	private boolean isModuleOrAjsm(InterviewQuestion interviewQuestionVO) {
		return "M".equals(interviewQuestionVO.getNodeClass())
				|| "M_IntroModule".equals(interviewQuestionVO.getType())
				|| "Q_linkedajsm".equals(interviewQuestionVO.getType())
				|| "Q_linkedmodule".equals(interviewQuestionVO.getType())
				|| "F_ajsm".equals(interviewQuestionVO.getType());
	}
	
	public String getStatusDescription(int status) {
		
		if (status == 1) {
			return "Partial";
		} else if (status == 2) {
			return "Completed";
		}
		return "Running";
	}
}
