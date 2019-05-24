package org.occideas.vo;

public class LanguageModBreakdownVO
{

    private long idNode;
    private String name;
    private Integer current;
    private Integer total;


    public long getIdNode()
    {
        return idNode;
    }


    public void setIdNode(long idNode)
    {
        this.idNode = idNode;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Integer getCurrent()
    {
        return current;
    }


    public void setCurrent(Integer current)
    {
        this.current = current;
    }


    public Integer getTotal()
    {
        return total;
    }


    public void setTotal(Integer total)
    {
        this.total = total;
    }

}
