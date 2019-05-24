package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.AdditionalField;
import org.occideas.vo.AdditionalFieldVO;
import org.springframework.stereotype.Component;

@Component
public class AdditionalFieldMapperImpl implements AdditionalFieldMapper {

    @Override
    public AdditionalFieldVO convertToAdditionalFieldVO(AdditionalField moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        AdditionalFieldVO moduleVO = new AdditionalFieldVO();

        moduleVO.setIdadditionalfield( moduleEntity.getIdadditionalfield() );
        moduleVO.setType( moduleEntity.getType() );
        moduleVO.setValue( moduleEntity.getValue() );       
        
        return moduleVO;
    }

    @Override
    public List<AdditionalFieldVO> convertToAdditionalFieldVOList(List<AdditionalField> moduleEntity) {
        if ( moduleEntity == null ) {
            return null;
        }

        List<AdditionalFieldVO> list = new ArrayList<AdditionalFieldVO>();
        for ( AdditionalField module : moduleEntity ) {
            list.add( convertToAdditionalFieldVO( module ) );
        }

        return list;
    }

    @Override
    public AdditionalField convertToAdditionalField(AdditionalFieldVO moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }
        AdditionalField module = new AdditionalField();

        module.setIdadditionalfield( moduleVO.getIdadditionalfield() );
        module.setType( moduleVO.getType() );
        module.setValue( moduleVO.getValue() );

        return module;
    }

    @Override
    public List<AdditionalField> convertToAdditionalFieldList(List<AdditionalFieldVO> moduleVO) {
        if ( moduleVO == null ) {
            return null;
        }

        List<AdditionalField> list = new ArrayList<AdditionalField>();
        for ( AdditionalFieldVO moduleVO_ : moduleVO ) {
            list.add( convertToAdditionalField( moduleVO_ ) );
        }

        return list;
    }
    
}

