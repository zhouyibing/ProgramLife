package framework.guava.collections;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Range;

public class TestTableAndRange {

	@Test
	public void testHashBasedTable(){
		//this data struct like Map<R,Map<C,V>>
		HashBasedTable<Integer, Integer, String> table = HashBasedTable.create();
		
		//Creating table with 5 rows and columns initially
		HashBasedTable<Integer,Integer,String> tableWithSpecifiedCapacity =
		HashBasedTable.create(5,5);
		//Creating a table from an existing table
		HashBasedTable<Integer,Integer,String> copiedTable =
		HashBasedTable.create(table);
		
		table.put(1,1,"Rook");
		table.put(1,2,"Knight");
		table.put(1,3,"Bishop");
		table.put(1,4,"Bishop");
		table.put(2,3,"Bishop");
		table.put(3,3,"aad");
		
		boolean contains11 = table.contains(1,1);
		boolean containColumn2 = table.containsColumn(2);
		boolean containsRow1 = table.containsRow(1);
		boolean containsRook = table.containsValue("Rook");
		System.out.println(table.remove(1,3));
		System.out.println(table.get(3,4));
		System.out.println(table);
		
		//table's view
		Map<Integer,String> columnMap = table.column(3);//this return row-value mappings with the given column's key value
		System.out.println(columnMap);
		Map<Integer,String> rowMap = table.row(1);//returned column-value mappints with the given row's key value
		System.out.println(rowMap);
	}
	
	@Test
	//用作范围值的比较。
	public void testRange(){
		Range<Integer> numberRange = Range.closed(1,10);
		//both return true meaning inclusive
		Assert.assertTrue(numberRange.contains(10));
		Assert.assertTrue(numberRange.contains(1));
		
		Range<Integer> numberRange2 = Range.open(1,10);
		//both return false meaning exclusive
		Assert.assertFalse(numberRange2.contains(10));
		Assert.assertFalse(numberRange2.contains(1));
		//Range实现了Predicate接口，可以运用刚在predicates.compose中，与Function结合使用
		//Predicates.compose(numberRange, function);
		
		
	}
}
