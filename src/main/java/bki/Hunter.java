package bki;

import java.awt.Color;
import bki.RobotHelper.Area;
import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;

/**
 * Provides the implementation for a Robocode {@link AdvancedRobot}.
 * 
 * @author Bret K. Ikehara
 */
public class Hunter extends AdvancedRobot {

  private double bearing, distance;
  private double vector, expectedTime;

  private String name = null;

  private long fireTime = 0, scanTime = 0;
  private boolean isWallAvoid = false, isTargeting = false;

  @Override
  public void run() {
    this.setColors(Color.black, Color.black, Color.red);
    this.setAdjustRadarForRobotTurn(true);
    this.setAdjustGunForRobotTurn(true);
    this.setAdjustRadarForGunTurn(true);

    // lock onto an enemy
    while (this.name == null) {
      this.setTurnRadarRight(360);
      this.execute();
    }

    while (true) {
      if (getGunTurnRemaining() == 0) {
        isTargeting = false;
        this.setFire(Rules.MAX_BULLET_POWER);
      }
      this.handleAvoidWall();

      if (this.isWallAvoid && getTurnRemaining() == 0) {
        this.isWallAvoid = false;
      }
      this.execute();
      fireTime = this.getTime() + 1;
    }
  }

  /**
   * Ensures that the robot will avoid the wall.
   */
  public void handleAvoidWall() {
    double offset = 100;
    Area area =
        Area.getArea(this.getX(), this.getY(), this.getBattleFieldWidth(),
            this.getBattleFieldHeight(), offset, offset);

    // ensure the robot turns away from the wall.
    double angleAwayFromWall = RobotHelper.calculateAvoidWall(this.getHeading(), area);

    // checks whether we are close to the wall.
    if (angleAwayFromWall != 0) {
      this.isWallAvoid = true;
      this.setTurnRight(angleAwayFromWall);
      this.setAhead(10);
    }
  }

  /**
   * Turns the gun for predictive firing.
   */
  private void handleTurnGun() {
    // double enemyVector = RobotHelper.calculateVector(this.heading, this.velocity, 10);

    double angleToEnemy = this.getHeading() - this.getGunHeading() + bearing;
    angleToEnemy = RobotHelper.calculateOptimalAngle(angleToEnemy);
    this.expectedTime = getTime() + angleToEnemy / Rules.MAX_TURN_RATE;

    this.setTurnGunRight(angleToEnemy);
  }

  /**
   * Handles a scanned robot event.
   * 
   * @param event {@link ScannedRobotEvent}
   */
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    // target the closest enemy.
    if (name == null) {
      name = event.getName();
    }

    if (this.name.equals(event.getName())) {
      this.setAhead(100);
      distance = event.getDistance();
      bearing = event.getBearing();

      // keep enemy in our sights.
      double turnRadar = this.getHeading() + bearing - this.getRadarHeading();
      turnRadar = RobotHelper.calculateOptimalAngle(turnRadar);
      this.setTurnRadarRight(turnRadar);

      // get angle of this robot.
      if (!this.isWallAvoid) {
        this.setTurnRight(bearing + this.getHeading());
      }

      if (!isTargeting) {
        this.handleTurnGun();
        isTargeting = true;
      }

      // calculate predictive firing.
      scanTime = this.getTime();
      vector = event.getVelocity() * Math.sin(event.getHeadingRadians());
    }
  }

  /**
   * Clear target on death.
   * 
   * @param event {@link RobotDeathEvent}
   */
  @Override
  public void onRobotDeath(RobotDeathEvent event) {
    if (this.name.equals(event.getName())) {
      this.name = null;
    }
  }
}