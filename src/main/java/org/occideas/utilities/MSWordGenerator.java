package org.occideas.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.occideas.config.ReportConfig;
import org.occideas.fragment.service.FragmentService;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.commons.lang3.math.NumberUtils;

@Component
public class MSWordGenerator {

  public Map<Integer, XWPFTableRow> map;

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private ReportConfig reportConfig;
  @Autowired
  private StudyAgentUtil studyAgentUtil;
  @Autowired
  private ModuleService moduleService;
  @Autowired
  private FragmentService fragmentService;
  @Autowired
  private IModuleDao dao;
  @Autowired
  private INodeService nodeService;

  public File writeDocument(String idNode, boolean filterStudyAgent) throws IOException {
    if (idNode == null) {
      log.error("No idNode provided in MSWord.");
      return null;
    }
    // Blank Document
    map = new HashMap<>();
    XWPFDocument document = new XWPFDocument();
    String path = reportConfig.getExportDir();
    
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

 //   if (!filterStudyAgent) {
      if (!moduleList.isEmpty() && moduleList.get(0) != null) {
        nodeVO = moduleList.get(0);
      } else if (!fragmentList.isEmpty()) {
        nodeVO = fragmentList.get(0);
      }
  //  } else {
  //    if (!moduleList.isEmpty() && moduleList.get(0) != null) {
  //      ModuleVO moduleVO = moduleList.get(0);
  //      nodeVO = systemPropertyService.filterModulesNodesWithStudyAgents(moduleVO);
  //    } else if (!fragmentList.isEmpty() && fragmentList.get(0) != null) {
   //     FragmentVO fragmentVO = fragmentList.get(0);
   //     nodeVO = systemPropertyService.filterFragmentNodesWithStudyAgents(fragmentVO);
  //    }
 //   }
    //prefixAllNodes(nodeVO,nodeVO.getName());
    if (nodeVO == null) {
      log.info("Can't generate document for null object");
    } else {
		
		try {
			if (filterStudyAgent) {
              String[] listOfIdNodes;
              listOfIdNodes = studyAgentUtil.getStudyAgentCSV(String.valueOf(nodeVO.getIdNode()));
              generateRowForNodes(nodeVO, table, 1,listOfIdNodes);
              String runningPrefix = nodeVO.getName().substring(0,4).toLowerCase();

              String filePath = "/opt/data/redcap/";
              String zipFilePath = "/opt/data/redcapZips/"+nodeVO.getName()+".zip";
              String instrumentFilePath = "/opt/data/redcap/instrument.csv";
              File instrumentFile = createREDCapCSVFile(instrumentFilePath);
              createREDCapInstrumentFile(nodeVO,listOfIdNodes,runningPrefix,"", false,instrumentFile);
              ZipUtil.zipFolder(filePath,zipFilePath);
			}else {
				generateRowForNodes(nodeVO, table, 1,nodeVO.getName().substring(0,4).toLowerCase(),"",false);
			}
		  
		} catch (Exception e) {
		  log.error("Error getting study nodes from csv " + nodeVO.getTopNodeId() + " ", e);
		  System.out.println("Printing full Module");
		  generateRowForNodes(nodeVO, table, 1,nodeVO.getName().substring(0,4).toLowerCase(),"",false);
		}
		
	  
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
    if (listOfIds != null && containsInArray(listOfIds, String.valueOf(vo.getIdNode()))) {
    	XWPFTableRow tablerow = table.createRow();
        tablerow.getCell(0).setText(vo.getNumber());
        tablerow.getCell(1).setText(vo.getNodeType());
        tablerow.getCell(2).setText(vo.getName());
        //tablerow.getCell(3).setText(vo.getDescription());
        row++;
        if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
          for (NodeVO nodes : vo.getChildNodes()) {
            generateRowForNodes(nodes, table, row,listOfIds);
          }
        }
    }else {
    	if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
          for (NodeVO nodes : vo.getChildNodes()) {
            generateRowForNodes(nodes, table, row,listOfIds);
          }
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

  private File createREDCapCSVFile(String filePath) throws IOException {
    // Define the CSV header
    String header = "Variable / Field Name,Form Name,Section Header,Field Type,Field Label," +
            "Choices, Calculations, OR Slider Labels,Field Note,Text Validation Type OR Show Slider Number," +
            "Text Validation Min,Text Validation Max,Identifier?,Branching Logic (Show field only if...)," +
            "Required Field?,Custom Alignment,Question Number (surveys only),Matrix Group Name," +
            "Matrix Ranking?,Field Annotation";

    // Create a new file object
    File csvFile = new File(filePath);

    // Write the header to the file
    try (FileWriter writer = new FileWriter(csvFile)) {
      writer.write(header + "\n");
    }

    return csvFile;
  }

  private void createREDCapInstrumentFile(NodeVO vo,String[] listOfIds,String runningPrefix, String moduleLinkRule, Boolean parentIsMulti,File instrumentFile) throws IOException {
    if (listOfIds != null && containsInArray(listOfIds, String.valueOf(vo.getIdNode()))) {
      String redCapRow = new String();

      if (vo.getNodeclass().equalsIgnoreCase("Q")) {
        String nodeCode = vo.getNumber();
        //if(vo.getType().equalsIgnoreCase("Q_linkedmodule") || vo.getType().equalsIgnoreCase("Q_linkedajsm")){
        if (vo.getType().equalsIgnoreCase("Q_linkedajsm")) {
          long id = vo.getLink();
          NodeVO nodeVO = null;
          List<ModuleVO> moduleList = moduleService.findById(id);
          List<FragmentVO> fragmentList = fragmentService.findById(id);

          if (!moduleList.isEmpty() && moduleList.get(0) != null) {
            nodeVO = moduleList.get(0);
            NodeVO parentAnswerNodeVO = nodeService.getNode(Long.valueOf(vo.getParentId()));
            NodeVO parentQuestionNodeVO = nodeService.getNode(Long.valueOf(parentAnswerNodeVO.getParentId()));
            moduleLinkRule = "[" + runningPrefix + "_" + parentQuestionNodeVO.getNumber() + "]='" + runningPrefix + "_" + parentAnswerNodeVO.getNumber() + "'";

            runningPrefix += nodeVO.getName().substring(0, 4).toLowerCase();


            //prefixAllNodes(nodeVO,runningPrefix+nodeCode);
          } else if (!fragmentList.isEmpty()) {
            nodeVO = fragmentList.get(0);
            NodeVO parentAnswerNodeVO = nodeService.getNode(Long.valueOf(vo.getParentId()));
            if (parentAnswerNodeVO.getType().equalsIgnoreCase("M_Module")) {

            } else {
              NodeVO parentQuestionNodeVO = nodeService.getNode(Long.valueOf(parentAnswerNodeVO.getParentId()));
              moduleLinkRule = "[" + runningPrefix + "_" + parentQuestionNodeVO.getNumber() + "]='" + runningPrefix + "_" + parentAnswerNodeVO.getNumber() + "'";
            }
            String[] listOfIdNodes = studyAgentUtil.getStudyAgentCSV(String.valueOf(nodeVO.getIdNode()));
            ArrayList<String> list = new ArrayList<>(Arrays.asList(listOfIds));
            list.addAll(Arrays.asList(listOfIdNodes));
            String[] mergedArray = list.toArray(new String[0]);
            listOfIds = mergedArray;
            runningPrefix += nodeVO.getName().substring(0, 4).toLowerCase();

          }

          createREDCapInstrumentFile(nodeVO, listOfIds, runningPrefix, moduleLinkRule, parentIsMulti,instrumentFile);
        } else if (vo.getType().equalsIgnoreCase("Q_linkedmodule")) {
          // do nothing for now
        } else {
          String instrumentName = runningPrefix.substring(0, 4);
          redCapRow += runningPrefix + "_" + vo.getNumber();
          redCapRow += ",";
          redCapRow += instrumentName;
          redCapRow += ",,";
          String fieldType = "";

          if (vo.getType().equalsIgnoreCase("Q_simple")) {
            fieldType = "radio";
          } else if (vo.getType().equalsIgnoreCase("Q_single")) {
            fieldType = "radio";
          } else if (vo.getType().equalsIgnoreCase("Q_multiple")) {
            fieldType = "checkbox";

          } else {
            fieldType = "radio";
          }
          if (vo.getName().equalsIgnoreCase("What is the job number?")) {
            fieldType = "text";
          }
          redCapRow += fieldType;
          redCapRow += ",";
          //redCapRow += "\"(" +runningPrefix + vo.getNumber() + ") " + vo.getName()+"\"";
          redCapRow += "\"" + vo.getName() + "\"";
          redCapRow += ",\"";
          if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
            for (NodeVO nodes : vo.getChildNodes()) {
              redCapRow += runningPrefix + "_" + nodes.getNumber();
              redCapRow += ",";
              redCapRow += nodes.getName() + " | ";
            }
            redCapRow = redCapRow.substring(0, redCapRow.length() - 3)+"\"";
          }
          String[] nodeNumberSplit = vo.getNumber().split("_");
          String last = nodeNumberSplit[nodeNumberSplit.length - 1];
          redCapRow += ",,,,,,";

          if(last.equalsIgnoreCase("1") && parentIsMulti){
            String branchRule = transformModuleLinkRule(moduleLinkRule);
            redCapRow += branchRule;
            moduleLinkRule = "";
          } else if (!isNumeric(last)) {
            if (moduleLinkRule.length() > 0) {
              redCapRow += moduleLinkRule;
              moduleLinkRule = "";
            } else {
              String[] strSplit = splitAtLastNonNumericalCharacter(last);
              String parentNumber = strSplit[0];
              String childNumber = strSplit[1];
              String branchRule = "[";
              if (parentIsMulti) {
                branchRule += runningPrefix + "_" + parentNumber;
                branchRule += "(" + runningPrefix + "_" + childNumber + ")]='1'";
              } else {
                branchRule += runningPrefix + "_" + parentNumber;
                branchRule += "]='";
                branchRule += runningPrefix + "_" + childNumber;
                branchRule += "'";
              }
              redCapRow += branchRule;
            }
          } else {
            redCapRow += moduleLinkRule;
            moduleLinkRule = "";
          }
          System.out.println(redCapRow);
          try (FileWriter writer = new FileWriter(instrumentFile,true)) {
            writer.write(redCapRow + "\n");
          }catch (IOException e) {
            System.err.println("An error occurred while creating the CSV file: " + e.getMessage());
          }
        }
      }

      if(vo.getType().equalsIgnoreCase("F_ajsm")){

      }
      if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
        if (vo.getType().equalsIgnoreCase("Q_multiple")) {
          parentIsMulti = true;
        } else {
          if (!vo.getType().equalsIgnoreCase("P_simple")) {
            if(!vo.getType().equalsIgnoreCase("F_ajsm")){
              parentIsMulti = false;
            }
          }

        }
        for (NodeVO nodes : vo.getChildNodes()) {
          createREDCapInstrumentFile(nodes, listOfIds, runningPrefix, moduleLinkRule, parentIsMulti, instrumentFile);
        }
      }
    }
  }
  private String transformModuleLinkRule(String input) {
    // Check if the input matches the pattern
    String regex = "\\[(\\w+)]='(\\w+)'";
    if (input.matches(regex)) {
      // Extract the first and second strings using regex groups
      String firstString = input.replaceAll(regex, "$1");
      String secondString = input.replaceAll(regex, "$2");

      // Build the transformed string
      return String.format("[%s(%s)]='1'", firstString, secondString);
    }
    // Return the input unmodified if it doesn't match the pattern
    return input;
  }
  private void generateRowForNodes(NodeVO vo, XWPFTable table, int row, String runningPrefix, String moduleLinkRule, Boolean parentIsMulti) {
    XWPFTableRow tablerow = table.createRow();
    tablerow.getCell(0).setText(vo.getNumber());
    tablerow.getCell(1).setText(vo.getNodeType());
    tablerow.getCell(2).setText(vo.getName());
    //tablerow.getCell(3).setText(vo.getDescription());
    //System.out.println(vo.getNumber());

    String redCapRow = new String();
    //String moduleLinkRule = "";
    if (vo.getNodeclass().equalsIgnoreCase("Q")) {
      String nodeCode = vo.getNumber();
      //if(vo.getType().equalsIgnoreCase("Q_linkedmodule") || vo.getType().equalsIgnoreCase("Q_linkedajsm")){
      if (vo.getType().equalsIgnoreCase("Q_linkedajsm")) {
        long id = vo.getLink();
        NodeVO nodeVO = null;
        List<ModuleVO> moduleList = moduleService.findById(id);
        List<FragmentVO> fragmentList = fragmentService.findById(id);

        if (!moduleList.isEmpty() && moduleList.get(0) != null) {
          nodeVO = moduleList.get(0);
          NodeVO parentAnswerNodeVO = nodeService.getNode(Long.valueOf(vo.getParentId()));
          NodeVO parentQuestionNodeVO = nodeService.getNode(Long.valueOf(parentAnswerNodeVO.getParentId()));
          moduleLinkRule = "[" + runningPrefix + "_" + parentQuestionNodeVO.getNumber() + "]='" + runningPrefix + "_" + parentAnswerNodeVO.getNumber() + "'";

          runningPrefix += nodeVO.getName().substring(0, 4).toLowerCase();


          //prefixAllNodes(nodeVO,runningPrefix+nodeCode);
        } else if (!fragmentList.isEmpty()) {
          nodeVO = fragmentList.get(0);
          NodeVO parentAnswerNodeVO = nodeService.getNode(Long.valueOf(vo.getParentId()));
          if (parentAnswerNodeVO.getType().equalsIgnoreCase("M_Module")) {

          } else {
            NodeVO parentQuestionNodeVO = nodeService.getNode(Long.valueOf(parentAnswerNodeVO.getParentId()));
            moduleLinkRule = "[" + runningPrefix + "_" + parentQuestionNodeVO.getNumber() + "]='" + runningPrefix + "_" + parentAnswerNodeVO.getNumber() + "'";
          }


          runningPrefix += nodeVO.getName().substring(0, 4).toLowerCase();
        }
        generateRowForNodes(nodeVO, table, row, runningPrefix, moduleLinkRule, false);
      } else if(vo.getType().equalsIgnoreCase("Q_linkedmodule")){
        // do nothing for now
      } else {
        String instrumentName = runningPrefix.substring(0,4);
        redCapRow += runningPrefix + "_" + vo.getNumber();
        redCapRow += "\t";
        redCapRow += instrumentName;
        redCapRow += "\t\t";
        String fieldType = "";

        if (vo.getType().equalsIgnoreCase("Q_simple")) {
          fieldType = "radio";
        } else if (vo.getType().equalsIgnoreCase("Q_single")) {
          fieldType = "radio";
        } else if (vo.getType().equalsIgnoreCase("Q_multiple")) {
          fieldType = "checkbox";

        } else {
          fieldType = "radio";
        }
        if (vo.getName().equalsIgnoreCase("What is the job number?")) {
          fieldType = "text";
        }
        redCapRow += fieldType;
        redCapRow += "\t";
        redCapRow += "(" + vo.getNumber() + ") " + vo.getName();
        redCapRow += "\t";
        if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
          for (NodeVO nodes : vo.getChildNodes()) {
            redCapRow += runningPrefix + "_" + nodes.getNumber();
            redCapRow += ",";
            redCapRow += nodes.getName() + " | ";
          }
          redCapRow = redCapRow.substring(0, redCapRow.length() - 3);
        }
        String[] nodeNumberSplit = vo.getNumber().split("_");
        String last = nodeNumberSplit[nodeNumberSplit.length - 1];
        redCapRow += "\t\t\t\t\t\t";
        if (!isNumeric(last)) {
          /// Double debug = Double.parseDouble(last);
          //      if(debug>9){
          //      int iDebug = 0;
          //    iDebug++;
          //}
          if (moduleLinkRule.length() > 0) {
            redCapRow += moduleLinkRule;
            moduleLinkRule = "";
          } else {
            String[] strSplit = splitAtLastNonNumericalCharacter(last);
            String parentNumber = strSplit[0];
            String childNumber = strSplit[1];
            String branchRule = "[";
            if(parentIsMulti){
              branchRule += runningPrefix + "_" + parentNumber;
              branchRule += "("+runningPrefix + "_" + childNumber+")]='1'";
            }else{
              branchRule += runningPrefix + "_" + parentNumber;
              branchRule += "]='";
              branchRule += runningPrefix + "_" + childNumber;
              branchRule += "'";
            }


            redCapRow += branchRule;
          }

        } else {
          redCapRow += moduleLinkRule;
          moduleLinkRule = "";
        }
        System.out.println(redCapRow);
      }
    }


    row++;
    if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
      if(vo.getType().equalsIgnoreCase("Q_multiple")){
        parentIsMulti = true;
      }else{
        if(!vo.getType().equalsIgnoreCase("P_simple")){
          parentIsMulti = false;
        }

      }
      for (NodeVO nodes : vo.getChildNodes()) {
        generateRowForNodes(nodes, table, row, runningPrefix, moduleLinkRule, parentIsMulti);
      }
    }
  }

  private  String[] splitAtLastNonNumericalCharacter(String input) {
    if (input == null || input.isEmpty()) {
      return new String[]{"", ""};
    }

    int lastNonNumericIndex = -1;

    // Find the position of the last non-numerical character
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (!Character.isDigit(c)) {
        lastNonNumericIndex = i;
      }
    }

    // If no non-numerical character is found, return the whole string in the second part
    if (lastNonNumericIndex == -1) {
      return new String[]{"", input};
    }

    // Split the string
    String firstPart = input.substring(0, lastNonNumericIndex);
    String secondPart = input.substring(0,lastNonNumericIndex + 1);

    return new String[]{firstPart, secondPart};
  }
  private void prefixAllNodes(NodeVO nodeVO, String nodeCode) {
    nodeVO.setNumber(nodeCode+"_"+nodeVO.getNumber());
    for(NodeVO childNode: nodeVO.getChildNodes()){
      prefixAllNodes(childNode,nodeCode);
    }
  }
  private  boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }
    if(str.matches("-?\\d+")){
      return true;
    }else{
      return false;
    }
 //   try {
 //     Double.parseDouble(str); // Use Integer.parseInt(str) for integers only
 //     return true;
 //   } catch (NumberFormatException e) {
 //     return false;
 //   }
  }
  private void createheader(XWPFTableRow tableRowOne) {
    tableRowOne.getCell(0).setText("Node Number");
    tableRowOne.addNewTableCell().setText("Type");
    tableRowOne.addNewTableCell().setText("Text");
    //tableRowOne.addNewTableCell().setText("Description");
    // tableRowOne.addNewTableCell().setText("Translation");
  }

}
