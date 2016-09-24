package org.occideas.reporthistory.service;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.occideas.mapper.ReportHistoryMapper;
import org.occideas.reporthistory.dao.ReportHistoryDao;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReportHistoryServiceImpl implements ReportHistoryService{

	@Autowired
	private ReportHistoryDao dao;
	@Autowired
	private ReportHistoryMapper mapper;
	
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

}
