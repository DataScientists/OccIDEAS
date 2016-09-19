package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.ReportHistory;
import org.occideas.vo.ReportHistoryVO;

@Mapper(componentModel = "spring")
public interface ReportHistoryMapper {

	ReportHistory convertToReportHistory(ReportHistoryVO vo);
	
	List<ReportHistory> convertToReportHistoryList(List<ReportHistoryVO> vo);
	
	ReportHistoryVO convertToReportHistoryVO(ReportHistory entity);
	
	List<ReportHistoryVO> convertToReportHistoryVOList(List<ReportHistory> vo);
	
}
