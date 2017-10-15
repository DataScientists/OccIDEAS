package org.occideas.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterAgentVO
{

    private String fileName;
    private List<Long> filterAgent;


    public String getFileName()
    {
        return fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public List<Long> getFilterAgent()
    {
        return filterAgent;
    }


    public void setFilterAgent(List<Long> filterAgent)
    {
        this.filterAgent = filterAgent;
    }

}
