package org.occideas.systemproperty.service;

import java.util.List;

import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.SystemPropertyVO;

public interface SystemPropertyService {

	public SystemPropertyVO save(SystemPropertyVO sysProp);
	public SystemPropertyVO getById(long id);
	public List<SystemPropertyVO> getAll();
	public void delete(SystemPropertyVO vo);
	public SystemPropertyVO getByName(String name);
	public List<SystemPropertyVO> getByType(String studyAgentSysProp);
	public FragmentVO getFragmentNodesWithStudyAgents(FragmentVO vo);
	public ModuleVO filterModulesNodesWithStudyAgents(ModuleVO moduleVO);
	public FragmentVO filterFragmentNodesWithStudyAgents(FragmentVO fragmentVO);
	public ModuleVO filterModulesNodesWithAgents(ModuleVO vo, long idAgent);
	public FragmentVO filterFragmentNodesWithAgents(FragmentVO vo, Long idAgent);
	public FragmentVO getFragmentNodesWithAgents(FragmentVO fragmentVO, Long idAgent);
	void populateNodeidList(Long nodeId);
	void testNodeidList(Long nodeId);
    List<String> filterNodesWithStudyAgents(NodeVO node);
    void listAllQId(List<String> listOfIdNodes, NodeVO vo);
}
