package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Translate;
import org.occideas.vo.TranslateVO;

public class TranslateMapperImpl implements TranslateMapper
{

    @Override
    public Translate convertToTranslate(TranslateVO vo) {
        if(vo == null){
            return null;
        }
        Translate entity = new Translate();
        entity.setDescription(vo.getDescription());
        entity.setId(vo.getId());
        entity.setLanguage(vo.getLanguage());
        entity.setLastUpdated(vo.getLastUpdated());
        entity.setFlag(vo.getFlag());
        entity.setJdoc(vo.getJdoc());
        return entity;
    }

    @Override
    public List<Translate> convertToTranslateList(List<TranslateVO> voList) {
        if(voList == null){
            return null;
        }
        List<Translate> list = new ArrayList<>();
        for(TranslateVO vo:voList){
            list.add(convertToTranslate(vo));
        }
        return list;
    }

    @Override
    public TranslateVO convertToTranslateVO(Translate entity) {
        if(entity == null){
            return null;
        }
        TranslateVO vo = new TranslateVO();
        vo.setDescription(entity.getDescription());
        vo.setId(entity.getId());
        vo.setLanguage(entity.getLanguage());
        vo.setLastUpdated(entity.getLastUpdated());
        vo.setFlag(entity.getFlag());
        vo.setJdoc(entity.getJdoc());
        return vo;
    }

    @Override
    public List<TranslateVO> convertToTranslateVOList(List<Translate> entityList) {
        if(entityList == null){
            return null;
        }
        List<TranslateVO> list = new ArrayList<>();
        for(Translate entity:entityList){
            list.add(convertToTranslateVO(entity));
        }
        return list;
    }

}
