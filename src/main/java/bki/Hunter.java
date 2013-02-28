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

  long fireTime = 0;

  @Override
  public void run() {
    this.turnRadarRight(360);
    this.setAdjustRadarForRobotTurn(true);
    this.setAdjustGunForRobotTurn(true);
    this.setAdjustRadarForGunTurn(true);

    while (true) {
      // find all enemies.
      this.setTurnRadarRight(360);
      this.setAhead(100);

      if (this.name != null) {
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
    double offset = 100;
    Area area =
        Area.getArea(this.getX(), this.getY(), this.getBattleFieldWidth(),
            this.getBattleFieldHeight(), offset, 50);

    // ensure the robot turns away from the wall.
    double angleAwayFromWall = RobotHelper.calculateAvoidWall(this.getHeading(), area);

    // checks whether we are close to the wall.
    if (angleAwayFromWall != 0) {
      this.setTurnRight(angleAwayFromWall);
      this.setAhead(5);
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
    // turn gun towards the enemy.
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

    if (getGunTurnRemaining() == 0) {
      this.setFire(Rules.MAX_BULLET_POWER);
    }
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

  /**
   * Handles a scanned robot event.
   * 
   * @param event {@link ScannedRobotEvent}
   */
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