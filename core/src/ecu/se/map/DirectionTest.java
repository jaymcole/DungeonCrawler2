package ecu.se.map;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;
import ecu.se.map.Direction;
public class DirectionTest extends TestCase {

	//public String name, shorthand;
   // public int x, y, degAngle;
	
	/* havent got this part working
	public DirectionTest(String name, String shorthand, int x, int y, int degAngle) 
	{
	    name = name;
	    shorthand = shorthand;
	    x = x;
	    y = y;
	    degAngle = degAngle;
	}
	DirectionTest north = new DirectionTest("NORTH"	 , "N"	, 0, 	1, 	90);
	
	public void testNorth2Direction() {
		assertEquals(north, Direction.NORTH);
	}
	*/
	
	//testing name
	public void testNorthDirection() {
		assertEquals("NORTH", Direction.NORTH.getName());
	}
	public void testSouthDirection() {
		assertEquals("SOUTH", Direction.SOUTH.getName());
	}
	public void testWestDirection() {
		assertEquals("WEST", Direction.WEST.getName());
	}
	public void testEastDirection() {
		assertEquals("EAST", Direction.EAST.getName());
	}
	public void testNorthEastDirection() {
		assertEquals("NORTHEAST", Direction.NORTHEAST.getName());
	}
	public void testSouthEastDirection() {
		assertEquals("SOUTHEAST", Direction.SOUTHEAST.getName());
	}
	public void testNorthWestDirection() {
		assertEquals("NORTHWEST", Direction.NORTHWEST.getName());
	}
	public void testSouthWestDirection() {
		assertEquals("SOUTHWEST", Direction.SOUTHWEST.getName());
	}
	//testing shorthand
	public void testNorth2Direction() {
		assertEquals("N", Direction.NORTH.getShorthand());
	}
	public void testSouth2Direction() {
		assertEquals("S", Direction.SOUTH.getShorthand());
	}
	public void testWest2Direction() {
		assertEquals("W", Direction.WEST.getShorthand());
	}
	public void testEast2Direction() {
		assertEquals("E", Direction.EAST.getShorthand());
	}
	//testing X value
	public void testNorth3Direction() {
		assertEquals(0, Direction.NORTH.getX());
	}
	public void testSouth3Direction() {
		assertEquals(0, Direction.SOUTH.getX());
	}
	public void testEast3Direction() {
		assertEquals(1, Direction.EAST.getX());
	}
	public void testWest3Direction() {
		assertEquals(-1, Direction.WEST.getX());
	}
	//testing Y value
	public void testNorth4Direction() {
		assertEquals(1, Direction.NORTH.getY());
	}
	//testing angDegree
	public void testNorth5Direction() {
		assertEquals(90, Direction.NORTH.getDegAngle());
	}
}
