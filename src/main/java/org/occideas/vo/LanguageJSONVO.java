package org.occideas.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageJSONVO
{

    private String idNode;
    private String languageId;
    List<NodeLanguageVO> vo;


    public String getIdNode()
    {
        return idNode;
    }


    public void setIdNode(String idNode)
    {
        this.idNode = idNode;
    }


    public List<NodeLanguageVO> getVo()
    {
        return vo;
    }


    public void setVo(List<NodeLanguageVO> vo)
    {
        this.vo = vo;
    }


    public String getLanguageId()
    {
        return languageId;
    }


    public void setLanguageId(String languageId)
    {
        this.languageId = languageId;
    }

}
