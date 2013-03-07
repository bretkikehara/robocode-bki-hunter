package bki;

import java.awt.Color;
import bki.RobotHelper.Area;
import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

/**
 * Provides the implementation for a Robocode {@link AdvancedRobot}.
 * 
 * @author Bret K. Ikehara
 */
public class Hunter extends AdvancedRobot {

  private double bearing, distance;

  // private double heading, velocity;

  private String name = null;

  @Override
  public void run() {
    this.setColors(Color.black, Color.black, Color.red);
    
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
    // double enemyVector = RobotHelper.calculateVector(this.heading, this.velocity, 10);

    double angleToEnemy = this.getHeading() - this.getGunHeading() + bearing;
    angleToEnemy = RobotHelper.calculateOptimalAngle(angleToEnemy);
    this.setTurnGunRight(angleToEnemy);
    this.setFireBullet(Rules.MAX_BULLET_POWER);
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