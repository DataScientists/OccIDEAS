package org.occideas.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.occideas.entity.Constant;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MSWordGenerator {

	public Map<Integer, XWPFTableRow> map;

	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SystemPropertyService systemPropertyService;

	public File writeDocument(String idNode, String[] nodeNumbers, String[] descriptions) throws IOException {
		// Blank Document
		map = new HashMap<>();
		XWPFDocument document = new XWPFDocument();
		String path = System.getProperty("user.home");
		SystemPropertyVO reportDir = systemPropertyService.getByName(Constant.REPORT_DOC_DIR);
		if (reportDir == null) {
			log.info("Admin config "+Constant.REPORT_DOC_DIR+ " does not exist going for default path user home.");
		}else{
			path = reportDir.getValue();
		}
		LocalDateTime localDateTime = LocalDateTime.now();
		String formatDate = DateTimeFormatter.ofPattern("yyy_MM_dd_HH_mm_ss").format(localDateTime);
		File file = new File(path +"/"+ idNode + "_" + formatDate + ".docx");
		// Write the Document in file system
		FileOutputStream out = new FileOutputStream(file);

		// create table
		XWPFTable table = document.createTable();

		// create first row
		XWPFTableRow tableRowOne = table.getRow(0);
		createheader(tableRowOne);
		populateNodeNumbers(nodeNumbers, table);
		populateDescriptions(descriptions, table);
		
		document.write(out);
		document.close();
		out.flush();
		out.close();
		log.info("doc report created in "+file.getAbsolutePath()+" successfully.");
		return file;
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

	private void createheader(XWPFTableRow tableRowOne) {
		tableRowOne.getCell(0).setText("Node Number");
		tableRowOne.addNewTableCell().setText("Description");
		tableRowOne.addNewTableCell().setText("Translation");
	}

}
