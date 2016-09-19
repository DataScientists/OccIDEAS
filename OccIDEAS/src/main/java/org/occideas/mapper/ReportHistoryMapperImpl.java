package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ReportHistory;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.stereotype.Component;

@Component
public class ReportHistoryMapperImpl implements ReportHistoryMapper{

	@Override
	public ReportHistory convertToReportHistory(ReportHistoryVO vo) {
		if(vo == null){
			return null;
		}
		ReportHistory entity = new ReportHistory();
		entity.setId(vo.getId());
		entity.setName(vo.getName());
		entity.setPath(vo.getPath());
		entity.setRequestor(vo.getRequestor());
		entity.setStatus(vo.getStatus());
		entity.setType(vo.getType());
		if(vo.getJsonData() != null){
			entity.setJsonData(vo.getJsonData());
		}
		entity.setUpdatedDt(vo.getUpdatedDt());
		entity.setUpdatedBy(vo.getUpdatedBy());
		return entity;
	}

	@Override
	public List<ReportHistory> convertToReportHistoryList(List<ReportHistoryVO> voList) {
		if(voList == null){
			return null;
		}
		List<ReportHistory> list = new ArrayList<ReportHistory>();
		for(ReportHistoryVO vo:voList){
			list.add(convertToReportHistory(vo));
		}
		return list;
	}

	@Override
	public ReportHistoryVO convertToReportHistoryVO(ReportHistory entity) {
		if(entity == null){
			return null;
		}
		ReportHistoryVO vo = new ReportHistoryVO();
		vo.setId(entity.getId());
		vo.setName(entity.getName());
		vo.setPath(entity.getPath());
		vo.setRequestor(entity.getRequestor());
		vo.setStatus(entity.getStatus());
		vo.setType(entity.getType());
		vo.setUpdatedBy(entity.getUpdatedBy());
		vo.setUpdatedDt(entity.getUpdatedDt());
		vo.setJsonData(entity.getJsonData());
		return vo;
	}

	@Override
	public List<ReportHistoryVO> convertToReportHistoryVOList(List<ReportHistory> entityList) {
		if(entityList == null){
			return null;
		}
		List<ReportHistoryVO> list = new ArrayList<>();
		for(ReportHistory entity:entityList){
			list.add(convertToReportHistoryVO(entity));
		}
		return list;
	}

}
