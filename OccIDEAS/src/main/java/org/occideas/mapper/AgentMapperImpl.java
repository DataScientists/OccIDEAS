package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Agent;
import org.occideas.vo.AgentVO;
import org.springframework.stereotype.Component;

@Component
public class AgentMapperImpl implements AgentMapper {

    @Override
    public AgentVO convertToAgentVO(Agent moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        AgentVO moduleVO = new AgentVO();

        moduleVO.setIdAgent( moduleEntity.getIdAgent() );
        moduleVO.setName( moduleEntity.getName() );
        moduleVO.setDescription( moduleEntity.getDescription() );       
        moduleVO.setLastUpdated( moduleEntity.getLastUpdated() ); 
        moduleVO.setGroupName(moduleEntity.getGroup().getName());
        moduleVO.setDeleted( moduleEntity.getDeleted() );
        return moduleVO;
    }

    @Override
    public List<AgentVO> convertToAgentVOList(List<Agent> moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<AgentVO> list = new ArrayList<AgentVO>();
        for ( Agent module : moduleEntity ) {
            list.add( convertToAgentVO( module ) );
        }

        return list;
    }

    @Override
    public Agent convertToAgent(AgentVO moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        Agent module = new Agent();

        module.setIdAgent( moduleVO.getIdAgent() );
        module.setName( moduleVO.getName() );
        module.setDescription( moduleVO.getDescription() );
        
        //module.setGroup( parentMapper.convertToGroup( moduleVO ) );
        module.setLastUpdated( moduleVO.getLastUpdated() );            
        module.setDeleted( moduleVO.getDeleted() );


        return module;
    }

    @Override
    public List<Agent> convertToAgentList(List<AgentVO> moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        List<Agent> list = new ArrayList<Agent>();
        for ( AgentVO moduleVO_ : moduleVO ) {
            list.add( convertToAgent( moduleVO_ ) );
        }

        return list;
    }
    
}

