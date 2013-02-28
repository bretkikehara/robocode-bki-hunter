package bki;

import robocode.Robot;

/**
 * Defines a Robocode Robot helper methods.
 * 
 * @author Bret K. Ikehara
 */
public class RobotHelper {

  public static final double MAX_AVOID_WALL_TURN = 75;
  
  /**
   * Defines an area enumeration for each direction starting with north. See
   * {@link Robot#getHeading}() for more information regarding meaning behind enumerated values.
   * 
   * @author Bret K. Ikehara
   */
  public enum Area {
    /**
     * Defines an unknown area. Most likely in the center where we don't case.
     */
    UNKNOWN(-1),
    /**
     * Defines the north section of the battle field.
     */
    NORTH(0),
    /**
     * Defines the north east section of the battle field.
     */
    NORTHEAST(1),
    /**
     * Defines the east section of the battle field.
     */
    EAST(2),
    /**
     * Defines the south east section of the battle field.
     */
    SOUTHEAST(3),
    /**
     * Defines the south section of the battle field.
     */
    SOUTH(4),
    /**
     * Defines the south west section of the battle field.
     */
    SOUTHWEST(5),
    /**
     * Defines the west section of the battle field.
     */
    WEST(6),

    /**
     * Defines the north west section of the battle field.
     */
    NORTHWEST(7);

    private int area;

    /**
     * Area enumeration.
     * 
     * @param area int
     */
    Area(int area) {
      this.area = area;
    }

    /**
     * Convenience method to ensure proper value.
     * 
     * @param i Area number.
     * @return {@link Area}
     */
    private static Area valueOf(int i) {
      switch (i) {
      case 0:
        return NORTH;
      case 1:
        return NORTHEAST;
      case 2:
        return EAST;
      case 3:
        return SOUTHEAST;
      case 4:
        return SOUTH;
      case 5:
        return SOUTHWEST;
      case 6:
        return WEST;
      case 7:
        return NORTHWEST;
      default:
        return UNKNOWN;
      }
    }

    /**
     * Gets the area of the robot on the battle field dimensions with regards to the offset from the
     * wall.
     * 
     * @param robotX Robot x coordinate.
     * @param robotY Robot y coordinate.
     * @param battleFiledWidth Battle field width.
     * @param battleFieldHeight Battle field height.
     * @param widthOffset The padding inside the battle field.
     * @param heightOffset The padding inside the battle field.
     * @return Area
     */
    public static Area getArea(double robotX, double robotY, double battleFiledWidth,
        double battleFieldHeight, double widthOffset, double heightOffset) {
      boolean nearWallXMin = robotX < widthOffset;
      boolean nearWallXMax = robotX > battleFiledWidth - widthOffset;
      boolean nearWallYMin = robotY < heightOffset;
      boolean nearWallYMax = robotY > battleFieldHeight - heightOffset;
      if (nearWallYMax) {
        if (nearWallXMax) {
          return Area.valueOf(1);
        }
        else if (nearWallXMin) {
          return Area.valueOf(7);
        }
        else {
          return Area.valueOf(0);
        }
      }
      else if (nearWallYMin) {
        if (nearWallXMax) {
          return Area.valueOf(3);
        }
        else if (nearWallXMin) {
          return Area.valueOf(5);
        }
        else {
          return Area.valueOf(4);
        }
      }
      else {
        if (nearWallXMax) {
          return Area.valueOf(2);
        }
        else if (nearWallXMin) {
          return Area.valueOf(6);
        }
        else {
          return Area.valueOf(-1);
        }
      }
    }

    /**
     * Gets the area value.
     * 
     * @return int
     */
    public int getValue() {
      return area;
    }
  };

  // helps calculate radian to degree.
  private static final double RADIAN_TO_DEGREE = 180 / Math.PI;

  /**
   * Converts a radian value to degree value.
   * 
   * @param radian double
   * @return double
   */
  public static final double convertRadiantoDegrees(double radian) {
    return radian * RADIAN_TO_DEGREE;
  }

  /**
   * Calculates the distance between two points on a Cartesian plane.
   * 
   * @param robot {@link Robot}
   * @param x2 double To-coordinate.
   * @param y2 double To-coordinate.
   * @return double
   */
  public static double calculateDistance(final Robot robot, final double x2, final double y2) {
    return calculateDistance(robot.getX(), robot.getY(), x2, y2);

  }

  /**
   * Calculates the distance between two points on a Cartesian plane.
   * 
   * @param x1 double From-coordinate.
   * @param y1 double From-coordinate.
   * @param x2 double To-coordinate.
   * @param y2 double To-coordinate.
   * @return double
   */
  public static double calculateDistance(final double x1, final double y1, final double x2,
      final double y2) {
    double value = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
    return Math.sqrt(value);
  }

  /**
   * Calculate the angle needed to face towards a xy-point.
   * 
   * @param robot {@link Robot}
   * @param pointX double Coordinate to point the robot.
   * @param pointY double Coordinate to point the robot.
   * @return double
   */
  public static double calculateAngleToPoint(final Robot robot, final double pointX,
      final double pointY) {
    return calculateAngleToPoint(robot.getHeading(), robot.getX(), robot.getY(), pointX, pointY);
  }

  /**
   * Calculate the angle needed to face towards a xy-point. Return value is given as an angle that a
   * robot could use to turn right.
   * 
   * @param heading double Robot's heading.
   * @param robotX double Robot's x coordinate.
   * @param robotY double Robot's y coordinate.
   * @param pointX double Coordinate to point the robot.
   * @param pointY double Coordinate to point the robot.
   * @return double
   */
  public static double calculateAngleToPoint(final double heading, final double robotX,
      final double robotY, final double pointX, final double pointY) {
    double tanget = (pointX - robotX) / (pointY - robotY);
    double angle = Math.atan(tanget);
    angle = convertRadiantoDegrees(angle);

    // face in the y-direction towards the point.
    double headingTowardsPoint;
    if (robotY > pointY) {
      headingTowardsPoint = 180 - heading;
    }
    else {
      headingTowardsPoint = 0 - heading;
    }

    return headingTowardsPoint + angle;
  }

  /**
   * Calculates the oblique triangle in regards to an enemy bearing and heading. Helps calculate the
   * angle needing to turn the gun to hit our target.
   * 
   * @param robotHeading double
   * @param enemyBearing double
   * @param enemyHeading double
   * @return double
   */
  public static double calculateAngleToHeading(final double robotHeading,
      final double enemyBearing, final double enemyHeading) {
    double angleToRobot = (robotHeading + enemyBearing) % 360;
    // right triangle angle.
    double triangleAngle = angleToRobot % 90;

    double offset;
    if (angleToRobot < 90) {
      offset = 180;
    }
    else if (angleToRobot < 180) {
      offset = 270;
    }
    else if (angleToRobot < 270) {
      offset = 0;
    }
    else {
      offset = 90;
    }

    return enemyHeading - (triangleAngle + offset % 360);
  }

  /**
   * Calculates the optimal right-turn angle.
   * 
   * @param angle double
   * @return double
   */
  public static double calculateOptimalAngle(final double angle) {
    double a = angle % 360;
    if (a > 180) {
      return a - 360;
    }
    else if (a < -180) {
      return a + 360;
    }
    return a;
  }

  /**
   * Calculates how to avoid the wall based on the robot's area based on the heading of the robot.
   * 
   * @param robotHeading double
   * @param area {@link Area}
   * @return double
   */
  public static double calculateAvoidWall(final double robotHeading, final Area area) {
    if (Area.UNKNOWN == area) {
      return 0;
    }

    // down cast to int.
    int angleLocation = (int) ((robotHeading % 360) / 45);
    int areaVal = area.getValue();

    int a2 = (areaVal + 1) % 8;
    int a3 = (areaVal - 1) % 8;
    int a4 = (areaVal - 2) % 8;

    // handle negative values for the check.
    if (areaVal == 0) {
      a3 = 7;
      a4 = 6;
    }
    else if (areaVal == 1) {
      a4 = 7;
    }

    // ensure the robot turns away from the wall.
    if (areaVal == angleLocation || a2 == angleLocation) {
      return MAX_AVOID_WALL_TURN;
    }
    else if (a3 == angleLocation || a4 == angleLocation) {
      return -MAX_AVOID_WALL_TURN;
    }
    return 0;
  }
}