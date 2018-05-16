package org.occideas.vo;

public class HeaderVO
{

    private String headerName;
    private String value;


    public HeaderVO(String headerName, String value)
    {
        super();
        this.headerName = headerName;
        this.value = value;
    }


    public String getHeaderName()
    {
        return headerName;
    }


    public void setHeaderName(String headerName)
    {
        this.headerName = headerName;
    }


    public String getValue()
    {
        return value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }

}
