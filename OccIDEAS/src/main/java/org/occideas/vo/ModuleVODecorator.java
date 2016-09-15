package org.occideas.vo;

public class ModuleVODecorator {

	private ModuleVO moduleVO;
	private boolean created;

	public ModuleVODecorator() {
	}
	
	public ModuleVODecorator(ModuleVO moduleVO, boolean created) {
		super();
		this.moduleVO = moduleVO;
		this.created = created;
	}

	public ModuleVO getModuleVO() {
		return moduleVO;
	}

	public void setModuleVO(ModuleVO moduleVO) {
		this.moduleVO = moduleVO;
	}

	public boolean isCreated() {
		return created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

}
