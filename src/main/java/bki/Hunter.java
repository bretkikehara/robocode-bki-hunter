package bki;

import bki.RobotHelper.Area;
import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

/**
 * Provides the implementation for a Robocode {@link AdvancedRobot}.
 * 
 * @author Bret K. Ikehara
 * 
 */
public class Hunter extends AdvancedRobot {

  private double bearing, distance;
  // double heading, velocity;
  private String name = null;

  @Override
  public void run() {
    this.turnRadarRight(360);
    this.setAdjustRadarForRobotTurn(true);
    this.setAdjustGunForRobotTurn(true);
    this.setAdjustRadarForGunTurn(true);

    while (true) {
      // find all enemies.
      this.setTurnRadarRight(360);

      if (this.name != null) {
        this.setAhead(100);
        
        // turn towards enemy
        handleTurn();

        // predict enemy location.
        handleTurnGun();
        this.name = null;
      }

      handleAvoidWall();

      this.execute();
    }
  }

  /**
   * Ensures that the robot will avoid the wall.
   */
  public void handleAvoidWall() {
    double offset = 300;
    
    // checks whether we are close to the wall.
    Area area =
        Area.getArea(this.getX(), this.getY(), this.getBattleFieldWidth(),
            this.getBattleFieldHeight(), offset, offset);
    int angleLocation = (int) (this.getHeading() / 45);

    if (!Area.UNKNOWN.equals(area)) {
      // ensure the robot turns away from the wall.
      if (area.getValue() == angleLocation) {
        this.setTurnRight(-180);
      }
      else if (area.getValue() == (angleLocation - 1) % 8) {
        this.setTurnLeft(180);
      }
      this.setAhead(50);
      this.execute();
    }
  }

  /**
   * Handle the robot turn.
   */
  private void handleTurn() {
    // get angle of this robot.
    double turnAngle = bearing + this.getHeading();
    turnAngle = RobotHelper.calculateOptimalAngle(turnAngle);
    this.setTurnRight(turnAngle);
  }

  /**
   * Turns the gun for predictive firing.
   */
  private void handleTurnGun() {

    // turn gun towrads the enemy.
    // double angleC = RobotHelper.calculateAngleToHeading(this.getHeading(), bearing, heading);
    // long timePassed = (System.currentTimeMillis() - time) / 1000;
    // double traveledDistance = velocity * timePassed;
    // double sideC = calculateLength(distance, traveledDistance, angleC);
    //
    // double predictiveTurn = Math.asin((traveledDistance - Math.sin(angleC)) / sideC);
    // predictiveTurn = RobotHelper.calculateOptimalAngle(predictiveTurn);
    // this.setTurnGunRight(predictiveTurn);
    // this.setFire(Rules.MAX_BULLET_POWER);

    double angleToEnemy = this.getHeading() - this.getGunHeading() + bearing;
    angleToEnemy = RobotHelper.calculateOptimalAngle(angleToEnemy);
    this.setTurnGunRight(angleToEnemy);
    this.setFire(Rules.MAX_BULLET_POWER);
  }

  /**
   * Calculates the length of a side using the law of cosines.
   * 
   * @param sideA double
   * @param sideB double
   * @param angleC double
   * @return sideC double
   */
  public double calculateLength(double sideA, double sideB, double angleC) {
    double sideC = Math.pow(sideA, 2) + Math.pow(sideB, 2) - 2 * sideA * sideB * Math.cos(angleC);
    return Math.sqrt(sideC);
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    // target the closest enemy.
    if (name == null || distance > event.getDistance()) {
      name = event.getName();
      distance = event.getDistance();
      bearing = event.getBearing();
      // heading = event.getHeading();
      // velocity = event.getVelocity();
    }
  }
}