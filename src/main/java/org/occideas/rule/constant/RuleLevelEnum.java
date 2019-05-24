package org.occideas.rule.constant;

public enum RuleLevelEnum {

	ProbHigh(0,"probHigh"),
	ProbMedium(1,"probMedium"),
	ProbLow(2,"probLow"),
	ProbUnknown(3,"probUnknown"),
	PossUnknown(4,"possUnknown"),
	NoExposure(5,"noExposure");
	
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

}
