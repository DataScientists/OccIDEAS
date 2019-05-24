package org.occideas.admin.service;

import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.agent.dao.IAgentDao;
import org.occideas.entity.*;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interviewanswer.dao.IInterviewAnswerDao;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.mapper.*;
import org.occideas.module.dao.IModuleDao;
import org.occideas.node.dao.INodeDao;
import org.occideas.nodelanguage.dao.INodeLanguageDao;
import org.occideas.noderule.dao.INodeRuleDao;
import org.occideas.participant.dao.IParticipantDao;
import org.occideas.possibleanswer.dao.IPossibleAnswerDao;
import org.occideas.question.dao.IQuestionDao;
import org.occideas.rule.dao.IRuleDao;
import org.occideas.utilities.NodeUtil;
import org.occideas.utilities.OSUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class DbConnectServiceImpl implements IDbConnectService {

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private NodeUtil util;

  @Autowired
  private IModuleDao moduleDao;

  @Autowired
  private IQuestionDao questionDao;

  @Autowired
  private IPossibleAnswerDao answerDao;

  @Autowired
  private IInterviewDao interviewDao;

  @Autowired
  private IInterviewQuestionDao interviewQuestionDao;

  @Autowired
  private IInterviewAnswerDao interviewAnswerDao;

  @Autowired
  private IFragmentDao fragmentDao;

  @Autowired
  private RuleMapper ruleMapper;

  @Autowired
  private RuleAdditionalFieldMapper ruleAddFieldMapper;

  @Autowired
  private NodeRuleMapper nodeRuleMapper;

  @Autowired
  private AgentMapper agentMapper;

  @Autowired
  private ModuleMapper moduleMapper;

  @Autowired
  private QuestionMapper questionMapper;

  @Autowired
  private FragmentMapper fragmentMapper;

  @Autowired
  private PossibleAnswerMapper possibleAnswerMapper;

  @Autowired
  private IRuleDao ruleDao;

  @Autowired
  private INodeRuleDao nodeRuleDao;

  @Autowired
  private IAgentDao agentDao;

  @Autowired
  private IParticipantDao participantDao;

  @Autowired
  private INodeDao nodeDao;

  @Autowired
  private INodeLanguageDao nodeLanguageDao;

  @Autowired
  private NodeLanguageMapper nodeLanguageMapper;

  @Autowired
  private ApplicationContext context;


  @Override
  public Connection connectToDb(DBConnectVO dbConnect) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(dbConnect.getHost(), dbConnect.getUsername(),
        dbConnect.getPassword());
      return con;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<NodePlain> importLibrary(DBConnectVO dbConnect) throws SQLException {
    createDumpFile(dbConnect);
    deleteNodeRules();
    deleteAgents();
    deleteNodeLanguage();
    deleteInterview();
    deleteNodes();
    deleteRules();
    System.out.println("Cleaned up db");
    Connection connectToDb = connectToDb(dbConnect);
//		List<RuleAdditionalFieldVO> ruleAddFieldList = copyRuleAddFieldFromDB(connectToDb);
    List<NodePlain> nodesFromDB = getNodesFromDB(connectToDb);
    System.out.println("Importing Nodes");
    saveBatchNodes(nodesFromDB);
    System.out.println("Importing agents");
    saveBatchAgentsPlain(copyAgentInfoPlainFromDB(connectToDb));
    System.out.println("Importing rules");
    saveBatchRules(copyRulesFromDB(connectToDb));
    System.out.println("Importing node rules");
    saveBatchNodeRules(copyNodeRuleFromDB(connectToDb));
    System.out.println("Importing languages");
    saveBatchNodeLanguage(copyNodeLanguageFromDB(connectToDb));
    System.out.println("Import from Library Done!");
    return nodesFromDB;
  }

  private void saveBatchAgentsPlain(List<AgentPlain> copyAgentInfoPlainFromDB) {
    agentDao.saveBatchAgentsPlain(copyAgentInfoPlainFromDB);
  }

  private void saveBatchNodeRules(List<NodeRuleVO> nrules) {
    nodeRuleDao.saveBatchNodeRule(nodeRuleMapper.convertToNodeRuleList(nrules));
  }

  private void saveBatchRules(List<RulePlain> copyRulesFromDB) {
    ruleDao.saveBatchRule(copyRulesFromDB);
  }

  private void saveBatchAgents(List<AgentVO> copyAgentInfoFromDB) {
    agentDao.saveBatchAgents(agentMapper.convertToAgentList(copyAgentInfoFromDB, false));
  }

  private void deleteInterview() {
    interviewAnswerDao.deleteAll();
    interviewQuestionDao.deleteAll();
    interviewDao.deleteAll();
    participantDao.deleteAll();
  }

  private void saveBatchNodes(List<NodePlain> nodes) {
    nodeDao.saveBatchNodesPlain(nodes);
  }

  private void saveNodes(List<NodeVO> nodes) {
    for (NodeVO vo : nodes) {
      if (vo instanceof ModuleVO) {
        moduleDao.saveOrUpdateIgnoreFK(moduleMapper.convertToModule((ModuleVO) vo, false));
      }
      if (vo instanceof QuestionVO) {
        questionDao.saveOrUpdateIgnoreFK(questionMapper.convertToQuestion((QuestionVO) vo));
      }
      if (vo instanceof FragmentVO) {
        fragmentDao.saveOrUpdateIgnoreFK(fragmentMapper.convertToFragment((FragmentVO) vo, false));
      }
      if (vo instanceof PossibleAnswerVO) {
        answerDao.saveOrUpdateIgnoreFK(possibleAnswerMapper.convertToPossibleAnswer((PossibleAnswerVO) vo));
      }
    }
  }

  private void saveBatchNodeLanguage(List<NodeLanguageVO> list) {
    nodeLanguageDao.batchSave(nodeLanguageMapper.convertToNodeLanguageList(list));
  }

  private void saveNodeRules(List<NodeRuleVO> list) {
    for (NodeRuleVO nodeRuleVO : list) {
      nodeRuleDao.saveOrUpdate(nodeRuleMapper.convertToNodeRule(nodeRuleVO));
    }
  }

  private void saveRules(List<RuleVO> rules) {
    for (RuleVO vo : rules) {
      ruleDao.saveOrUpdate(ruleMapper.convertToRule(vo));
    }
  }

  private void saveAgents(List<AgentVO> agentInfoList) {
    for (AgentVO vo : agentInfoList) {
      agentDao.saveOrUpdate(agentMapper.convertToAgent(vo, false));
    }
  }

  private void deleteNodeLanguage() {
    nodeLanguageDao.deleteAll();
  }

  private void deleteNodes() {
    nodeDao.deleteAll();
  }

  private void deleteAgents() {
    agentDao.deleteAll();
  }

  private void deleteRules() {
    ruleDao.deleteAll();
  }

  private void deleteNodeRules() {
    nodeRuleDao.deleteAll();
  }

  private void prepareRules(Map<Long, Long> idNodesChecklist, List<RuleVO> ruleVOList,
                            List<RuleAdditionalFieldVO> ruleAddFieldList, List<NodeRuleVO> nodeRuleVOList) {
    Map<Long, Long> ruleIds = new HashMap<>();
    Long initialRuleId = ruleDao.getMaxRuleId();
    for (RuleVO ruleVO : ruleVOList) {
      if (!ruleIds.containsKey(ruleVO.getIdRule())) {
        initialRuleId = initialRuleId + 1;
        Long newId = initialRuleId;
        ruleIds.put(ruleVO.getIdRule(), newId);
        ruleVO.setIdRule(newId);
        log.info(ruleVO);
      }
    }
    // @TODO mapping of rule additional field
    for (NodeRuleVO nodeRule : nodeRuleVOList) {
      if (ruleIds.containsKey(nodeRule.getIdRule()) && idNodesChecklist.containsKey(nodeRule.getIdNode())) {
        nodeRule.setIdRule(ruleIds.get(nodeRule.getIdRule()));
        nodeRule.setIdNode(idNodesChecklist.get(nodeRule.getIdNode()));
        log.info(nodeRule);
      }
    }
  }

  private void mergeLibrary(List<NodeVO> nodes) {
    for (NodeVO vo : nodes) {
      log.info(vo);
    }
  }

  private List<AgentVO> copyAgentInfoFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from AgentInfo where deleted = 0";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<AgentVO> list = new ArrayList<>();
      while (rs.next()) {
        AgentVO vo = new AgentVO();
        vo.setDiscriminator(rs.getString("agent_discriminator"));
        vo.setIdAgent(rs.getLong("idAgent"));
        vo.setAgentGroup(new AgentGroupVO(rs.getLong("agentGroup_idAgent")));
        vo.setDeleted(rs.getInt("deleted"));
        vo.setDescription(rs.getString("description"));
        vo.setName(rs.getString("name"));
        vo.setLastUpdated(rs.getDate("lastUpdated"));
        list.add(vo);
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<AgentPlain> copyAgentInfoPlainFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from AgentInfo";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<AgentPlain> list = new ArrayList<>();
      while (rs.next()) {
        AgentPlain entity = new AgentPlain();
        entity.setDiscriminator(rs.getString("agent_discriminator"));
        entity.setIdAgent(rs.getLong("idAgent"));
        entity.setAgentGroupId(rs.getLong("agentGroup_idAgent"));
        entity.setDeleted(rs.getInt("deleted"));
        entity.setDescription(rs.getString("description"));
        entity.setName(rs.getString("name"));
        entity.setLastUpdated(rs.getDate("lastUpdated"));
        list.add(entity);
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<NodeLanguageVO> copyNodeLanguageFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from Node_Language";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<NodeLanguageVO> list = new ArrayList<>();
      while (rs.next()) {
        NodeLanguageVO language = new NodeLanguageVO();
        language.setId(rs.getLong("id"));
        language.setLanguageId(rs.getLong("languageId"));
        language.setWord(rs.getString("word"));
        language.setTranslation(rs.getString("translation"));
        language.setLastUpdated(rs.getDate("lastUpdated"));
        list.add(language);
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<NodePlain> getNodesFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from Node where deleted = 0";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<NodePlain> list = new ArrayList<>();
      while (rs.next()) {
        NodePlain node = new NodePlain();
        node.setIdNode(rs.getLong("idNode"));
        node.setNodeDiscriminator(rs.getString("node_discriminator"));
        node.setDeleted(rs.getInt("deleted"));
        node.setDescription(rs.getString("description"));
        node.setLastUpdated(rs.getDate("lastUpdated"));
        node.setLink(rs.getLong("link"));
        node.setName(rs.getString("name"));
        node.setNodeclass(rs.getString("nodeclass"));
        node.setNumber(rs.getString("number"));
        node.setOriginalId(rs.getLong("originalId"));
        node.setSequence(rs.getInt("sequence"));
        node.setTopNodeId(rs.getLong("topNodeId"));
        node.setType(rs.getString("type"));
        node.setParentId(rs.getString("parent_idNode"));
        list.add(node);
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<RulePlain> copyRulesFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from Rule where deleted = 0";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<RulePlain> list = new ArrayList<>();
      while (rs.next()) {
        RulePlain rule = new RulePlain();
        rule.setIdRule(rs.getLong("idRule"));
        rule.setAgentId(rs.getLong("agentId"));
        rule.setLastUpdated(rs.getDate("lastUpdated"));
        rule.setLegacyRuleId(rs.getLong("legacyRuleId"));
        rule.setLevel(rs.getInt("level"));
        rule.setType(rs.getString("type"));
        rule.setDeleted(rs.getInt("deleted"));
        list.add(rule);
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<RuleAdditionalFieldVO> copyRuleAddFieldFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from Rule_AdditionalField";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<RuleAdditionalFieldVO> list = new ArrayList<>();
      while (rs.next()) {
        RuleAdditionalField ruleAddField = new RuleAdditionalField();
        ruleAddField.setIdRuleAdditionalField(rs.getLong("idRuleAdditionalField"));
        ruleAddField.setValue(rs.getString("value"));
        ruleAddField.setIdRule(rs.getLong("idRule"));
        ruleAddField.setAdditionalfield(new AdditionalField(rs.getLong("idAdditionalField")));
        list.add(ruleAddFieldMapper.convertToRuleAdditionalFieldVO(ruleAddField));
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  private List<NodeRuleVO> copyNodeRuleFromDB(Connection connect) throws SQLException {
    Statement stmt = null;
    String query = "select * from Node_Rule";
    try {
      stmt = connect.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      List<NodeRuleVO> list = new ArrayList<>();
      while (rs.next()) {
        NodeRule nrule = new NodeRule();
        nrule.setIdRule(rs.getLong("idRule"));
        nrule.setIdNode(rs.getLong("idNode"));
        list.add(nodeRuleMapper.convertToNodeRuleVO(nrule));
      }
      return list;
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return null;
  }

  public void createDumpFile(DBConnectVO dbConnect) {

    /***********************************************************/
    if (context.getBean("dataSource") instanceof BoneCPDataSource) {
      BoneCPDataSource dataSource = (BoneCPDataSource) context.getBean("dataSource");
      createDumpBoneCpDatasource(dataSource);
    } else {
      BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
      createDumpBasicDataSource(dataSource);
    }


  }

  private void createDumpBoneCpDatasource(BoneCPDataSource dataSource) {
    String jdbcUrl = dataSource.getJdbcUrl();
    int lastIndex = jdbcUrl.contains("?") ? jdbcUrl.indexOf("?") : jdbcUrl.length();
    String databaseName = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, lastIndex);

    String executeCmd = getExecuteCmd(databaseName, dataSource.getUsername(), dataSource.getPassword());

    Process runtimeProcess;
    try {
      runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      int processComplete = runtimeProcess.waitFor();
      if (processComplete == 0) {
        log.info("Backup taken successfully with " + executeCmd);
      } else {
        log.error("Could not take mysql backup with " + executeCmd);
      }
    } catch (IOException e) {
      log.error("Could not take mysql backup", e);
    } catch (InterruptedException e) {
      log.error("Could not take mysql backup", e);
    }
  }

  private void createDumpBasicDataSource(BasicDataSource dataSource) {
    String jdbcUrl = dataSource.getUrl();
    int lastIndex = jdbcUrl.contains("?") ? jdbcUrl.indexOf("?") : jdbcUrl.length();
    String databaseName = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, lastIndex);

    String executeCmd = getExecuteCmd(databaseName, dataSource.getUsername(), dataSource.getPassword());

    Process runtimeProcess;
    try {
      runtimeProcess = Runtime.getRuntime().exec(executeCmd);
      int processComplete = runtimeProcess.waitFor();
      if (processComplete == 0) {
        System.out.println("Backup taken successfully with " + executeCmd);

      } else {
        System.out.println("Could not take mysql backup with " + executeCmd);

      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private String getExecuteCmd(String databaseName, String username, String password) {
    String filename = "backup" + LocalDateTime.now().toString() + ".sql";
    filename = filename.replace(":", "").replace("-", "");
    String executeCmd = "";
    if (OSUtil.isWindows()) {
      String fullpath = "C:\\Users\\jed\\Documents\\dumps\\" + filename;
      createFile(fullpath);
      executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u " +
        username + " -p" + password + " " + databaseName
        + " -r " + fullpath;
    } else {
      String fullpath = "/opt/data/" + filename;
      createFile(fullpath);
      executeCmd = "mysqldump -u " + username
        + " -p" + password + " " + databaseName
        + " -r " + fullpath;
    }
    return executeCmd;
  }

  private void createFile(String fullpath) {
    File file = new File(fullpath);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private Map<Long, Long> prepareIdsForNodes(List<NodeVO> list) {
    Map<Long, Long> idNodeMap = new HashMap<>();
    Long initialIdNode = moduleDao.generateIdNode();
    for (NodeVO node : list) {
      if (!idNodeMap.containsKey(node.getIdNode())) {
        initialIdNode = initialIdNode + 1;
        Long newIdNode = initialIdNode;
        idNodeMap.put(node.getIdNode(), newIdNode);
        node.setIdNode(newIdNode);
      }
    }
    for (NodeVO node : list) {
      if (idNodeMap.containsKey(node.getParentId())) {
        node.setParentId(String.valueOf(idNodeMap.get(node.getParentId())));
      }
      if (idNodeMap.containsKey(node.getLink()) && node.getLink() > 0) {
        node.setLink(idNodeMap.get(node.getLink()));
      }
    }

    return idNodeMap;
  }

}
