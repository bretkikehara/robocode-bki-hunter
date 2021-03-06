package bki;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import bki.RobotHelper.Area;

/**
 * Test the area enumeration.
 * 
 * @author Bret K. Ikehara
 */
public class TestArea {

  /**
   * Tests the {@link RobotHelper.Area#getArea} method.
   */
  @Test
  public void testGetAreaSquareArena() {

    double x, y;
    double width = 800;
    double offset = 100;
    Area testArea, expected;

    // test north.
    x = 101;
    y = 750;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.NORTH;
    assertArea(expected, testArea);

    // test north east.
    x = 758;
    y = 750;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.NORTHEAST;
    assertArea(expected, testArea);

    // test east.
    x = 704;
    y = 159;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.EAST;
    assertArea(expected, testArea);

    // test south east.
    x = 704;
    y = 88;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.SOUTHEAST;
    assertArea(expected, testArea);

    // test south.
    x = 470;
    y = 70;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.SOUTH;
    assertArea(expected, testArea);

    // test south west
    x = 29;
    y = 60;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.SOUTHWEST;
    assertArea(expected, testArea);

    // test west.
    x = 46;
    y = 300;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.WEST;
    assertArea(expected, testArea);

    // test north west.
    x = 97;
    y = 750;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.NORTHWEST;
    assertArea(expected, testArea);

    // test unknown area.
    x = 100;
    y = 150;
    testArea = Area.getArea(x, y, width, width, offset, offset);
    expected = Area.UNKNOWN;
    assertArea(expected, testArea);
  }

  /**
   * Helper method to test area.
   * 
   * @param expected {@link Area}
   * @param test {@link Area}
   */
  private static void assertArea(Area expected, Area test) {
    assertEquals("Area expected is " + expected, expected, test);
  }
}
