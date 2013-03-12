package bki;

import org.junit.Test;
import bki.RobotHelper.Area;
import bki.RobotHelper.Coordinate;
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
    return checkDouble(expected, value, epsilon);
  }

  /**
   * Checks double value with precision to the 0.05.
   * 
   * @param expected double Expected value.
   * @param value double Value that should be check against the expected value.
   * @param epsilon double Value offset allowed.
   * @return boolean
   */
  private static boolean checkDouble(double expected, double value, double epsilon) {
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
   * Test the {@link RobotHelper#calculateHeadingToPoint} method.
   */
  @Test
  public void testCalculateHeadingToPoint() {
    double centerX = 10, centerY = 10, angle, x, y;

    // robot position in regards to center: below x and below y.
    x = 8;
    y = 9;
    // face north.
    angle = RobotHelper.calculateHeadingToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(63.43, angle);

    // face east
    angle = RobotHelper.calculateHeadingToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(-26.56, angle);

    // face south
    angle = RobotHelper.calculateHeadingToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-116.56, angle);

    // face south
    angle = RobotHelper.calculateHeadingToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-206.56, angle);

    // robot position in regards to center: above x and below y.
    x = 13;
    y = 8;
    // face north.
    angle = RobotHelper.calculateHeadingToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(-56.31, angle);

    // face east
    angle = RobotHelper.calculateHeadingToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(-146.31, angle);

    // face south
    angle = RobotHelper.calculateHeadingToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-236.31, angle);

    // face west
    angle = RobotHelper.calculateHeadingToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-326.31, angle);

    // robot position in regards to center: below x and above y.
    x = 8;
    y = 13;
    // face north.
    angle = RobotHelper.calculateHeadingToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(146.31, angle);

    // face east
    angle = RobotHelper.calculateHeadingToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(56.31, angle);

    // face south
    angle = RobotHelper.calculateHeadingToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(-33.69, angle);

    // face west
    angle = RobotHelper.calculateHeadingToPoint(270, x, y, centerX, centerY);
    testDoubleHelper(-123.69, angle);

    // robot position in regards to center: above x and above y.
    x = 12;
    y = 13;
    // face north.
    angle = RobotHelper.calculateHeadingToPoint(0, x, y, centerX, centerY);
    testDoubleHelper(213.69, angle);

    // face east
    angle = RobotHelper.calculateHeadingToPoint(90, x, y, centerX, centerY);
    testDoubleHelper(123.69, angle);

    // face south
    angle = RobotHelper.calculateHeadingToPoint(180, x, y, centerX, centerY);
    testDoubleHelper(33.69, angle);

    // face west
    angle = RobotHelper.calculateHeadingToPoint(270, x, y, centerX, centerY);
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

    // TODO finish tests if used.
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

    // north heading
    expectedAngle = RobotHelper.MAX_AVOID_WALL_TURN;

    // test north wall
    wall = Area.NORTH;
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(expectedAngle, angle);

    // north west heading
    angle = RobotHelper.calculateAvoidWall(315, wall);
    testDoubleHelper(-expectedAngle, angle);

    // north east heading
    angle = RobotHelper.calculateAvoidWall(45, wall);
    testDoubleHelper(expectedAngle, angle);

    // south heading
    angle = RobotHelper.calculateAvoidWall(180, wall);
    testDoubleHelper(0, angle);

    // test north east wall
    wall = Area.NORTHEAST;

    // test north east heading
    angle = RobotHelper.calculateAvoidWall(45, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north heading
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(-expectedAngle, angle);

    // test east heading
    angle = RobotHelper.calculateAvoidWall(90, wall);
    testDoubleHelper(expectedAngle, angle);

    // test south west heading
    angle = RobotHelper.calculateAvoidWall(270, wall);
    testDoubleHelper(0, angle);

    // test north west wall
    wall = Area.NORTHWEST;

    // test north west heading
    angle = RobotHelper.calculateAvoidWall(315, wall);
    testDoubleHelper(expectedAngle, angle);

    // test north heading
    angle = RobotHelper.calculateAvoidWall(0, wall);
    testDoubleHelper(expectedAngle, angle);

    // test west heading
    angle = RobotHelper.calculateAvoidWall(270, wall);
    testDoubleHelper(-expectedAngle, angle);

    // test south west heading
    angle = RobotHelper.calculateAvoidWall(135, wall);
    testDoubleHelper(0, angle);
  }

  /**
   * Tests the {@link RobotHelper#calculateRightAngleBasedOnHeading} method.
   */
  @Test
  public void testCalculateRightAngleBasedOnHeading() {

    double angle, headingToEnemy;

    // angle is less than 90
    headingToEnemy = 45;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(45, angle);

    // angle is less than 180
    headingToEnemy = 100;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(80, angle);

    // angle is less than 270
    headingToEnemy = 200;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(20, angle);

    // angle is less than 360
    headingToEnemy = 300;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(60, angle);

    // angle is greater than 360
    headingToEnemy = 370;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(10, angle);

    // negative angle
    headingToEnemy = -10;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(10, angle);

    // negative angle greater than 360.
    headingToEnemy = -440;
    angle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    testDoubleHelper(80, angle);
  }

  /**
   * Tests the {@link RobotHelper#calculateEnemyRobotPosition} method.
   */
  @Test
  public void testCalculateEnemyRobotPosition() {

    Coordinate coord;

    double robotX, robotY, heading, distToEnemy;
    robotX = 100;
    robotY = 100;
    distToEnemy = 100;

    // heading less than 90.
    heading = 45;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(170, 170, coord);

    // angle is less than 180
    heading = 100;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(198, 82, coord);

    // angle is less than 270
    heading = 200;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(66, 7, coord);

    // angle is less than 360
    heading = 300;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(14, 150, coord);

    // angle is greater than 360
    // negative angle
    heading = -10;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(83, 198, coord);

    // negative angle greater than 360.
    heading = -440;
    coord = RobotHelper.calculateEnemyRobotPosition(robotX, robotY, heading, distToEnemy);
    testCoordinateHelper(2, 117, coord);    
  }

  /**
   * Helper method to test coordinates.
   * 
   * @param expectedX double
   * @param expectedY double
   * @param coordinate {@link Coordinate}
   */
  private void testCoordinateHelper(double expectedX, double expectedY, Coordinate coordinate) {
    double x = coordinate.getX();
    double y = coordinate.getY();
    double epsilon = 1.0;
    boolean b = checkDouble(expectedX, x, epsilon);
    assertTrue("X should be " + expectedX + ", but is " + x, b);
    b = checkDouble(expectedY, y, epsilon);
    assertTrue("Y should be " + expectedY + ", but is " + y, b);
  }
  
  /**
   * Tests the {@link RobotHelper#calculateLawOfCosinesForAngle} method.
   */
  @Test
  public void testCalculateLawOfCosinesForAngle() {
    
    double angle;
    double sideA, sideB, sideC;
    
    sideA = 9;
    sideB = 5;
    sideC = 8;
    angle = RobotHelper.calculateLawOfCosinesForAngle(sideA, sideB, sideC);
    testDoubleHelper(62.2, angle);
    
    
    sideA = 170;
    sideB = 223;
    sideC = 60;
    angle = RobotHelper.calculateLawOfCosinesForAngle(sideA, sideB, sideC);
    testDoubleHelper(8.28, angle);
  }
}