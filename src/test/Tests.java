package test;

import static org.junit.Assert.*;

import org.junit.*;

import exceptions.NoRouteFoundException;
import routefinder.SatNavSystem;
import routefinder.conditions.NumberRoutesWithExactJunctionsCondition;
import routefinder.conditions.NumberRoutesWithMaxJunctionsCondition;

public class Tests {
	
	static SatNavSystem satNav;
	
	@BeforeClass
	public static void setUp() throws Exception{
		satNav = new SatNavSystem("input/testdata.txt");
	}
	
	@Test
	public void test1() throws Exception{
		String[] route = {"A", "B", "C"};
		
		assertEquals(satNav.distanceThroughJunctions(route), 9);
	}
	
	@Test
	public void test2() throws Exception{
		String[] route = {"A", "D"};
		
		assertEquals(satNav.distanceThroughJunctions(route), 5);
	}
	
	@Test
	public void test3() throws Exception{
		String[] route = {"A", "D", "C"};
		
		assertEquals(satNav.distanceThroughJunctions(route), 13);
	}
	
	@Test
	public void test4() throws Exception{
		String[] route = {"A", "E", "B", "C", "D"};
		
		assertEquals(satNav.distanceThroughJunctions(route), 21);
	}
	
	@Test(expected = Exception.class)
	public void test5() throws Exception{
		String[] route = {"A", "E", "D"};
		
		satNav.distanceThroughJunctions(route);
	}
	
	@Test
	public void test6() throws NoRouteFoundException{
		NumberRoutesWithMaxJunctionsCondition condition = new NumberRoutesWithMaxJunctionsCondition("C", "C", 3);
		assertEquals(condition.accept(satNav), 2);
	}
	
	@Test
	public void test7() throws NoRouteFoundException{
		NumberRoutesWithExactJunctionsCondition condition = new NumberRoutesWithExactJunctionsCondition("A", "C", 4);
		assertEquals(condition.accept(satNav), 3);
	}
	
	@Test
	public void test8(){
		assertEquals(satNav.shortestDistanceBetween("A", "C"), 9);
	}

	@Test
	public void test9(){
		assertEquals(satNav.shortestDistanceBetween("B", "B"), 9);
	}
	
	@Test
	public void test10(){
		assertEquals(satNav.numberOfTripsWithinDistance("C", "C", 30), 9);
	}
}
