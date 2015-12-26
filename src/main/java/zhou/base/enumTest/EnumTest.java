package zhou.base.enumTest;

import java.util.EnumSet;

public class EnumTest {

	public static void main(String[] args) {

		EnumSet<ScanType> scanType = EnumSet.allOf(ScanType.class);
		System.out.println(scanType.size());
	}

}
