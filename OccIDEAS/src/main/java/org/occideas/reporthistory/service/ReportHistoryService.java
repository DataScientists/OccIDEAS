package org.occideas.reporthistory.service;

import java.util.List;

import org.occideas.vo.ReportHistoryVO;

public interface ReportHistoryService {

	List<ReportHistoryVO> getAll();
	List<ReportHistoryVO> getByType(String type);
	ReportHistoryVO getLatestByType(String type);
	ReportHistoryVO save(ReportHistoryVO entity);
	void delete(ReportHistoryVO entity);
	
}
