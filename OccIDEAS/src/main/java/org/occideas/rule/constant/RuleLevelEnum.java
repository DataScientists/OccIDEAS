package org.occideas.rule.constant;

public enum RuleLevelEnum {

	ProbHigh(0,"probHigh"),
	ProbMedium(1,"probMedium"),
	ProbLow(2,"probLow"),
	ProbUnknown(3,"probUnknown"),
	PossUnknown(4,"possUnknown"),
	NoExposure(4,"possUnknown");
	
	private int value;
	private String description;
	
	private RuleLevelEnum(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public static String getDescriptionByValue(int value){
		for(RuleLevelEnum x: RuleLevelEnum.values()){
			if(x.value == value){
				return x.getDescription();
			}
		}
		return "";
	}
	
	public static int getValueByDescription(String description){
		for(RuleLevelEnum x: RuleLevelEnum.values()){
			if(x.description.equals(description)){
				return x.getValue();
			}
		}
		return -1;
	}
	
}
