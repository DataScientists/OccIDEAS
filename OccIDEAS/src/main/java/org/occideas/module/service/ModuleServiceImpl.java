package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Module;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleDao dao;
	
	@Autowired
	private ModuleMapper mapper;
	
	@Override
	public List<ModuleVO> listAll() {
		return mapper.convertToModuleVOList(dao.getAllActive(),false);
	}

	@Override
	public List<ModuleVO> findById(Long id) {
		Module module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module,true);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}
	@Override
	public List<ModuleVO> findByIdForInterview(Long id) {
		Module module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module,false);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}
	
	@Override
	public ModuleVO create(ModuleVO module) {
		Module moduleEntity = dao.save(mapper.convertToModule(module,false));
		return mapper.convertToModuleVO(moduleEntity,false);
	}

	@Override
	public void update(ModuleVO module) {
		generateIdIfNotExist(module);
		dao.saveOrUpdate(mapper.convertToModule(module,true));
	}
	
	@Override
	public void delete(ModuleVO module) {
		dao.delete(mapper.convertToModule(module,false));
	}

	@Override
	public void merge(ModuleVO module) {
		dao.merge(mapper.convertToModule(module,true));
	}

	@Override
	public Long getMaxId() {
		return dao.generateIdNode();
	}
	
	private void generateIdIfNotExist(ModuleVO module) {
		if(StringUtils.isEmpty(module.getIdNode())){
			module.setIdNode(dao.generateIdNode());
		}
	}

	@Override
	public Long copyModule(ModuleCopyVO vo) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode()+1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		populateQuestionsWithIdNode(idNode,copyVO.getChildNodes());
		dao.saveCopy(mapper.convertToModule(copyVO,true));
		return idNode;
	}

	private long lastIdNode = 0L;
	private void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes) {
		lastIdNode = idNode;
		for(QuestionVO vo:childNodes){
			vo.setParentId(String.valueOf(idNode));
			Long qsIdNode = lastIdNode+1;
			lastIdNode = qsIdNode;
			vo.setIdNode(qsIdNode);
			if(!vo.getChildNodes().isEmpty()){
				populateAnswerWithIdNode(qsIdNode,vo.getChildNodes());
			}
			if(qsIdNode > lastIdNode){
				lastIdNode =qsIdNode; 
			}
		}
	}

	private void populateAnswerWithIdNode(Long qsIdNode, List<PossibleAnswerVO> childNodes) {
		int count = 1;
		for(PossibleAnswerVO vo:childNodes){
			vo.setParentId(String.valueOf(qsIdNode));
			Long asIdNode = lastIdNode+count;
			vo.setIdNode(asIdNode);
	 		lastIdNode = asIdNode;
	 		if(!vo.getChildNodes().isEmpty()){
	 			populateQuestionsWithIdNode(asIdNode,vo.getChildNodes());
	 		}
			count++;
		}
		
	}

}
