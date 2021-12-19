package org.occideas.interview.service.result.view;

public class NoiseView {

    private String type;
    private String nodeNumber;
    private String dB;
    private String backgroundhours;
    private String partialExposure;
    private long idNode;
    private String nodeText;
    private String moduleName;
    private long topNodeId;

    public NoiseView(String type, String nodeNumber, String dB, String backgroundhours, String partialExposure, long idNode, String nodeText, String moduleName, long topNodeId) {
        this.type = type;
        this.nodeNumber = nodeNumber;
        this.dB = dB;
        this.backgroundhours = backgroundhours;
        this.partialExposure = partialExposure;
        this.idNode = idNode;
        this.nodeText = nodeText;
        this.moduleName = moduleName;
        this.topNodeId = topNodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(String nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public String getdB() {
        return dB;
    }

    public void setdB(String dB) {
        this.dB = dB;
    }

    public String getBackgroundhours() {
        return backgroundhours;
    }

    public void setBackgroundhours(String backgroundhours) {
        this.backgroundhours = backgroundhours;
    }

    public String getPartialExposure() {
        return partialExposure;
    }

    public void setPartialExposure(String partialExposure) {
        this.partialExposure = partialExposure;
    }

    public long getIdNode() {
        return idNode;
    }

    public void setIdNode(long idNode) {
        this.idNode = idNode;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public long getTopNodeId() {
        return topNodeId;
    }

    public void setTopNodeId(long topNodeId) {
        this.topNodeId = topNodeId;
    }
}
