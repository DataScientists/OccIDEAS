package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"conditions"})
public class ModuleRuleVO {

  private long idModule;
  private String moduleName;
  private long idRule;
  private String ruleLevel;
  private RuleVO rule;
  private long idAgent;
  private String agentName;
  private long idNode;
  private String nodeNumber;

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getNodeNumber() {
    return nodeNumber;
  }

  public void setNodeNumber(String nodeNumber) {
    this.nodeNumber = nodeNumber;
  }

  public long getIdModule() {
    return idModule;
  }

  public void setIdModule(long idModule) {
    this.idModule = idModule;
  }

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }

  public long getIdAgent() {
    return idAgent;
  }

  public void setIdAgent(long idAgent) {
    this.idAgent = idAgent;
  }

  public long getIdNode() {
    return idNode;
  }

  public void setIdNode(long idNode) {
    this.idNode = idNode;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getRuleLevel() {
    if (ruleLevel != null) {
      //if(this.getRule()!=null){
      //	ruleLevel = this.getRule().getLevel();
      if (ruleLevel.equalsIgnoreCase("0")) {
        ruleLevel = "probHigh";
      } else if (ruleLevel.equalsIgnoreCase("1")) {
        ruleLevel = "probMedium";
      } else if (ruleLevel.equalsIgnoreCase("2")) {
        ruleLevel = "probLow";
      } else if (ruleLevel.equalsIgnoreCase("3")) {
        ruleLevel = "probUnknown";
      } else if (ruleLevel.equalsIgnoreCase("4")) {
        ruleLevel = "possUnknown";
      } else if (ruleLevel.equalsIgnoreCase("5")) {
        ruleLevel = "noExposure";
      }
      //}
    }
    return ruleLevel;
  }

  public void setRuleLevel(String ruleLevel) {
    this.ruleLevel = ruleLevel;
  }

  public RuleVO getRule() {
    return rule;
  }

  public void setRule(RuleVO rule) {
    this.rule = rule;
  }


}
