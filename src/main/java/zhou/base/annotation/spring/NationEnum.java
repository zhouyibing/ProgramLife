package zhou.base.annotation.spring;

public enum NationEnum {
	CHINA("zhou.base.annotation.spring.ChineseUserImpl"),
	ENGLISH("zhou.base.annotation.spring.EnglishUserImpl"),
	KOREA("zhou.base.annotation.spring.KoreaUserImpl");
	
	private String cls;

	private NationEnum(String cls) {
		this.cls = cls;
	}

	public String getCls() {
		return cls;
	}
}
