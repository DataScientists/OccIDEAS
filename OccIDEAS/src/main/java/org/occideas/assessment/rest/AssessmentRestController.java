package org.occideas.assessment.rest;

import java.io.File;
import java.io.FileReader;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.assessment.service.AssessmentService;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.entity.Constant;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.Note;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.entity.Rule;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewfiredrules.service.InterviewFiredRulesService;
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
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.occideas.vo.ExportCSVVO;
import org.occideas.vo.FilterModuleVO;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewFiredRulesVO;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.ReportHistoryVO;
import org.occideas.vo.RuleVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@Path("/assessment")
public class AssessmentRestController {

	// Default duration in minutes
	private static final float INITIAL_DURATION_MIN = 60;

	private static final long MS_PER_MIN = 60 * 1000;

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private InterviewService interviewService;
	@Autowired
	private InterviewQuestionService interviewQuestionService;
	@Autowired
	private InterviewAnswerService interviewAnswerService;
	@Autowired
	private SystemPropertyService systemPropertyService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ReportHistoryService reportHistoryService;
	@Autowired
	private AssessmentService assessmentService;
	@Autowired
	private InterviewFiredRulesService firedRulesService;

	private DecimalFormat df = new DecimalFormat("#.0");

	@POST
	@Path(value = "/exportInterviewsCSV")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response exportInterviewsCSV(FilterModuleVO filterModuleVO) {

		if (filterModuleVO.getFilterModule() == null || filterModuleVO.getFilterModule().length < 1) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("No module was selected in the filter dialog.").build();
		}

		if (StringUtils.isEmpty(filterModuleVO.getFileName())) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity("Filename cannot be empty.").build();
		}

		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);

		// check if we have the directory TreeSet ins sys prop
		if (csvDir == null) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());

		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null, 0,
				ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());
		// filename saved on the report would be different from the one saved in
		// dir
		// this is to avoid overwritten reports
		String fullPath = csvDir.getValue() + reportHistoryVO.getId() + "_" + exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath, reportHistoryVO.getId(), 0,
				ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue());

		Long count = interviewQuestionService.getUniqueInterviewQuestionCount(filterModuleVO.getFilterModule());

		reportHistoryVO.setRecordCount(count);

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
				INITIAL_DURATION_MIN);

		long msPerInterview = getMsPerInterview(count.intValue(), reportHistoryVO,
				ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue(), 0.2);

		// log.info("[Report] before getting unique interview questions ");
		// List<InterviewQuestion> uniqueInterviewQuestions =
		// interviewQuestionService.getUniqueInterviewQuestions(filterModuleVO.getFilterModule());
		System.out.println("About to  get interviews");
		List<Interview> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
		System.out.println("found interviews " + uniqueInterviews.size());

		// log.info("[Report] after getting unique interview questions ");
		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		String[] modules = filterModuleVO.getFilterModule();
		// Initialize map to prevent multiple re-queries
		for (String module : modules) {
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
		}
		try {
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0;
			int currentCount = 0;

			System.out.println("About to  create folder");
			String tempFolder = csvDir.getValue() + reportHistoryVO.getId() + "/";
			File temp = new File(tempFolder);
			// FileUtils.deleteDirectory(temp);
			temp.mkdir();
			System.out.println("done creating folder");
			int iCount = 0;
			List<InterviewAnswerVO> allAnswers = new ArrayList<InterviewAnswerVO>();
			List<InterviewQuestionVO> allQuestions = new ArrayList<InterviewQuestionVO>();
			for (Interview interview : uniqueInterviews) {

				currentCount++;

				updateProgress(uniqueInterviews.size(), reportHistoryVO, currentCount, elapsedTime, msPerInterview);

				File file = new File(tempFolder + iCount + ".csv");
				CSVWriter writer = new CSVWriter(new FileWriter(file), ',');
				List<InterviewAnswerVO> answers = interviewAnswerService.findByInterviewId(interview.getIdinterview());
				List<InterviewQuestionVO> questions = interviewQuestionService
						.findByInterviewId(interview.getIdinterview());
				allAnswers.addAll(answers);
				allQuestions.addAll(questions);
				List<String> headerList = new ArrayList<String>();
				headerList.add("InterviewId");
				headerList.add("ReferenceNumber");
				headerList.add("Status");
				List<String> answerList = new ArrayList<String>();
				answerList.add(String.valueOf(interview.getIdinterview()));
				answerList.add(String.valueOf(interview.getReferenceNumber()));
				String pStatus = "ERROR";
				try {
					pStatus = String.valueOf(getStatusDescription(interview.getParticipant().getStatus()));
				} catch (Exception e) {
					System.out.println("no participant at interview" + interview.getIdinterview());
				}
				answerList.add(pStatus);

				for (InterviewAnswerVO ia : answers) {
					// InterviewQuestionVO iq =
					// interviewQuestionService.findById(ia.getInterviewQuestionId()).get(0);
					String headerKey = generateHeaderKey(ia, nodeVoList, questions);
					// System.out.println(interview.getReferenceNumber());
					// System.out.println(interview.getIdinterview());
					// System.out.println(headerKey);
					// System.out.println(ia.getAnswerFreetext());
					boolean ignore = false;
					if (headerList.contains(headerKey)) {
						// String dupSufix = "DUP";
						/*
						 * String last4Char =
						 * headerKey.substring(headerKey.length() - 4);
						 * if(last4Char.startsWith("DUP")){ String lastChar =
						 * last4Char.substring(last4Char.length()-1);
						 * if(NumberUtils.isNumber(lastChar)){ Integer dupCount
						 * = Integer.valueOf(lastChar); dupCount = dupCount+1;
						 * dupSufix = "DUP"+dupCount; } }else{
						 */
						// headerList.add("ERROR-"+headerKey+"-"+dupSufix);
						// }
						ignore = true;
					} else {
						headerList.add(headerKey);

					}
					if (!ignore) {
						answerList.add(ia.getAnswerFreetext());
					}
				}
				writer.writeNext(headerList.toArray(new String[headerList.size()]));
				writer.writeNext(answerList.toArray(new String[answerList.size()]));
				writer.close();
				iCount++;
	//			if (iCount > 200) {
	//				updateProgress(uniqueInterviews.size(), reportHistoryVO, uniqueInterviews.size(), elapsedTime,
	//						msPerInterview);
	//				updateProgress(reportHistoryVO, ReportsStatusEnum.COMPLETED.getValue(), 100);

	//				break;
	//			}
				System.out.println(iCount);
			}
			File folder = new File(tempFolder);

			File[] listOfFiles = folder.listFiles();
			List<String> fullHeaderList = new ArrayList<String>();
			for (File rfile : listOfFiles) {
				if (rfile.isFile()) {
					// System.out.println(rfile.getName());
					CSVReader reader = new CSVReader(new FileReader(rfile));
					String[] line = null;
					iCount = 0;
					String[] headerKeys = null;
					while ((line = reader.readNext()) != null) {
						// if(!line.isEmpty()){
						if (iCount == 0) {
							headerKeys = line;
							for (String headerKey : headerKeys) {
								if (!fullHeaderList.contains(headerKey)) {
									fullHeaderList.add(headerKey);
								}
							}
						} else if (iCount == 1) {

						} else {
							System.out.println("Error more lines than there should be");
						}
						// }
						iCount++;
					}
					reader.close();

				}
			}
			File fileOut = new File(fullPath);
			CSVWriter writer = new CSVWriter(new FileWriter(fileOut), ',');
			// Collections.sort(fullHeaderList);
			writer.writeNext(fullHeaderList.toArray(new String[fullHeaderList.size()]));
			for (File rfile : listOfFiles) {
				if (rfile.isFile()) {
					// System.out.println(rfile.getName());
					// BufferedReader br = new BufferedReader(new
					// FileReader(rfile));
					CSVReader reader = new CSVReader(new FileReader(rfile));
					iCount = 0;
					String[] headerKeys = null;
					String[] answers = null;
					String[] record = null;
					while ((record = reader.readNext()) != null) {
						// if(!line.isEmpty()){
						if (iCount == 0) {
							headerKeys = record;
						} else if (iCount == 1) {
							answers = record;
						} else {
							System.out.println("Error more lines than there should be");
						}
						// }
						iCount++;
					}
					reader.close();
					Map<String, String> answerMap = new HashMap<String, String>();
					for (int i = 0; i < headerKeys.length; i++) {
						answerMap.put(headerKeys[i], answers[i]);
					}
					List<String> fullAnswerList = new ArrayList<String>();
					for (String header : fullHeaderList) {
						String answer = "-NA-";
						if (answerMap.get(header) != null) {
							answer = answerMap.get(header);
						}
						fullAnswerList.add(answer);
					}
					writer.writeNext(fullAnswerList.toArray(new String[fullAnswerList.size()]));
				}
			}
			writer.close();
			FileUtils.deleteDirectory(temp);
			writeLookupNew(fullPath, allAnswers, nodeVoList, allQuestions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateProgress(reportHistoryVO, ReportsStatusEnum.COMPLETED.getValue(), 100);

		System.out.println("Report Done");
		// ExportCSVVO csvVO =
		// populateCSV(uniqueInterviewQuestions,reportHistoryVO,filterModuleVO.getFilterModule(),
		// count);

		// writeReport(fullPath, reportHistoryVO, csvVO);

		return Response.ok().build();
	}

	private void writeReport(String fullPath, ReportHistoryVO reportHistoryVO, ExportCSVVO csvVO) {
		CSVWriter writer;
		try {
			SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
			File csvDirFile = new File(csvDir.getValue());

			if (!csvDirFile.exists()) {
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

			if (reportHistoryVO.getType().equals(ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue())) {
				writeLookup(fullPath, csvVO, headers);
			}

			updateProgress(reportHistoryVO, ReportsStatusEnum.COMPLETED.getValue(), 100);

		} catch (IOException e) {
			e.printStackTrace();
			updateProgress(reportHistoryVO, ReportsStatusEnum.FAILED.getValue(), 0);
		}
	}

	private void writeLookup(String fullPath, ExportCSVVO csvVO, String[] headers) throws IOException {

		String[] names = Arrays.copyOf(csvVO.getQuestionList().toArray(), csvVO.getQuestionList().toArray().length,
				String[].class);

		if (names.length != (headers.length - 3)) {
			System.out.println("Something wrong with the data: header count: " + headers.length + ", answer count: "
					+ names.length);
		}

		ReportHistoryVO lookupVO = new ReportHistoryVO();
		lookupVO.setType("Lookup");
		lookupVO.setStartDt(new Date());

		// Write lookup file
		File fileLookup = new File(fullPath.substring(0, fullPath.length() - 4) + "-Lookup.csv");
		CSVWriter lookupWriter = new CSVWriter(new FileWriter(fileLookup), ',');

		String[] lookupHeader = new String[] { "Id", "Name" };
		lookupWriter.writeNext(lookupHeader);
		for (int i = 3, j = 0; i < headers.length && j < names.length; i++, j++) {

			String nameArray[] = names[j].split("\\|");
			String[] line = new String[] { headers[i], (nameArray.length == 1) ? names[j] : nameArray[0] };
			lookupWriter.writeNext(line);
		}

		lookupWriter.close();
		lookupVO.setRequestor(extractUserFromToken());
		lookupVO.setName(fileLookup.getName());
		lookupVO.setPath(fullPath);
		lookupVO.setEndDt(new Date());
		lookupVO.setDuration(lookupVO.getEndDt().getTime() - lookupVO.getStartDt().getTime());
		lookupVO.setStatus(ReportsStatusEnum.COMPLETED.getValue());
		lookupVO.setProgress("100.0%");

		reportHistoryService.save(lookupVO);
	}

	private void writeLookupNew(String fullPath, List<InterviewAnswerVO> allAnswers, Map<Long, NodeVO> nodeVoList,
			List<InterviewQuestionVO> questions) throws IOException {

		List<String> aNames = new ArrayList<String>();
		List<String> fullHeaderList = new ArrayList<String>();
		for (InterviewAnswerVO ia : allAnswers) {
			String header = generateHeaderKeyLookUp(ia, nodeVoList, questions);
			if (!fullHeaderList.contains(header)) {
				fullHeaderList.add(header);
			}

		}

		ReportHistoryVO lookupVO = new ReportHistoryVO();
		lookupVO.setType("Lookup");
		lookupVO.setStartDt(new Date());

		// Write lookup file
		File fileLookup = new File(fullPath.substring(0, fullPath.length() - 4) + "-Lookup.csv");
		CSVWriter lookupWriter = new CSVWriter(new FileWriter(fileLookup), ',');

		String[] lookupHeader = new String[] { "Id", "Name" };
		lookupWriter.writeNext(lookupHeader);
		for (String lookupkeyvalue : fullHeaderList) {
			String[] line = lookupkeyvalue.split("<>");
			lookupWriter.writeNext(line);
		}

		lookupWriter.close();
		lookupVO.setRequestor(extractUserFromToken());
		lookupVO.setName(fileLookup.getName());
		lookupVO.setPath(fullPath);
		lookupVO.setEndDt(new Date());
		lookupVO.setDuration(lookupVO.getEndDt().getTime() - lookupVO.getStartDt().getTime());
		lookupVO.setStatus(ReportsStatusEnum.COMPLETED.getValue());
		lookupVO.setProgress("100.0%");

		reportHistoryService.save(lookupVO);
	}

	@POST
	@Path(value = "/exportAssessmentsCSV")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response exportAssessmentsCSV(FilterModuleVO filterModuleVO) {

		// check if we have the directory TreeSet ins sys prop
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if (csvDir == null) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());

		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null, 0,
				ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue() + reportHistoryVO.getId() + "_" + exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath, reportHistoryVO.getId(), 0,
				ReportsEnum.REPORT_ASSESSMENT_EXPORT.getValue());

		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());

		reportHistoryVO.setRecordCount(count);

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
				INITIAL_DURATION_MIN);

		long msPerInterview = getMsPerInterview(count.intValue(), reportHistoryVO,
				ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue(), 0.2);

		List<Interview> uniqueInterviews = interviewService.listAllWithAssessments(filterModuleVO.getFilterModule());

		ExportCSVVO csvVO = populateAssessmentCSV(uniqueInterviews, reportHistoryVO, msPerInterview,
				filterModuleVO.getFilterModule());
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
	}

	@POST
	@Path(value = "/exportAssessmentsNoiseCSV")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response exportAssessmentsNoiseCSV(FilterModuleVO filterModuleVO) {

		// check if we have the directory TreeSet ins sys prop
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if (csvDir == null) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());

		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null, 0,
				ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue() + reportHistoryVO.getId() + "_" + exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath, reportHistoryVO.getId(), 0,
				ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue());

		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());

		reportHistoryVO.setRecordCount(count);

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
				INITIAL_DURATION_MIN);

		long msPerInterview = getMsPerInterview(count.intValue(), reportHistoryVO,
				ReportsEnum.REPORT_NOISE_ASSESSMENT_EXPORT.getValue(), 0.2);

		List<Interview> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());

		ExportCSVVO csvVO = populateAssessmentNoiseCSV(uniqueInterviews, reportHistoryVO, msPerInterview);
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
	}

	@POST
	@Path(value = "/exportAssessmentsVibrationCSV")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response exportAssessmentsVibrationCSV(FilterModuleVO filterModuleVO) {

		// check if we have the directory TreeSet ins sys prop
		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);
		if (csvDir == null) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}
		String exportFileCSV = createFileName(filterModuleVO.getFileName());

		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null, 0,
				ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue());
		String fullPath = csvDir.getValue() + reportHistoryVO.getId() + "_" + exportFileCSV;
		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath, reportHistoryVO.getId(), 0,
				ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue());

		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());

		reportHistoryVO.setRecordCount(count);

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
				INITIAL_DURATION_MIN);

		long msPerInterview = getMsPerInterview(count.intValue(), reportHistoryVO,
				ReportsEnum.REPORT_VIBRATION_ASSESSMENT_EXPORT.getValue(), 0.2);

		List<Interview> uniqueInterviews = interviewService.listAllWithRules(filterModuleVO.getFilterModule());
		ExportCSVVO csvVO = populateAssessmentVibrationCSV(uniqueInterviews, reportHistoryVO, msPerInterview);
		writeReport(fullPath, reportHistoryVO, csvVO);
		return Response.ok().build();
	}

	private ReportHistoryVO insertToReportHistory(String exportFileCSV, String fullPath, Long id, int progress,
			String type) {

		ReportHistoryVO reportHistoryVO = new ReportHistoryVO();
		if (id != null) {
			reportHistoryVO.setId(id);
		}
		String user = extractUserFromToken();
		reportHistoryVO.setName(exportFileCSV);
		reportHistoryVO.setPath(fullPath);
		reportHistoryVO.setRequestor(user);
		reportHistoryVO.setStatus(ReportsStatusEnum.IN_PROGRESS.getValue());
		reportHistoryVO.setType(type);
		reportHistoryVO.setUpdatedBy(user);
		reportHistoryVO.setProgress(progress + "%");
		return reportHistoryService.save(reportHistoryVO);
	}

	private ReportHistoryVO updateProgress(ReportHistoryVO reportHistoryVO, String status, double progress,
			Date startDate, float duration) {

		reportHistoryVO.setStatus(status);

		if (progress != 0) {
			reportHistoryVO.setProgress(df.format(progress) + "%");
			if (startDate != null && reportHistoryVO.getStartDt() == null) {
				reportHistoryVO.setStartDt(startDate);
			}

			reportHistoryVO.setDuration(Float.parseFloat(df.format(duration)));

		}
		return reportHistoryService.save(reportHistoryVO);
	}

	private ReportHistoryVO updateProgress(ReportHistoryVO reportHistoryVO, String status, double progress) {

		Date endDate = null;

		if (!status.equalsIgnoreCase(ReportsStatusEnum.IN_PROGRESS.getValue())) {
			endDate = new Date();

			reportHistoryVO
					.setDuration((float) (endDate.getTime() - reportHistoryVO.getStartDt().getTime()) / MS_PER_MIN);

			reportHistoryVO.setEndDt(endDate);
		}
		return updateProgress(reportHistoryVO, status, progress, reportHistoryVO.getStartDt(),
				reportHistoryVO.getDuration());
	}

	private String extractUserFromToken() {
		TokenManager tokenManager = new TokenManager();
		String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
		return tokenManager.parseUsernameFromToken(token);
	}

	private String createFileName(String filename) {

		return filename + ".csv";
	}

	private ExportCSVVO populateCSV(List<InterviewQuestion> uniqueInterviewQuestions, ReportHistoryVO reportHistoryVO,
			String[] modules, Long count) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswers(uniqueInterviewQuestions, vo, reportHistoryVO, modules, count);
		vo.setHeaders(headers);
		return vo;
	}

	private ExportCSVVO populateCSVNew(List<Interview> uniqueInterview, ReportHistoryVO reportHistoryVO,
			String[] modules, Long count) {
		ExportCSVVO vo = new ExportCSVVO();
		/*
		 * Set<String> headers =
		 * populateHeadersAndAnswersNew(uniqueInterviewQuestions,vo,
		 * reportHistoryVO,modules, count); vo.setHeaders(headers);
		 */
		return vo;
	}

	private ExportCSVVO populateAssessmentCSV(List<Interview> uniqueInterviews, ReportHistoryVO reportHistoryVO,
			long msPerInterview, String[] modules) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessment(uniqueInterviews, vo, reportHistoryVO, msPerInterview,
				modules);
		vo.setHeaders(headers);
		return vo;
	}

	private ExportCSVVO populateAssessmentNoiseCSV(List<Interview> uniqueInterviews, ReportHistoryVO reportHistoryVO,
			long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentNoise(uniqueInterviews, vo, reportHistoryVO,
				msPerInterview);
		vo.setHeaders(headers);
		return vo;
	}

	private ExportCSVVO populateAssessmentVibrationCSV(List<Interview> uniqueInterviews,
			ReportHistoryVO reportHistoryVO, long msPerInterview) {
		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateHeadersAndAnswersAssessmentVibration(uniqueInterviews, vo, reportHistoryVO,
				msPerInterview);
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

			updateProgress(uniqueInterviews.size(), reportHistoryVO, currentCount, elapsedTime, msPerInterview);

			boolean bFoundVibrationRules = false;
			List<RuleVO> vibrationRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			String pStatus = "ERROR";
			try {
				pStatus = String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus()));
			} catch (Exception e) {
				System.out.println("no participant at interview" + interviewVO.getIdinterview());
			}
			answers.add(pStatus);

			addModuleNames(interviewVO, answers);
			List<InterviewFiredRulesVO> firedRules = firedRulesService.findByInterviewId(interviewVO.getIdinterview());

			for (InterviewFiredRulesVO ir : firedRules) {
				for (RuleVO rule : ir.getRules()) {
					if (vibrationAgent.getIdAgent() == rule.getAgentId()) {
						vibrationRules.add(rule);
						bFoundVibrationRules = true;
					}
				}

			}
			String shiftHours = "-NA-";
			String dailyvibration = "-NA-";
			List<InterviewAnswerVO> interviewAnswers = interviewAnswerService
					.findByInterviewId(interviewVO.getIdinterview());
			List<InterviewQuestionVO> interviewQuestions = interviewQuestionService
					.findByInterviewId(interviewVO.getIdinterview());

			for (InterviewAnswerVO ia : interviewAnswers) {
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
					InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewAnswers, parentNode);

					if (actualAnswer != null) {
						InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewAnswers,
								interviewQuestions, actualAnswer);

						if (frequencyHoursNode != null) {
							try {
								frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
							} catch (Exception e) {
								System.err
										.println("Invalid frequency! Check interview " + interviewVO.getIdinterview());
							}
						}
					}

					try {
						level = Float.valueOf(noiseRule.getRuleAdditionalfields().get(0).getValue());
					} catch (Exception e) {
						System.err.println("Invalid noise rule! Check rule " + noiseRule.getIdRule());
					}
					Float partialExposure = (float) (level) * (float) (level) * (float) (frequencyhours);

					totalPartialExposure = ((totalPartialExposure) + (partialExposure));
					totalFrequency += frequencyhours;
				}

				Float dailyVibration = (float) Math.sqrt((float) (totalPartialExposure) / 8);
				dailyvibration = dailyVibration.toString();

			}
			answers.add(dailyvibration);

			exportCSVVO.getAnswers().put(interviewVO, answers);

			elapsedTime = System.currentTimeMillis() - startTime;
		}

		return headers;
	}

	private void addModuleNames(Interview interviewVO, List<String> answers) {

		if (interviewVO.getModuleList() != null) {
			answers.add(getIntroModuleName(interviewVO.getModuleList().get(0)));
			answers.add(getModuleName(
					(interviewVO.getModuleList().size() > 1) ? interviewVO.getModuleList().get(1) : null));
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
			System.out.println("Noise report:" + currentCount);

			updateProgress(uniqueInterviews.size(), reportHistoryVO, currentCount, elapsedTime, msPerInterview);

			boolean bFoundNoiseRules = false;
			List<RuleVO> noiseRules = new ArrayList<RuleVO>();
			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			String pStatus = "ERROR";
			try {
				pStatus = String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus()));
			} catch (Exception e) {
				System.out.println("No participant for interview " + interviewVO.getIdinterview());
			}
			answers.add(pStatus);

			addModuleNames(interviewVO, answers);
			List<InterviewFiredRulesVO> firedRules = firedRulesService.findByInterviewId(interviewVO.getIdinterview());
			for (InterviewFiredRulesVO fr : firedRules) {
				for (RuleVO r : fr.getRules()) {
					RuleVO rule = r;
					if (noiseAgent.getIdAgent() == rule.getAgentId()) {
						noiseRules.add(rule);
						bFoundNoiseRules = true;
					}
				}

			}
			String shiftHours = "-NA-";
			String totalExposure = "-NA-";
			String laeq8 = "-NA-";
			String peakNoise = "-NA-";
			String strRatio = "-NA-";

			List<InterviewAnswerVO> interviewAnswers = interviewAnswerService
					.findByInterviewId(interviewVO.getIdinterview());
			List<InterviewQuestionVO> interviewQuestions = interviewQuestionService
					.findByInterviewId(interviewVO.getIdinterview());
			for (InterviewAnswerVO ia : interviewAnswers) {
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
						InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewAnswers, parentNode);

						String answeredValue = "0";
						if (actualAnswer != null) {
							InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewAnswers,
									interviewQuestions, actualAnswer);

							if (frequencyHoursNode != null) {
								answeredValue = frequencyHoursNode.getAnswerFreetext();
							}
						}
						try {
							totalFrequency += Float.valueOf(answeredValue);
						} catch (Exception e) {
							System.err.println("Invalid not bg frequency! Check interview "
									+ interviewVO.getIdinterview() + " Rule " + noiseRule.getIdRule());
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
						try {
							String sLevel = noiseRule.getRuleAdditionalfields().get(0).getValue();
							level = Integer.valueOf(sLevel);
							Float partialExposure = (float) (4 * hoursbg * (Math.pow(10, (level - 100) / 10)));
							if (partialExposure > maxBackgroundPartialExposure) {
								maxBackgroundPartialExposure = partialExposure;
								maxBackgroundHours = hoursbg;
							}
						} catch (Exception e) {
							System.err.println("Invalid noise rule! Check rule " + noiseRule.getIdRule());
						}

					} else {

						Float hours = new Float(0);
						Float frequencyhours = new Float(0);
						PossibleAnswerVO parentNode = noiseRule.getConditions().get(0);
						InterviewAnswerVO actualAnswer = findInterviewAnswer(interviewAnswers, parentNode);
						boolean isSecondsTimeFrequency = false;
						if (actualAnswer != null) {
							InterviewAnswerVO frequencyHoursNode = findFrequencyInterviewAnswer(interviewAnswers,
									interviewQuestions, actualAnswer);

							if (frequencyHoursNode != null) {
								try {
									frequencyhours = Float.valueOf(frequencyHoursNode.getAnswerFreetext());
									if (frequencyHoursNode.getType().equalsIgnoreCase("P_frequencyseconds")) {

										frequencyhours = Float.valueOf(frequencyhours) / 3600; // convert
																								// seconds
																								// to
																								// hours
										isSecondsTimeFrequency = true;
									}
								} catch (Exception e) {
									System.err.println(
											"Invalid frequency! Check interview " + interviewVO.getIdinterview());
								}
							}
						}
						if (useRatio && !isSecondsTimeFrequency) {
							hours = frequencyhours / ratio;
						} else {
							hours = frequencyhours;
						}
						try {
							level = Integer.valueOf(noiseRule.getRuleAdditionalfields().get(0).getValue());
						} catch (Exception e) {
							System.err.println("Invalid noise rule! Check rule " + noiseRule.getIdRule());
						}
						Float partialExposure = (float) ((float) 4 * hours
								* (Math.pow(10, (float) (level - 100) / (float) 10)));
						// System.out.println(parentNode.getNumber() + " " +
						// parentNode.getIdNode() + " "
						// + parentNode.getName() + " " + level + " " + hours +
						// " " + partialExposure);
						totalPartialExposure = ((totalPartialExposure) + (partialExposure));

					}
					if (iPeakNoise < level) {
						iPeakNoise = level;
					}
				}
				totalPartialExposure = ((totalPartialExposure) + (maxBackgroundPartialExposure));
				// totalPartialExposure = totalPartialExposure.toFixed(4);
				totalFrequency += maxBackgroundHours;

				Float autoExposureLevel = (float) (10
						* (Math.log10(totalPartialExposure / (3.2 * (Math.pow(10, -9))))));
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

		if (properties != null && properties.size() > 0) {
			noiseAgent.setIdAgent(Long.valueOf(properties.get(0).getValue()));
			noiseAgent.setName(properties.get(0).getName());
		}

		return noiseAgent;
	}

	private InterviewAnswerVO findFrequencyInterviewAnswer(List<InterviewAnswerVO> interviewAnswers,
			List<InterviewQuestionVO> interviewQuestions, InterviewAnswerVO actualAnswer) {
		InterviewAnswerVO retValue = null;
		InterviewQuestionVO piq = null;
		for (InterviewQuestionVO iq : interviewQuestions) {
			if (iq.getParentAnswerId() > 0) {
				if (iq.getParentAnswerId() == actualAnswer.getAnswerId()) {
					piq = iq;
					break;
				}
			}
		}
		if (piq != null) {
			for (InterviewAnswerVO ia : interviewAnswers) {
				if (ia.getParentQuestionId() == piq.getQuestionId()) {
					retValue = ia;
				}
			}
		}
		if (retValue != null) {
			if (!retValue.getType().startsWith("P_frequency")) {
				retValue = findFrequencyInterviewAnswer(interviewAnswers, interviewQuestions, retValue);
			}
		}

		return retValue;
	}

	private InterviewAnswerVO findInterviewAnswer(List<InterviewAnswerVO> answerHistory, PossibleAnswerVO parentNode) {
		InterviewAnswerVO retValue = null;
		for (InterviewAnswerVO ia : answerHistory) {
			if (ia.getAnswerId() == parentNode.getIdNode()) {
				retValue = ia;
			}
		}
		return retValue;
	}

	private Set<String> populateHeadersAndAnswersAssessment(List<Interview> uniqueInterviews, ExportCSVVO exportCSVVO,
			ReportHistoryVO reportHistoryVO, long msPerInterview, String[] modules) {

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

			headers.add(agent.getName() + "_MANUAL");
			headers.add(agent.getName() + "_AUTO");
		}

		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		int currentCount = 0;

		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		// Initialize map to prevent multiple re-queries
		for (String module : modules) {
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
		}

		for (Interview interviewVO : uniqueInterviews) {

			currentCount++;

			updateProgress(uniqueInterviews.size(), reportHistoryVO, currentCount, elapsedTime, msPerInterview);

			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			String pStatus = "ERROR";
			try {
				pStatus = String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus()));
			} catch (Exception e) {
				System.out.println("no participant at interview" + interviewVO.getIdinterview());
			}
			answers.add(pStatus);

			addModuleNames(interviewVO, answers);

			for (AgentVO agent : agents) {
				boolean aAgentFound = false;
				for (Rule rule : interviewVO.getManualAssessedRules()) {
					if (agent.getIdAgent() == rule.getAgentId()) {
						answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
						aAgentFound = true;
						break;
					}
				}
				if (!aAgentFound) {
					answers.add("-NA-");
				}
				aAgentFound = false;
				for (Rule rule : interviewVO.getAutoAssessedRules()) {
					if (agent.getIdAgent() == rule.getAgentId()) {
						answers.add(RuleMapperImpl.getDescriptionByValue(rule.getLevel()));
						aAgentFound = true;
						break;
					}
				}
				if (!aAgentFound) {
					answers.add("-NA-");
				}
			}
			exportCSVVO.getAnswers().put(interviewVO, answers);

			elapsedTime = System.currentTimeMillis() - startTime;
		}

		return headers;
	}

	private String getModuleName(InterviewIntroModuleModule interviewIntroModuleModuleVO) {

		return (interviewIntroModuleModuleVO == null) ? ""
				: (interviewIntroModuleModuleVO.getInterviewModuleName() == null) ? " "
						: interviewIntroModuleModuleVO.getInterviewModuleName().substring(0, 4);
	}

	private String getIntroModuleName(InterviewIntroModuleModule interviewIntroModuleModuleVO) {

		return (interviewIntroModuleModuleVO == null) ? ""
				: (interviewIntroModuleModuleVO.getIntroModuleNodeName() == null) ? " "
						: interviewIntroModuleModuleVO.getIntroModuleNodeName().substring(0, 4);
	}

	private Set<String> populateHeadersAndAnswers(List<InterviewQuestion> uniqueInterviewQuestions,
			ExportCSVVO exportCSVVO, ReportHistoryVO reportHistoryVO, String[] modules, Long uniqueInterviewSize) {

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.2);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("Interview Id");
		headers.add("AWES ID");
		headers.add("Status");
		ArrayList<Long> uniqueInterviews = new ArrayList<Long>();

		long msPerInterview = getMsPerInterview(uniqueInterviewSize.intValue(), reportHistoryVO,
				ReportsEnum.REPORT_INTERVIEW_EXPORT.getValue(), 0.3);

		if (uniqueInterviewQuestions.size() > 0) {
			// sort interview question
			uniqueInterviewQuestions = sortInterviewQuestions(uniqueInterviewQuestions);
		}

		Map<Long, NodeVO> nodeVoList = new HashMap<>();
		// Initialize map to prevent multiple re-queries
		for (String module : modules) {
			nodeVoList.put(Long.valueOf(module), getTopModuleByTopNodeId(Long.valueOf(module)));
		}

		int currentCount = 0;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		List<Interview> interviewQuestionAnswer = null;

		if (msPerInterview != 0) {
			reportHistoryVO.setDuration((uniqueInterviewSize * msPerInterview) / MS_PER_MIN);
		}

		int size = uniqueInterviewQuestions.size() * 2;
		for (InterviewQuestion interviewQuestionVO : uniqueInterviewQuestions) {

			currentCount++;
			updateProgress(size, reportHistoryVO, currentCount, elapsedTime, msPerInterview);

			// Build header and answers
			addHeadersAndAnswers(headers, interviewQuestionVO, exportCSVVO, nodeVoList);

			if (!uniqueInterviews.contains(interviewQuestionVO.getIdInterview())) {
				uniqueInterviews.add(interviewQuestionVO.getIdInterview());
				reportHistoryVO.setRecordCount(uniqueInterviews.size());
			}

			elapsedTime = System.currentTimeMillis() - startTime;
		}

		size = uniqueInterviews.size() + uniqueInterviewQuestions.size();
		// Consolidate answers for all interviews, fill not answered and not
		// asked questions
		for (Long interviewId : uniqueInterviews) {

			currentCount++;
			updateProgress(size, reportHistoryVO, currentCount, elapsedTime, msPerInterview);

			interviewQuestionAnswer = interviewService.getInterviewQuestionAnswer(interviewId);

			Interview interviewVO = interviewQuestionAnswer.get(0);

			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));
			answers.add(String.valueOf(getStatusDescription(interviewVO.getParticipant().getStatus())));

			Set<String> questionIdList = exportCSVVO.getQuestionIdList();

			for (String questionId : questionIdList) {

				if (questionId.contains("_")) {

					handleMultiQuestion(interviewVO, answers, questionId);

				} else {
					handleSimpleQuestion(interviewVO, answers, questionId);
				}
			}

			exportCSVVO.getAnswers().put(interviewVO, answers);

			elapsedTime = System.currentTimeMillis() - startTime;
		}

		return headers;
	}

	private String findHeaderInList(Set<String> headers, InterviewAnswerVO interviewAnswer) {
		long interviewQuestionId = interviewAnswer.getInterviewQuestionId();

		return null;
	}

	private void handleSimpleQuestion(Interview interviewVO, List<String> answers, String questionId) {
		InterviewQuestion questionAsked = null;
		for (InterviewQuestion mIntQuestionVO : interviewVO.getQuestionHistory()) {
			if (mIntQuestionVO.getQuestionId() == Long.valueOf(questionId)) {
				questionAsked = mIntQuestionVO;
				break;
			}
		}
		if (questionAsked != null) {
			if (!questionAsked.getAnswers().isEmpty()) {
				InterviewAnswer questionAskedAns = questionAsked.getAnswers().get(0);
				if (!StringUtils.isEmpty(questionAskedAns.getAnswerFreetext())) {
					answers.add(questionAskedAns.getAnswerFreetext());
				} else {
					answers.add(questionAskedAns.getName());
				}
			} else {
				answers.add("-NoA-");
			}
		} else {
			answers.add("-QNotAsked-");
		}
	}

	private void handleMultiQuestion(Interview interviewVO, List<String> answers, String questionId) {
		String[] temp = questionId.split("_");
		long tempQId = Long.valueOf(temp[0]);
		String tempNumber = String.valueOf(temp[1]);
		InterviewQuestion questionAsked = null;
		for (InterviewQuestion mIntQuestionVO : interviewVO.getQuestionHistory()) {
			if (mIntQuestionVO.getQuestionId() == tempQId) {
				questionAsked = mIntQuestionVO;
				break;
			}
		}
		if (questionAsked != null) {
			if (!questionAsked.getAnswers().isEmpty()) {
				boolean numberExist = false;
				for (InterviewAnswer ans : questionAsked.getAnswers()) {
					if (ans.getNumber().equals(tempNumber) && !numberExist) {
						if (!StringUtils.isEmpty(ans.getAnswerFreetext())) {
							answers.add(ans.getAnswerFreetext());
							numberExist = true;
						} else {
							answers.add(ans.getName());
							numberExist = true;
						}
					}
				}
				if (!numberExist) {
					answers.add("-NoA-");
				}
			} else {
				answers.add("-QNotAsked-");
			}
		} else {
			answers.add("-QNotAsked-");
		}
	}

	/**
	 * Estimate duration from the past completed report if available
	 * 
	 * @param size
	 * @param reportHistoryVO
	 * @param msPerInterview
	 * @return
	 */
	private long getMsPerInterview(int size, ReportHistoryVO reportHistoryVO, String type, double progress) {

		long msPerInterview = 0;
		ReportHistoryVO historicalReport = reportHistoryService.getLatestByType(type);
		if (historicalReport != null) {
			msPerInterview = (long) ((historicalReport.getDuration() * MS_PER_MIN) / historicalReport.getRecordCount());
			reportHistoryVO.setDuration((size * msPerInterview) / MS_PER_MIN);
			updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), progress);
		}
		return msPerInterview;
	}

	/**
	 * This method is supposed to dynamically estimate the duration given the
	 * processed record count and elapsed time
	 * 
	 * @param interviewSize
	 * @param reportHistoryVO
	 * @param currentCount
	 * @param elapsedTime
	 * @param progress
	 * @param msPerInterview
	 *            - Estimate from latest completed record
	 */
	private void updateProgress(int interviewSize, ReportHistoryVO reportHistoryVO, int currentCount, long elapsedTime,
			long msPerInterview) {

		// Check processed count and estimate duration
		if (msPerInterview == 0) {
			msPerInterview = (long) (elapsedTime / currentCount);
			reportHistoryVO.setDuration((interviewSize * msPerInterview) / MS_PER_MIN);
		}

		updateProgress(reportHistoryVO, reportHistoryVO.getStatus(), (double) currentCount / interviewSize * 100,
				reportHistoryVO.getStartDt(), reportHistoryVO.getDuration());
	}

	private List<InterviewQuestion> sortInterviewQuestions(List<InterviewQuestion> uniqueInterviewQuestions) {
		Map<String, List<InterviewQuestion>> map = new LinkedHashMap<>();
		// Consolidate the questions by its top node id
		for (InterviewQuestion iqv : uniqueInterviewQuestions) {
			if (!map.containsKey(String.valueOf(iqv.getTopNodeId()))) {
				map.put(String.valueOf(iqv.getTopNodeId()), new LinkedList<InterviewQuestion>());
			}
			map.get(String.valueOf(iqv.getTopNodeId())).add(iqv);
		}
		// Sort and merge by number
		List<InterviewQuestion> resultList = new LinkedList<>();
		for (Map.Entry<String, List<InterviewQuestion>> entry : map.entrySet()) {
			List<InterviewQuestion> value = entry.getValue();
			InterviewQuestion firstElement = value.remove(0);
			Collections.sort(value, new QuestionComparator());
			value.add(0, firstElement);
			resultList.addAll(value);
		}

		return resultList;
	}

	private void addHeadersAndAnswers(Set<String> headers, InterviewQuestion interviewQuestionVO,
			ExportCSVVO exportCSVVO, Map<Long, NodeVO> nodeVoList) {

		long topNodeId = Long.valueOf(interviewQuestionVO.getTopNodeId());
		NodeVO topModule = nodeVoList.get(topNodeId);
		if (topModule == null) {
			topModule = getTopModuleByTopNodeId(topNodeId);
			nodeVoList.put(topNodeId, topModule);
		}

		if (isModuleOrAjsm(interviewQuestionVO)) {
			// Do nothing?
			// headers.add(interviewQuestionVO.getName().substring(0, 4));
		} else if ("Q_multiple".equals(interviewQuestionVO.getType())) {
			Question qVO = findInterviewQuestionInMultipleQuestionsNode(interviewQuestionVO.getQuestionId());
			if (qVO == null) {
				System.out.println("Something wrong with our data");
				return;
			}

			for (PossibleAnswer pVO : qVO.getChildNodes()) {
				StringBuilder header = new StringBuilder();
				header.append(topModule.getName().substring(0, 4));
				header.append("_");
				// header.append(interviewQuestionVO.getNumber());
				// header.append("_");
				header.append(pVO.getNumber());
				headers.add(header.toString());
				exportCSVVO.getQuestionList()
						.add(interviewQuestionVO.getName() + " " + pVO.getName() + "|" + header.toString());
				exportCSVVO.getQuestionIdList()
						.add(String.valueOf(interviewQuestionVO.getQuestionId() + "_" + pVO.getNumber()));
			}
		} else {

			StringBuilder header = new StringBuilder();
			header.append(topModule.getName().substring(0, 4));
			header.append("_");
			header.append(interviewQuestionVO.getNumber());
			headers.add(header.toString());
			exportCSVVO.getQuestionList().add(interviewQuestionVO.getName() + "|" + header.toString());
			exportCSVVO.getQuestionIdList().add(String.valueOf(interviewQuestionVO.getQuestionId()));
		}
	}

	private void addHeadersAndAnswersNew(Set<String> headers, Interview interviewVO, ExportCSVVO exportCSVVO,
			Map<Long, NodeVO> nodeVoList) {

		List<InterviewAnswerVO> listInterviewAnswerVO = interviewAnswerService
				.findByInterviewId(interviewVO.getIdinterview());
		for (InterviewAnswerVO interviewAnswerVO : listInterviewAnswerVO) {

		}
	}

	private String generateHeaderKey(InterviewAnswerVO interviewAnswerVO, Map<Long, NodeVO> nodeVoList,
			List<InterviewQuestionVO> questions) {
		String retValue = interviewAnswerVO.getNumber();
		InterviewQuestionVO interviewQuestionVO = null;
		for (InterviewQuestionVO question : questions) {
			if (interviewAnswerVO.getInterviewQuestionId() == question.getId()) {
				interviewQuestionVO = question;
				break;
			}
		}
		if (interviewQuestionVO == null) {
			interviewQuestionVO = interviewQuestionService.findIntQuestion(interviewAnswerVO.getIdInterview(),
					interviewAnswerVO.getParentQuestionId());
		}
		long topNodeId = Long.valueOf(interviewAnswerVO.getTopNodeId());
		NodeVO topModule = nodeVoList.get(topNodeId);
		if (topModule == null) {
			topModule = getTopModuleByTopNodeId(topNodeId);
			nodeVoList.put(topNodeId, topModule);
		}

		StringBuilder header = new StringBuilder();
		if ("Q_multiple".equals(interviewQuestionVO.getType())) {
			String headerName = topModule.getName().substring(0, 4);
			if (headerName.charAt(0) == '_') {
				headerName = headerName.replaceFirst("_", "");
			}
			header.append(headerName);
			header.append("_");
			// header.append(interviewQuestionVO.getNumber());
			// header.append("_");
			header.append(interviewAnswerVO.getNumber());
			retValue = header.toString();

		} else {
			String headerName = topModule.getName().substring(0, 4);
			if (headerName.charAt(0) == '_') {
				headerName = headerName.replaceFirst("_", "");
			}
			header.append(headerName);
			header.append("_");
			header.append(interviewQuestionVO.getNumber());
			retValue = header.toString();
		}
		return retValue;

	}

	private String generateHeaderKeyLookUp(InterviewAnswerVO interviewAnswerVO, Map<Long, NodeVO> nodeVoList,
			List<InterviewQuestionVO> questions) {
		String retValue = "";
		String key = "";
		String name = "";

		InterviewQuestionVO interviewQuestionVO = null;
		for (InterviewQuestionVO question : questions) {
			if (interviewAnswerVO.getInterviewQuestionId() == question.getId()) {
				interviewQuestionVO = question;
				break;
			}
		}
		if (interviewQuestionVO == null) {
			interviewQuestionVO = interviewQuestionService.findIntQuestion(interviewAnswerVO.getIdInterview(),
					interviewAnswerVO.getParentQuestionId());
		}
		long topNodeId = Long.valueOf(interviewAnswerVO.getTopNodeId());
		NodeVO topModule = nodeVoList.get(topNodeId);
		if (topModule == null) {
			topModule = getTopModuleByTopNodeId(topNodeId);
			nodeVoList.put(topNodeId, topModule);
		}

		StringBuilder header = new StringBuilder();
		if ("Q_multiple".equals(interviewQuestionVO.getType())) {
			String headerName = topModule.getName().substring(0, 4);
			if (headerName.charAt(0) == '_') {
				headerName = headerName.replaceFirst("_", "");
			}
			header.append(headerName);
			header.append("_");
			// header.append(interviewQuestionVO.getNumber());
			// header.append("_");
			header.append(interviewAnswerVO.getNumber());
			key = header.toString();
			name = interviewQuestionVO.getName() + " " + interviewAnswerVO.getName();

		} else {
			String headerName = topModule.getName().substring(0, 4);
			if (headerName.charAt(0) == '_') {
				headerName = headerName.replaceFirst("_", "");
			}
			header.append(headerName);
			header.append("_");
			header.append(interviewQuestionVO.getNumber());
			key = header.toString();
			name = interviewQuestionVO.getName();

		}
		retValue = key + "<>" + name;
		return retValue;

	}

	private NodeVO getTopModuleByTopNodeId(long topNodeId) {
		return questionService.getTopModuleByTopNodeId(topNodeId);
	}

	private Question findInterviewQuestionInMultipleQuestionsNode(long questionId) {
		return questionService.findMultipleQuestion(questionId);
	}

	private boolean isModuleOrAjsm(InterviewQuestion interviewQuestionVO) {
		return "M".equals(interviewQuestionVO.getNodeClass()) || "M_IntroModule".equals(interviewQuestionVO.getType())
				|| "Q_linkedajsm".equals(interviewQuestionVO.getType())
				|| "Q_linkedmodule".equals(interviewQuestionVO.getType())
				|| "F_ajsm".equals(interviewQuestionVO.getType());
	}

	private boolean isModuleOrAjsm(InterviewQuestionVO interviewQuestionVO) {
		return "M".equals(interviewQuestionVO.getNodeClass()) || "M_IntroModule".equals(interviewQuestionVO.getType())
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

	@POST
	@Path(value = "/getAnswerSummaryByName")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter) {
		PageVO<AssessmentAnswerSummary> list = null;
		try {
			filter.setStatus();
			list = assessmentService.getAnswerSummaryByName(filter);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@POST
	@Path(value = "/exportNotesCSV")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response exportNotes(FilterModuleVO filterModuleVO) {

		SystemPropertyVO csvDir = systemPropertyService.getByName(Constant.REPORT_EXPORT_CSV_DIR);

		// check if we have the directory TreeSet ins sys prop
		if (csvDir == null) {
			return Response.status(Status.BAD_REQUEST).type("text/plain")
					.entity("REPORT_EXPORT_CSV_DIR does not exist in System Property.").build();
		}

		String exportFileCSV = createFileName(filterModuleVO.getFileName() + "-Notes");

		ReportHistoryVO reportHistoryVO = insertToReportHistory(exportFileCSV, "", null, 0,
				ReportsEnum.NOTES_EXPORT.getValue());

		String fullPath = csvDir.getValue() + reportHistoryVO.getId() + "_" + exportFileCSV;

		reportHistoryVO = insertToReportHistory(exportFileCSV, fullPath, reportHistoryVO.getId(), 0,
				ReportsEnum.NOTES_EXPORT.getValue());

		Long count = interviewService.getAllWithRulesCount(filterModuleVO.getFilterModule());
		reportHistoryVO.setRecordCount(count);

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.11, new Date(),
				INITIAL_DURATION_MIN);

		List<Interview> uniqueInterviews = interviewService.listAssessmentsForNotes(filterModuleVO.getFilterModule());

		ExportCSVVO csvVO = populateNotesCSV(uniqueInterviews, reportHistoryVO, filterModuleVO.getFilterModule());

		writeReport(fullPath, reportHistoryVO, csvVO);

		return Response.ok().build();
	}

	private ExportCSVVO populateNotesCSV(List<Interview> uniqueInterviewQuestions, ReportHistoryVO reportHistoryVO,
			String[] filterModule) {

		ExportCSVVO vo = new ExportCSVVO();
		Set<String> headers = populateNotes(uniqueInterviewQuestions, vo, reportHistoryVO, filterModule);
		vo.setHeaders(headers);
		return vo;
	}

	private Set<String> populateNotes(List<Interview> interviews, ExportCSVVO exportCSVVO,
			ReportHistoryVO reportHistoryVO, String[] filterModule) {

		updateProgress(reportHistoryVO, ReportsStatusEnum.IN_PROGRESS.getValue(), 0.1);
		Set<String> headers = new LinkedHashSet<>();
		headers.add("IdInterview");
		headers.add("Reference Number");

		List<String> types = getTypes();
		for (String type : types) {
			headers.add(type);
		}

		headers.add("LastUpdated");

		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		int currentCount = 0;

		for (Interview interviewVO : interviews) {

			currentCount++;

			updateProgress(interviews.size(), reportHistoryVO, currentCount, elapsedTime, 0);

			List<String> answers = new ArrayList<>();
			answers.add(String.valueOf(interviewVO.getIdinterview()));
			answers.add(String.valueOf(interviewVO.getReferenceNumber()));

			for (Note note : interviewVO.getNotes()) {
				if (note.getText() == null) {
					answers.add("");
				} else {
					answers.add(note.getText());
				}
				if (answers.size() == 5) {
					answers.add(note.getLastUpdated().toString());
				}
			}

			exportCSVVO.getAnswers().put(interviewVO, answers);

			elapsedTime = System.currentTimeMillis() - startTime;
		}

		return headers;
	}

	private List<String> getTypes() {
		return interviewService.getNoteTypes();
	}
}
