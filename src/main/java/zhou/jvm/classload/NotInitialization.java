package zhou.jvm.classload;

/**
 * 必须对类进行"初始化"的四种情况
 * 1 遇到new,getstatic,putstatic和invokestatic这4条字节码指令时，如果没有进行过初始化，则需要先触发其初始化
 * 2 使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先触发其初始化
 * 3 当初始化一个类的时候，如果发现其父类还没有初始化，则需要先触发其父类的初始化
 * 4 当虚拟机启动时，用户需要指定一个要执行的主类，虚拟机会先初始化这个主类
 * @author zhou
 *
 */
public class NotInitialization {
	static{
		System.out.println("NotInitialization init!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	  //1 对于静态字段/静态方法，只有直接定义这个字段/静态方法的类才会被初始化，通过其子类来引用父类中定义的静态字段/静态方法，只会触发父类的初始化而不会触发子类的初始化	
      System.out.println(SubClass.a());

      //2 通过数据定义来引用类，不会触发此类的初始化方法
      SuperClass[] sca = new SuperClass[10];
      
      //3 常量在编译阶段会存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化
      System.out.println(ConstClass.HELLO_WORLD);
      
      System.out.println(SubClass.cons);
	}

}
