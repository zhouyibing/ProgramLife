package zhou.base.enumTest;
/**
 * 扫描类型
 * @author Zhou Yibing
 *
 */
public enum ScanType{
	TOOK_SCAN("14",0x80000,0x40000),//揽件扫描
	TRANSFER_PACKAGE("03",0x20000,0x10000),//中转集包
	PACKAGE_SCAN("05",0x08000,0x04000),//集包扫描
	TRANSFER_ENTER("17",0x02000,0x01000),//入中转扫描
	TRANSFER_SEND("18",0x00800,0x00400),//中转发出
	TRUCK_WEIGH("29",0x00200,0x00100),//汽运称重
	AVIATION_WEIGH("44",0x00080,0x00040),//航空称重
	SEND_SCAN("81",0x00020,0x00010),//发出扫描
	TICKET_ARRIVED ("13",0x00008,0x00004),//派件到达
	SIGN("10",0x00002,0x00001);//签收
	//LOAD_SCAN("97",0x80000,0x40000),//装车扫描
	//UNLOAD_SCAN("96",0x80000,0x40000);//卸车扫描
	
	
	private String type;
	private int initalFlag;
	private int operationFlag;
	private ScanType(String type,int initalFlag,int operationFlag){
		this.type = type;
		this.initalFlag =  initalFlag;
		this.operationFlag =  operationFlag;
	}
	
	public static ScanType getScanType(String value){
		if(null==value||"".equals(value.trim()))
			return null;
		ScanType type = null;
		switch (value) {
		case "14":
			type = ScanType.TOOK_SCAN;
			break;
		case "03":
			type = ScanType.TRANSFER_PACKAGE;
			break;
		case "05":
			type = ScanType.PACKAGE_SCAN;
			break;
		case "17":
			type = ScanType.TRANSFER_ENTER;
			break;
		case "18":
			type = ScanType.TRANSFER_SEND;
			break;
		case "29":
			type = ScanType.TRUCK_WEIGH;
			break;
		case "44":
			type = ScanType.AVIATION_WEIGH;
			break;
		case "81":
			type = ScanType.SEND_SCAN;
			break;
		case "13":
			type = ScanType.TICKET_ARRIVED;
			break;
		case "10":
			type = ScanType.SIGN;
			break;
		default:
			type = null;
			break;
		}
		
		return type;
	}
	
	public String getType(){
		return type;
	}
	
	public int getInitalFlag(){
		return initalFlag;
	}
	
	public int getOperationFlag(){
		return operationFlag;
	}
	
	public static int getOrdinal(String scanType){
		ScanType type = getScanType(scanType);
		if(null==type)
		    return -1;
		else
			return type.ordinal();
	}
	
	public static void main(String[] args){
		System.out.println(ScanType.getScanType("18"));
	}
}
