package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.Module;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.Question;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapperImpl implements ModuleMapper {

    @Autowired
    private QuestionMapper nodeMapper;
    
    @Autowired
    private ModuleRuleMapper ruleMapper;

    @Override
    public ModuleVO convertToModuleVO(Module moduleEntity,boolean includeChild) {
        if ( moduleEntity == null ) {
            return null;
        }

        ModuleVO moduleVO = new ModuleVO();

        moduleVO.setIdNode( moduleEntity.getIdNode() );
        moduleVO.setName( moduleEntity.getName() );
        moduleVO.setDescription( moduleEntity.getDescription() );
        moduleVO.setType( moduleEntity.getType() );
        moduleVO.setSequence( moduleEntity.getSequence() );
        moduleVO.setNumber( moduleEntity.getNumber() );
        moduleVO.setParentId( moduleEntity.getParentId());
        moduleVO.setLink( moduleEntity.getLink() );
        moduleVO.setTopNodeId( moduleEntity.getTopNodeId() );
        moduleVO.setLastUpdated( moduleEntity.getLastUpdated() );
        List<Question> childNodes = moduleEntity.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        moduleVO.setChildNodes( nodeMapper.convertToQuestionVOList( childNodes ) );
        }
        moduleVO.setOriginalId( moduleEntity.getOriginalId() );
        moduleVO.setDeleted( moduleEntity.getDeleted() );
        moduleVO.setNodeclass( moduleEntity.getNodeclass() );
        List<ModuleRule> moduleRule = moduleEntity.getModuleRule();
        if(!CommonUtil.isListEmpty(moduleRule)){
        	moduleVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
        }
        return moduleVO;
    }
    @Override
    public ModuleVO convertToModuleWithFlagsVO(Module moduleEntity,boolean includeChildNodes, boolean includeRules) {
        if ( moduleEntity == null ) {
            return null;
        }

        ModuleVO moduleVO = new ModuleVO();

        moduleVO.setIdNode( moduleEntity.getIdNode() );
        moduleVO.setName( moduleEntity.getName() );
        moduleVO.setDescription( moduleEntity.getDescription() );
        moduleVO.setType( moduleEntity.getType() );
        moduleVO.setSequence( moduleEntity.getSequence() );
        moduleVO.setNumber( moduleEntity.getNumber() );
        moduleVO.setParentId( moduleEntity.getParentId());
        moduleVO.setLink( moduleEntity.getLink() );
        moduleVO.setTopNodeId( moduleEntity.getTopNodeId() );
        moduleVO.setLastUpdated( moduleEntity.getLastUpdated() );
        List<Question> childNodes = moduleEntity.getChildNodes();
        if(includeChildNodes && !CommonUtil.isListEmpty(childNodes)){
        moduleVO.setChildNodes( nodeMapper.convertToQuestionWithFlagsVOList( childNodes, includeChildNodes,includeRules ) );
        }
        moduleVO.setOriginalId( moduleEntity.getOriginalId() );
        moduleVO.setDeleted( moduleEntity.getDeleted() );
        moduleVO.setNodeclass( moduleEntity.getNodeclass() );
        List<ModuleRule> moduleRule = moduleEntity.getModuleRule();
        if(includeRules && !CommonUtil.isListEmpty(moduleRule)){
        	moduleVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
        }
        return moduleVO;
    }
    @Override
    public ModuleVO convertToInterviewModuleVO(Module moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        ModuleVO moduleVO = new ModuleVO();

        moduleVO.setIdNode( moduleEntity.getIdNode() );
        moduleVO.setName( moduleEntity.getName() );
        moduleVO.setDescription( moduleEntity.getDescription() );
        moduleVO.setType( moduleEntity.getType() );
        moduleVO.setSequence( moduleEntity.getSequence() );
        moduleVO.setNumber( moduleEntity.getNumber() );
        moduleVO.setParentId( moduleEntity.getParentId());
        moduleVO.setLink( moduleEntity.getLink() );
        moduleVO.setTopNodeId( moduleEntity.getTopNodeId() );
        moduleVO.setLastUpdated( moduleEntity.getLastUpdated() );
        moduleVO.setOriginalId( moduleEntity.getOriginalId() );
        moduleVO.setDeleted( moduleEntity.getDeleted() );
        moduleVO.setNodeclass( moduleEntity.getNodeclass() );
        List<Question> childNodes = moduleEntity.getChildNodes();
        moduleVO.setChildNodes( nodeMapper.convertToInterviewQuestionVOList( childNodes ) );
        return moduleVO;
    }

    @Override
    public List<ModuleVO> convertToModuleVOList(List<Module> moduleEntity,boolean includeChild) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<ModuleVO> list = new ArrayList<ModuleVO>();
        for ( Module module : moduleEntity ) {
            list.add( convertToModuleVO( module ,includeChild) );
        }

        return list;
    }

    @Override
    public Module convertToModule(ModuleVO moduleVO,boolean includeChild) {
        if ( moduleVO == null ) {
            return null;
        }

        Module module = new Module();

        module.setIdNode( moduleVO.getIdNode() );
        module.setName( moduleVO.getName() );
        module.setDescription( moduleVO.getDescription() );
        module.setType( moduleVO.getType() );
        module.setSequence( moduleVO.getSequence() );
        module.setParentId( moduleVO.getParentId() );
        module.setLastUpdated( moduleVO.getLastUpdated() );
        List<QuestionVO> childNodes = moduleVO.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        	module.setChildNodes( nodeMapper.convertToQuestionList(childNodes) );
        }
        module.setNumber( moduleVO.getNumber() );
        module.setLink( moduleVO.getLink() );
        module.setTopNodeId( moduleVO.getTopNodeId() );
        module.setOriginalId( moduleVO.getOriginalId() );
        module.setDeleted( moduleVO.getDeleted() );
        module.setNodeclass( moduleVO.getNodeclass() );

        return module;
    }

    @Override
    public List<Module> convertToModuleList(List<ModuleVO> moduleVO,boolean includeChild) {
        if ( moduleVO == null ) {
            return null;
        }

        List<Module> list = new ArrayList<Module>();
        for ( ModuleVO moduleVO_ : moduleVO ) {
            list.add( convertToModule( moduleVO_,includeChild ) );
        }

        return list;
    }
	@Override
	public List<InterviewIntroModuleModuleVO>  convertToInterviewModuleListVO(List<InterviewIntroModuleModule> moduleList) {
		
		if ( moduleList == null ) {
            return null;
        }

        List<InterviewIntroModuleModuleVO> list = new ArrayList<InterviewIntroModuleModuleVO>();
        for ( InterviewIntroModuleModule module : moduleList ) {
            list.add( convertToModuleVO( module) );
        }

        return list;
	}
	private InterviewIntroModuleModuleVO convertToModuleVO(InterviewIntroModuleModule module) {
		
		if ( module == null ) {
            return null;
        }

		InterviewIntroModuleModuleVO moduleVO = new InterviewIntroModuleModuleVO();
		moduleVO.setInterviewModuleName(module.getInterviewModuleName());
		moduleVO.setIntroModuleNodeName(module.getIntroModuleNodeName());
		moduleVO.setInterviewId(module.getInterviewId());
		
        return moduleVO;
	}
    
}

