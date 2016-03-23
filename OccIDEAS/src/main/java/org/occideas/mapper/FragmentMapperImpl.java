package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.Question;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FragmentMapperImpl implements FragmentMapper {

    @Autowired
    private QuestionMapper nodeMapper;
    
    @Autowired
    private ModuleRuleMapper ruleMapper;

    @Override
    public FragmentVO convertToFragmentVO(Fragment moduleEntity,boolean includeChild) {
        if ( moduleEntity == null ) {
            return null;
        }

        FragmentVO fragmentVO = new FragmentVO();

        fragmentVO.setIdNode( moduleEntity.getIdNode() );
        fragmentVO.setName( moduleEntity.getName() );
        fragmentVO.setDescription( moduleEntity.getDescription() );
        fragmentVO.setType( moduleEntity.getType() );
        fragmentVO.setSequence( moduleEntity.getSequence() );
        fragmentVO.setNumber( moduleEntity.getNumber() );
        fragmentVO.setParentId( moduleEntity.getParentId());
        fragmentVO.setLink( moduleEntity.getLink() );
        fragmentVO.setTopNodeId( moduleEntity.getTopNodeId() );
        fragmentVO.setLastUpdated( moduleEntity.getLastUpdated() );
        List<Question> childNodes = moduleEntity.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        fragmentVO.setChildNodes( nodeMapper.convertToQuestionVOList( childNodes ) );
        }
        fragmentVO.setOriginalId( moduleEntity.getOriginalId() );
        fragmentVO.setDeleted( moduleEntity.getDeleted() );
        fragmentVO.setNodeclass( moduleEntity.getNodeclass() );
        List<ModuleRule> moduleRule = moduleEntity.getModuleRule();
        if(!CommonUtil.isListEmpty(moduleRule)){
        	fragmentVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
        }
        return fragmentVO;
    }
    @Override
    public FragmentVO convertToInterviewFragmentVO(Fragment moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        FragmentVO fragmentVO = new FragmentVO();

        fragmentVO.setIdNode( moduleEntity.getIdNode() );
        fragmentVO.setName( moduleEntity.getName() );
        fragmentVO.setDescription( moduleEntity.getDescription() );
        fragmentVO.setType( moduleEntity.getType() );
        fragmentVO.setSequence( moduleEntity.getSequence() );
        fragmentVO.setNumber( moduleEntity.getNumber() );
        fragmentVO.setParentId( moduleEntity.getParentId());
        fragmentVO.setLink( moduleEntity.getLink() );
        fragmentVO.setTopNodeId( moduleEntity.getTopNodeId() );
        fragmentVO.setLastUpdated( moduleEntity.getLastUpdated() );
        fragmentVO.setOriginalId( moduleEntity.getOriginalId() );
        fragmentVO.setDeleted( moduleEntity.getDeleted() );
        fragmentVO.setNodeclass( moduleEntity.getNodeclass() );
        List<Question> childNodes = moduleEntity.getChildNodes();
        fragmentVO.setChildNodes( nodeMapper.convertToInterviewQuestionVOList( childNodes ) );
        return fragmentVO;
    }
    @Override
    public List<FragmentVO> convertToFragmentVOList(List<Fragment> moduleEntity,boolean includeChild) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<FragmentVO> list = new ArrayList<FragmentVO>();
        for ( Fragment module : moduleEntity ) {
            list.add( convertToFragmentVO( module ,includeChild) );
        }

        return list;
    }
    @Override
    public List<FragmentVO> convertToInterviewFragmentVOList(List<Fragment> moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<FragmentVO> list = new ArrayList<FragmentVO>();
        for ( Fragment module : moduleEntity ) {
            list.add( convertToInterviewFragmentVO( module) );
        }

        return list;
    }
    @Override
    public Fragment convertToFragment(FragmentVO fragmentVO,boolean includeChild) {
        if ( fragmentVO == null ) {
            return null;
        }

        Fragment fragment = new Fragment();

        fragment.setIdNode( fragmentVO.getIdNode() );
        fragment.setName( fragmentVO.getName() );
        fragment.setDescription( fragmentVO.getDescription() );
        fragment.setType( fragmentVO.getType() );
        fragment.setSequence( fragmentVO.getSequence() );
        fragment.setParentId( fragmentVO.getParentId());
        fragment.setLastUpdated( fragmentVO.getLastUpdated() );
        List<QuestionVO> childNodes = fragmentVO.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        	fragment.setChildNodes( nodeMapper.convertToQuestionList( childNodes ) );
        }
        fragment.setNumber( fragmentVO.getNumber() );
        fragment.setLink( fragmentVO.getLink() );
        fragment.setTopNodeId( fragmentVO.getTopNodeId() );
        fragment.setOriginalId( fragmentVO.getOriginalId() );
        fragment.setDeleted( fragmentVO.getDeleted() );
        fragment.setNodeclass( fragmentVO.getNodeclass() );

        return fragment;
    }

    @Override
    public List<Fragment> convertToFragmentList(List<FragmentVO> fragmentVO,boolean includeChild) {
        if ( fragmentVO == null ) {
            return null;
        }

        List<Fragment> list = new ArrayList<Fragment>();
        for ( FragmentVO moduleVO_ : fragmentVO ) {
            list.add( convertToFragment( moduleVO_,includeChild ) );
        }

        return list;
    }
    
}

