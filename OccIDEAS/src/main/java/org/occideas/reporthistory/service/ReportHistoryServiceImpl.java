package org.occideas.reporthistory.service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.mapper.InterviewRulesMapper;
import org.occideas.mapper.ReportHistoryMapper;
import org.occideas.reporthistory.dao.ReportHistoryDao;
import org.occideas.vo.InterviewRuleReportVO;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
@Transactional
public class ReportHistoryServiceImpl implements ReportHistoryService{

	@Autowired
	private ReportHistoryDao dao;
	@Autowired
	private ReportHistoryMapper mapper;
	@Autowired
	private InterviewRulesMapper interviewRuleMapper;
	
	@Override
	public List<ReportHistoryVO> getAll() {
		return mapper.convertToReportHistoryVOList(dao.getAll());
	}

	@Override
	public List<ReportHistoryVO> getByType(String type) {
		return mapper.convertToReportHistoryVOList(dao.getByType(type));
	}

	@Override
	public ReportHistoryVO save(ReportHistoryVO vo) {
		return mapper.convertToReportHistoryVO(dao.save(mapper.
				convertToReportHistory(vo)));
	}

	@Override
	public void delete(ReportHistoryVO vo) {
		File file = new File(vo.getPath());
		file.delete();
		dao.delete(mapper.convertToReportHistory(vo));
	}

	@Override
	/**
	 * Get latest record for the given report type
	 */
	public ReportHistoryVO getLatestByType(String type) {
		return mapper.convertToReportHistoryVO(dao.getLatestByType(type));
	}
	
	
	@Override
	public void generateInterviewRuleReport(String filepath) throws Exception{
		List<InterviewRuleReport> list = dao.getInterviewRuleReport();
		List<InterviewRuleReportVO> listVO = interviewRuleMapper.convertToInterviewRuleVOList(list);
		Writer writer = new FileWriter(filepath);
		StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
	    beanToCsv.write(listVO);
	    writer.flush();
	    writer.close();
	}
	
	@Override
    public void generateInterviewRuleFilterReport(String filepath,List<Long> agentIds) throws Exception{
        List<InterviewRuleReport> list = dao.getInterviewRuleReportFilter(agentIds);
        List<InterviewRuleReportVO> listVO = interviewRuleMapper.convertToInterviewRuleVOList(list);
        Writer writer = new FileWriter(filepath);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(listVO);
        writer.flush();
        writer.close();
    }
	
	
}
