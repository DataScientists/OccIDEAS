package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.Node;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FragmentMapperImpl implements FragmentMapper {

    @Autowired
    private NodeMapper nodeMapper;

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
        List<Node> childNodes = moduleEntity.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        fragmentVO.setChildNodes( nodeMapper.convertToNodeVOList( childNodes ) );
        }
        fragmentVO.setOriginalId( moduleEntity.getOriginalId() );
        fragmentVO.setDeleted( moduleEntity.getDeleted() );
        fragmentVO.setNodeclass( moduleEntity.getNodeclass() );

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
    public Fragment convertToFragment(FragmentVO moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        Fragment module = new Fragment();

        module.setIdNode( moduleVO.getIdNode() );
        module.setName( moduleVO.getName() );
        module.setDescription( moduleVO.getDescription() );
        module.setType( moduleVO.getType() );
        module.setSequence( moduleVO.getSequence() );
        module.setParentId( moduleVO.getParentId());
        module.setLastUpdated( moduleVO.getLastUpdated() );
        List<NodeVO> childNodes = moduleVO.getChildNodes();
        if(!CommonUtil.isListEmpty(childNodes)){
        	module.setChildNodes( nodeMapper.convertToNodeList(childNodes) );
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
    public List<Fragment> convertToFragmentList(List<FragmentVO> moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        List<Fragment> list = new ArrayList<Fragment>();
        for ( FragmentVO moduleVO_ : moduleVO ) {
            list.add( convertToFragment( moduleVO_ ) );
        }

        return list;
    }
    
}

