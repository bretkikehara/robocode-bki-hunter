package bki;

import org.junit.Test;
import bki.RobotHelper.Area;
import static org.junit.Assert.assertTrue;

/**
 * Provides the unit tests for the {@link RobotHelper} methods.
 * 
 * @author Bret K. Ikehara
 * 
 */
public class TestRobotHelper {

  /**
   * Checks double value with precision to the 0.05.
   * 
   * @param expected double Expected value.
   * @param value double Value that should be check against the expected value.
   * @return boolean
   */
  private static boolean checkDouble(double expected, double value) {
    double epsilon = 0.05;
    return Math.abs(expected - value) < epsilon;
  }

  /**
   * Tests the {@link RobotHelper#calculateDistance} method.
   */
  @Test
  public void testCalculateDistance() {

    // test basic
    double dist = RobotHelper.calculateDistance(8, 12, 10, 10);
    boolean b = checkDouble(2.8, dist);
    assertTrue("Distance should be 2.8", b);

    // test negative
    dist = RobotHelper.calculateDistance(-1, -3, 10, 10);
    b = checkDouble(17, dist);
    assertTrue("Distance should be 17", b);
  }

  /**
   * Test the {@link RobotHelper#calculateAngleToPoint} method.
   */
  @Test
  public void testCalculateAngleToPoint() {
    double centerX = 10, centerY = 10, angle, x, y;

    // robot position in regards to center: below x and below y.
    x = 8;
    y = 9;
    // face north.
    angle = RobotHelper.calculateAngleToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(63.43, angle);

    // face east
    angle = RobotHelper.calculateAngleToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(-26.56, angle);

    // face south
    angle = RobotHelper.calculateAngleToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-116.56, angle);

    // face south
    angle = RobotHelper.calculateAngleToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-206.56, angle);

    // robot position in regards to center: above x and below y.
    x = 13;
    y = 8;
    // face north.
    angle = RobotHelper.calculateAngleToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(-56.31, angle);

    // face east
    angle = RobotHelper.calculateAngleToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(-146.31, angle);

    // face south
    angle = RobotHelper.calculateAngleToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-236.31, angle);

    // face west
    angle = RobotHelper.calculateAngleToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-326.31, angle);

    // robot position in regards to center: below x and above y.
    x = 8;
    y = 13;
    // face north.
    angle = RobotHelper.calculateAngleToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(146.31, angle);

    // face east
    angle = RobotHelper.calculateAngleToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(56.31, angle);

    // face south
    angle = RobotHelper.calculateAngleToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-33.69, angle);

    // face west
    angle = RobotHelper.calculateAngleToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-123.69, angle);

    // robot position in regards to center: above x and above y.
    x = 12;
    y = 13;
    // face north.
    angle = RobotHelper.calculateAngleToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(213.69, angle);

    // face east
    angle = RobotHelper.calculateAngleToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(123.69, angle);

    // face south
    angle = RobotHelper.calculateAngleToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(33.69, angle);

    // face west
    angle = RobotHelper.calculateAngleToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-56.31, angle);
  }

  /**
   * Tests the {@link RobotHelper#calculateOptimalAngle} method.
   */
  @Test
  public void testCalculateOptimalTurnAngle() {
    double angle;

    angle = RobotHelper.calculateOptimalAngle(360);
    testDoubleHelper(0, angle);

    angle = RobotHelper.calculateOptimalAngle(720);
    testDoubleHelper(0, angle);

    angle = RobotHelper.calculateOptimalAngle(-360);
    testDoubleHelper(0, angle);

    angle = RobotHelper.calculateOptimalAngle(180);
    testDoubleHelper(180, angle);

    angle = RobotHelper.calculateOptimalAngle(-180);
    testDoubleHelper(-180, angle);

    angle = RobotHelper.calculateOptimalAngle(185);
    testDoubleHelper(-175, angle);

    angle = RobotHelper.calculateOptimalAngle(-185);
    testDoubleHelper(175, angle);
  }

  /**
   * Tests the {@link RobotHelper#calculateAngleToHeading} method.
   */
  @Test
  public void testCalculateAngleToHeading() {
    double angle, robotHeading, enemyBearing, enemyHeading, expected;

    // basic test.
    robotHeading = 0;
    enemyBearing = 45;
    enemyHeading = 90;
    expected = -135;
    angle = RobotHelper.calculateAngleToHeading(robotHeading, enemyBearing, enemyHeading);
    testDoubleHelper(expected, angle);

    // ensure angle is correctly normalized.
    robotHeading = 20;
    enemyBearing = -65;
    enemyHeading = 160;
    expected = 25;
    angle = RobotHelper.calculateAngleToHeading(robotHeading, enemyBearing, enemyHeading);
    testDoubleHelper(expected, angle);

    // TODO finish tests.
  }

  /**
   * Helps the tests angles.
   * 
   * @param expectedAngle double
   * @param angle double
   */
  public void testDoubleHelper(double expectedAngle, double angle) {
    boolean b = checkDouble(expectedAngle, angle);
    assertTrue("Angle should be " + expectedAngle + ", but is " + angle, b);
  }

  /**
   * Tests the {@link RobotHelper#calculateAvoidWall} method.
   */
  @Test
  public void testCalculateAvoidWall() {

    double angle, expectedAngle;
    Area wall;

    // test north wall
    wall = Area.NORTH;
    // north heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(expectedAngle, angle);

    // north west heading
    expectedAngle = -180;
    angle = RobotHelper.calculateAvoidWall(315, wall);
    testDoubleHelper(expectedAngle, angle);

    // north east heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(45, wall);
    testDoubleHelper(expectedAngle, angle);

    // south heading
    expectedAngle = 0;
    angle = RobotHelper.calculateAvoidWall(180, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north east wall
    wall = Area.NORTHEAST;

    // test north east heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(45, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north heading
    expectedAngle = -180;
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(expectedAngle, angle);

    // test east heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(90, wall);
    testDoubleHelper(expectedAngle, angle);

    // test south west heading
    expectedAngle = 0;
    angle = RobotHelper.calculateAvoidWall(270, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north west wall
    wall = Area.NORTHWEST;

    // test north west heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(315, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north heading
    expectedAngle = 180;
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(expectedAngle, angle);

    // test west heading
    expectedAngle = -180;
    angle = RobotHelper.calculateAvoidWall(270, wall);
    testDoubleHelper(expectedAngle, angle);

    // test south west heading
    expectedAngle = 0;
    angle = RobotHelper.calculateAvoidWall(135, wall);
    testDoubleHelper(expectedAngle, angle);
  }
}