package org.occideas.anzscocoder.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsCoderCodeResponse {

    private String codeStatus;
    private List<AbsCoderResultItem> result;

    public String getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
    }

    public List<AbsCoderResultItem> getResult() {
        return result;
    }

    public void setResult(List<AbsCoderResultItem> result) {
        this.result = result;
    }
}
