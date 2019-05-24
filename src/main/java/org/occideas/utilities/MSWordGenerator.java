package org.occideas.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.occideas.entity.Constant;
import org.occideas.fragment.service.FragmentService;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MSWordGenerator {

  public Map<Integer, XWPFTableRow> map;

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private SystemPropertyService systemPropertyService;
  @Autowired
  private ModuleMapper mapper;
  @Autowired
  private FragmentMapper fragmentMapper;
  @Autowired
  private StudyAgentUtil studyAgentUtil;
  @Autowired
  private ModuleService moduleService;
  @Autowired
  private FragmentService fragmentService;
  @Autowired
  private IModuleDao dao;

  public File writeDocument(String idNode, boolean filterStudyAgent) throws IOException {
    if (idNode == null) {
      log.error("No idNode provided in MSWord.");
      return null;
    }
    // Blank Document
    map = new HashMap<>();
    XWPFDocument document = new XWPFDocument();
    String path = System.getProperty("user.home");
    SystemPropertyVO reportDir = systemPropertyService.getByName(Constant.REPORT_DOC_DIR);
    if (reportDir == null) {
      log.info("Admin config " + Constant.REPORT_DOC_DIR + " does not exist going for default path user home.");
    } else {
      path = reportDir.getValue();
    }
    LocalDateTime localDateTime = LocalDateTime.now();
    String formatDate = DateTimeFormatter.ofPattern("yyy_MM_dd_HH_mm_ss").format(localDateTime);
    File file = new File(path + "/" + idNode + "_" + formatDate + ".docx");
    // Write the Document in file system
    FileOutputStream out = new FileOutputStream(file);

    // create table
    XWPFTable table = document.createTable();

    // create first row
    XWPFTableRow tableRowOne = table.getRow(0);
    createheader(tableRowOne);

    Long idNodeLong = Long.valueOf(idNode);
    NodeVO nodeVO = null;
    List<ModuleVO> moduleList = moduleService.findById(idNodeLong);
    List<FragmentVO> fragmentList = fragmentService.findById(idNodeLong);

    if (!filterStudyAgent) {
      if (!moduleList.isEmpty() && moduleList.get(0) != null) {
        nodeVO = moduleList.get(0);
      } else if (!fragmentList.isEmpty()) {
        nodeVO = fragmentList.get(0);
      }
    } else {
      if (!moduleList.isEmpty() && moduleList.get(0) != null) {
        ModuleVO moduleVO = moduleList.get(0);
        nodeVO = systemPropertyService.filterModulesNodesWithStudyAgents(moduleVO);
      } else if (!fragmentList.isEmpty() && fragmentList.get(0) != null) {
        FragmentVO fragmentVO = fragmentList.get(0);
        nodeVO = systemPropertyService.filterFragmentNodesWithStudyAgents(fragmentVO);
      }
    }

    if (nodeVO == null) {
      log.info("Can't generate document for null object");
    } else {
      generateRowForNodes(nodeVO, table, 1);
      document.write(out);
      log.info("doc report created in " + file.getAbsolutePath() + " successfully.");
    }

    document.close();
    out.flush();
    out.close();
    return file;
  }

  public boolean containsInArray(String[] arr, String targetValue) {
    return Arrays.asList(arr).contains(targetValue);
  }

  private void generateRowForNodes(NodeVO vo, XWPFTable table, int row, String[] listOfIds) {
    if (listOfIds != null && !containsInArray(listOfIds, String.valueOf(vo.getIdNode()))) {
      return;
    }
    XWPFTableRow tablerow = table.createRow();
    tablerow.getCell(0).setText(vo.getNumber());
    tablerow.getCell(1).setText(vo.getNodeType());
    tablerow.getCell(2).setText(vo.getName());
    tablerow.getCell(3).setText(vo.getDescription());
    row++;
    if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
      for (NodeVO nodes : vo.getChildNodes()) {
        generateRowForNodes(nodes, table, row);
      }
    }

  }

  private void populateDescriptions(String[] descriptions, XWPFTable table) {
    generateRow(descriptions, 1, table);
  }

  private void populateNodeNumbers(String[] nodeNumbers, XWPFTable table) {
    generateRow(nodeNumbers, 0, table);
  }

  private void generateRow(String[] strings, int column, XWPFTable table) {
    for (int i = 0; i < strings.length; i++) {
      if (!map.containsKey(i)) {
        map.put(i, table.createRow());
      }
      XWPFTableRow xwpfTableRow = map.get(i);
      xwpfTableRow.getCell(column).setText(strings[i]);
    }
  }

  private void generateRowForNodes(NodeVO vo, XWPFTable table, int row) {
    XWPFTableRow tablerow = table.createRow();
    tablerow.getCell(0).setText(vo.getNumber());
    tablerow.getCell(1).setText(vo.getNodeType());
    tablerow.getCell(2).setText(vo.getName());
    tablerow.getCell(3).setText(vo.getDescription());
    row++;
    if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
      for (NodeVO nodes : vo.getChildNodes()) {
        generateRowForNodes(nodes, table, row);
      }
    }
  }

  private void createheader(XWPFTableRow tableRowOne) {
    tableRowOne.getCell(0).setText("Node Number");
    tableRowOne.addNewTableCell().setText("Type");
    tableRowOne.addNewTableCell().setText("Name");
    tableRowOne.addNewTableCell().setText("Description");
    // tableRowOne.addNewTableCell().setText("Translation");
  }

}
