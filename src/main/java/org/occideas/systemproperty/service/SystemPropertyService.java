package org.occideas.systemproperty.service;

import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.SystemPropertyVO;

import java.util.List;

public interface SystemPropertyService {

  SystemPropertyVO save(SystemPropertyVO sysProp);

  SystemPropertyVO getById(long id);

  List<SystemPropertyVO> getAll();

  void delete(SystemPropertyVO vo);

  SystemPropertyVO getByName(String name);

  List<SystemPropertyVO> getByType(String studyAgentSysProp);

  FragmentVO getFragmentNodesWithStudyAgents(FragmentVO vo);

  ModuleVO filterModulesNodesWithStudyAgents(ModuleVO moduleVO);

  FragmentVO filterFragmentNodesWithStudyAgents(FragmentVO fragmentVO);

  ModuleVO filterModulesNodesWithAgents(ModuleVO vo, long idAgent);

  FragmentVO filterFragmentNodesWithAgents(FragmentVO vo, Long idAgent);

  FragmentVO getFragmentNodesWithAgents(FragmentVO fragmentVO, Long idAgent);

  void populateNodeidList(Long nodeId);

  void testNodeidList(Long nodeId);

  List<String> filterNodesWithStudyAgents(NodeVO node);

  void listAllQId(List<String> listOfIdNodes, NodeVO vo);
}
