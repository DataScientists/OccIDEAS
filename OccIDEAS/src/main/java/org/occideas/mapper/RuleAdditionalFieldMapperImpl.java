package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.RuleAdditionalField;
import org.occideas.vo.RuleAdditionalFieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleAdditionalFieldMapperImpl implements RuleAdditionalFieldMapper {

	@Autowired
	private AdditionalFieldMapper additionalFieldMapper;
	
    @Override
    public RuleAdditionalFieldVO convertToRuleAdditionalFieldVO(RuleAdditionalField moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        RuleAdditionalFieldVO moduleVO = new RuleAdditionalFieldVO();

        moduleVO.setAdditionalfield( additionalFieldMapper.convertToAdditionalFieldVO(moduleEntity.getAdditionalfield() ));
        moduleVO.setValue( moduleEntity.getValue() );       
        
        return moduleVO;
    }

    @Override
    public List<RuleAdditionalFieldVO> convertToRuleAdditionalFieldVOList(List<RuleAdditionalField> moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<RuleAdditionalFieldVO> list = new ArrayList<RuleAdditionalFieldVO>();
        for ( RuleAdditionalField module : moduleEntity ) {
            list.add( convertToRuleAdditionalFieldVO( module ) );
        }

        return list;
    }

    @Override
    public RuleAdditionalField convertToRuleAdditionalField(RuleAdditionalFieldVO moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }
        RuleAdditionalField module = new RuleAdditionalField();

        module.setAdditionalfield( additionalFieldMapper.convertToAdditionalField(moduleVO.getAdditionalfield() ));
        module.setValue( moduleVO.getValue() );

        return module;
    }

    @Override
    public List<RuleAdditionalField> convertToRuleAdditionalFieldList(List<RuleAdditionalFieldVO> moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        List<RuleAdditionalField> list = new ArrayList<RuleAdditionalField>();
        for ( RuleAdditionalFieldVO moduleVO_ : moduleVO ) {
            list.add( convertToRuleAdditionalField( moduleVO_ ) );
        }

        return list;
    }
    
}

