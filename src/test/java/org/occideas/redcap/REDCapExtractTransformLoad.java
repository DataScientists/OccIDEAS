package org.occideas.redcap;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.agent.dao.AgentDao;
import org.occideas.module.dao.ModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class REDCapExtractTransformLoad {

	@Autowired
	ModuleDao jobModuleDao;
	@Autowired
	AgentDao agentDao;
	@Autowired
	private ModuleService moduleService;

	@Test
	void etl() {

		try {
			long idNodeLong = 75434;
			List<ModuleVO> moduleList = moduleService.findById(idNodeLong);
			NodeVO nodeVO = moduleList.get(0);
			if (nodeVO.getChildNodes() != null && !nodeVO.getChildNodes().isEmpty()) {
				for (NodeVO nodes : nodeVO.getChildNodes()) {
					generateRowForNodes(nodes,1);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void generateRowForNodes(NodeVO vo, int row) {

		System.out.println(vo.getNumber());
		row++;
		if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
			for (NodeVO nodes : vo.getChildNodes()) {
				generateRowForNodes(nodes, row);
			}
		}
	}
}
