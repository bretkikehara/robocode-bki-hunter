package bki;

import bki.RobotHelper;
import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

/**
 * Provides the implementation for a Robocode {@link AdvancedRobot}
 * 
 * @author Bret K. Ikehara
 * 
 */
public class Hunter extends AdvancedRobot {

  private long time;
  private double bearing, heading, velocity, distance;
  private String name = null;

  @Override
  public void run() {
    this.turnRadarRight(360);
    while (true) {
      this.setAdjustRadarForRobotTurn(true);
      this.setAdjustGunForRobotTurn(true);
      this.setAdjustRadarForGunTurn(true);

      // find all enemies.
      this.setTurnRadarRight(360);
      this.setAhead(100);

      if (this.name != null) {
        // turn towards enemy
        handleTurn();

        // predict enemy location.
        handleTurnGun();
      }
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
    double angleC = RobotHelper.calculateAngleToHeading(this.getHeading(), bearing, heading);
    long timePassed = (System.currentTimeMillis() - time) / 1000;
    double traveledDistance = velocity * timePassed;
    double sideC = calculateLength(distance, traveledDistance, angleC);

    double predictiveTurn = Math.asin((traveledDistance - Math.sin(angleC)) / sideC);
    predictiveTurn = RobotHelper.calculateOptimalAngle(predictiveTurn);
    this.turnGunRight(predictiveTurn);
    this.fire(Rules.MAX_BULLET_POWER);
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
      heading = event.getHeading();
      velocity = event.getVelocity();
      time = System.currentTimeMillis();
    }
  }
}