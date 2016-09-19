package org.occideas.reporthistory.dao;

import java.util.List;

import org.occideas.entity.ReportHistory;

public interface IReportHistoryDao {

	List<ReportHistory> getAll();
	List<ReportHistory> getByType(String type);
	ReportHistory save(ReportHistory entity);
	void delete(ReportHistory entity);
	
}
