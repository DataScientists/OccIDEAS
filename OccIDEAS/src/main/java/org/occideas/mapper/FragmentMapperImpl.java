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

        FragmentVO moduleVO = new FragmentVO();

        moduleVO.setIdNode( moduleEntity.getIdNode() );
        moduleVO.setName( moduleEntity.getName() );
        moduleVO.setDescription( moduleEntity.getDescription() );
        moduleVO.setType( moduleEntity.getType() );
        moduleVO.setSequence( moduleEntity.getSequence() );
        moduleVO.setNumber( moduleEntity.getNumber() );
//        moduleVO.setParent( nodeMapper.convertToNodeVO( moduleEntity.getParent() ) );
        moduleVO.setLink( moduleEntity.getLink() );
        moduleVO.setTopNodeId( moduleEntity.getTopNodeId() );
        moduleVO.setLastUpdated( moduleEntity.getLastUpdated() );
        List<Node> childNodes = moduleEntity.getChildNodes();
        if(includeChild && !CommonUtil.isListEmpty(childNodes)){
        moduleVO.setChildNodes( nodeMapper.convertToNodeVOList( childNodes ) );
        }
        moduleVO.setOriginalId( moduleEntity.getOriginalId() );
        moduleVO.setDeleted( moduleEntity.getDeleted() );
        moduleVO.setNodeclass( moduleEntity.getNodeclass() );

        return moduleVO;
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
        module.setParent( nodeMapper.convertToModule( moduleVO.getParent() ) );
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

