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

  private String name = null;

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
  private void handleTurnGun(ScannedRobotEvent event) {

    // calculate angle to enemy in radians.
    double headingToEnemy = this.getHeading() + event.getBearing();
    double angleToEnemy = headingToEnemy - this.getGunHeading();

    // calculate the enemy coordinate.
    double rightAngle = RobotHelper.calculateRightAngleBasedOnHeading(headingToEnemy);
    double rightAngleRadian = Math.toRadians(rightAngle);
    
    double enemyX;
    double enemyY;

    double angle = headingToEnemy % 360;
    // handle negative angles.
    if (headingToEnemy < 0) {
      angle = 360 + angle;
    }
    
    double offsetX = Math.sin(rightAngleRadian) * event.getDistance();
    double offsetY = Math.cos(rightAngleRadian) * event.getDistance();
    
    if (angle < 90) {
      enemyX = this.getX() + offsetX;
      enemyY = this.getY() + offsetY;  
    }
    else if (angle < 180) {
      enemyX = this.getX() + offsetX;
      enemyY = this.getY() - offsetY;
    }
    else if (angle < 270) {
      enemyX = this.getX() - offsetX;
      enemyY = this.getY() - offsetY;
    }
    else {
      enemyX = this.getX() - offsetX;
      enemyY = this.getY() + offsetY;
    }
    
    out.println(enemyX);
    out.println(enemyY);
    
    
    double time = 5;
    double expectedDistance = event.getVelocity() * time;

    double expectedX = Math.sin(event.getHeadingRadians()) * expectedDistance + enemyX;
    double expectedY = Math.cos(event.getHeadingRadians()) * expectedDistance + enemyY;

    double changeX = enemyX - expectedX;
    double changeY = enemyY - expectedY;
    double distance = Math.sqrt(changeX * changeX + changeY * changeY);

    double c = (expectedDistance * expectedDistance);
    double a = (distance * distance);
    double b = (event.getDistance() * event.getDistance());

    double expectedAngle = Math.acos((c - a - b) / (-2 * distance * event.getDistance()));
    expectedAngle = RobotHelper.calculateOptimalAngle(expectedAngle);

    if (Double.isNaN(expectedAngle) || Double.isInfinite(expectedAngle)) {
      isTargeting = false;
    }
    else {
      this.setTurnGunRight(expectedAngle);
    }
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
      double bearing = event.getBearing();

      // keep enemy in our sights.
      double turnRadar = this.getHeading() + bearing - this.getRadarHeading();
      turnRadar = RobotHelper.calculateOptimalAngle(turnRadar);
      this.setTurnRadarRight(turnRadar);

      // get angle of this robot.
      if (!this.isWallAvoid) {
        this.setTurnRight(bearing + this.getHeading());
      }

      if (!isTargeting) {
        isTargeting = true;
        this.handleTurnGun(event);
      }
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