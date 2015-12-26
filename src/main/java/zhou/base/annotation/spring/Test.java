package zhou.base.annotation.spring;

public class Test {

	private IUser userDao;

	public IUser getUserDao() {
		return userDao;
	}

	@Nation( NationEnum.CHINA)
	public void setUserDao(IUser userDao) {
		this.userDao = userDao;
	}
	
	public void loginTest(){
		userDao.login();
	}
}
