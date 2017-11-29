package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import junit.framework.TestCase;
import ecu.se.map.Direction;
public class DirectionTest extends TestCase {

	public String name, shorthand;
    public int x, y, degAngle;
	
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
	
	//testing nextExpandedDirectionCW, should return the next direction turning to the right (clockwise)
	public void testNextExpandedDirectionCWAfterNorth()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.NORTH);
		assertEquals(Direction.NORTHEAST, dir);
	}
	public void testNextExpandedDirectionCWAfterNorthEast()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.NORTHEAST);
		assertEquals(Direction.EAST, dir);
	}
	public void testNextExpandedDirectionCWAfterEast()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.EAST);
		assertEquals(Direction.SOUTHEAST, dir);
	}
	public void testNextExpandedDirectionCWAfterSouthEast()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.SOUTHEAST);
		assertEquals(Direction.SOUTH, dir);
	}
	public void testNextExpandedDirectionCWAfterSouth()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.SOUTH);
		assertEquals(Direction.SOUTHWEST, dir);
	}
	public void testNextExpandedDirectionCWAfterSouthWest()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.SOUTHWEST);
		assertEquals(Direction.WEST, dir);
	}
	public void testNextExpandedDirectionCWAfterWest()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.WEST);
		assertEquals(Direction.NORTHWEST, dir);
	}
	public void testNextExpandedDirectionCWAfterNorthWest()
	{
		Direction dir = Direction.nextExpandedDirectionCW(Direction.NORTHWEST);
		assertEquals(Direction.NORTH, dir);
	}

	//testing nextExpandedDirectionCCW, should return the next direction turning to the left (counter-clockwise)
	public void testnextExpandedDirectionCCWNorth()
	{
		Direction dir = Direction.nextExpandedDirectionCCW(Direction.NORTH);
		assertEquals(Direction.NORTHWEST, dir);
	}
	public void testnextExpandedDirectionCCWEast()
	{
		Direction dir = Direction.nextExpandedDirectionCCW(Direction.EAST);
		assertEquals(Direction.NORTHEAST, dir);
	}
	
	//testing oppositeDirection
	public void testOppositeDirectionNorth()
	{
		Direction dir = Direction.oppositeDirection(Direction.NORTH);
		assertEquals(Direction.SOUTH, dir);
	}
	public void testoppositeDirectionSouth()
	{
		Direction dir = Direction.oppositeDirection(Direction.SOUTH);
		assertEquals(Direction.NORTH, dir);
	}
	public void testoppositeDirectionEast()
	{
		Direction dir = Direction.oppositeDirection(Direction.EAST);
		assertEquals(Direction.WEST, dir);
	}
	public void testoppositeDirectionWest()
	{
		Direction dir = Direction.oppositeDirection(Direction.WEST);
		assertEquals(Direction.EAST, dir);
	}
	
	//testing turn the Direction to the left OR the right
	public void turnN()
	{
		
	}
	
	//testing non-expanded direction from (x1, y1), to (x2, y2)
	public void testgetDirectionToNorth()
	{
		Direction dir = Direction.getDirectionTo(2,9,3,8);
		assertEquals(Direction.NORTH, dir);
	}
	public void testgetDirectionToSouth()
	{
		Direction dir = Direction.getDirectionTo(9,2,3,8);
		assertEquals(Direction.SOUTH, dir);
	}
	public void testgetDirectionToEast()
	{
		Direction dir = Direction.getDirectionTo(3,12,1,6);
		assertEquals(Direction.EAST, dir);
	}
	public void testgetDirectionToWEST()
	{
		Direction dir = Direction.getDirectionTo(3,4,3,12);
		assertEquals(Direction.WEST, dir);
	}
            
}
