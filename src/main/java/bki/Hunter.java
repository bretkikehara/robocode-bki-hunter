package bki;

import java.awt.Color;
import bki.RobotHelper.Area;
import bki.RobotHelper.Coordinate;
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

  private String targetName = null;

  private boolean isWallAvoidPhase = false, isTargetingPhase = false;

  @Override
  public void run() {
    this.setColors(Color.black, Color.black, Color.red);
    this.setAdjustRadarForRobotTurn(true);
    this.setAdjustGunForRobotTurn(true);
    this.setAdjustRadarForGunTurn(true);

    // lock onto an enemy
    while (this.targetName == null) {
      this.setTurnRadarRight(360);
      this.execute();
    }

    while (true) {
      // only shoot after turning gun according to our predictive fire algorithm.
      if (isTargetingPhase && getGunTurnRemaining() == 0) {
        isTargetingPhase = false;
        this.setFire(Rules.MAX_BULLET_POWER);
      }

      // overrides any other move patterns.
      this.handleAvoidWall();
      if (this.isWallAvoidPhase && getTurnRemaining() == 0) {
        this.isWallAvoidPhase = false;
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
      this.isWallAvoidPhase = true;
      this.setTurnRight(angleAwayFromWall);
      this.setAhead(10);
    }
  }

  /**
   * Turns the gun for predictive firing. Some concepts taken from <a
   * href="http://www.ibm.com/developerworks/library/j-pred-targeting/">Secrets from the Robocode
   * masters: Predictive targeting</a>.
   * 
   * @param event {@link ScannedRobotEvent}
   */
  private void handleTurnGun(ScannedRobotEvent event) {

    // calculate enemy coordinate
    double headingToEnemy = this.getHeading() + event.getBearing();
    Coordinate enemyPosition =
        RobotHelper.calculateEnemyRobotPosition(this.getX(), this.getY(), headingToEnemy,
            event.getDistance());
    double enemyX = enemyPosition.getX();
    double enemyY = enemyPosition.getY();

    // ensure expected position is variable.
    double time = 20 + (Math.random() * 3);
    if (Math.random() < 0.5) {
      time = -time;
    }

    // calculate enemy expected position.
    double expectedDistance = event.getVelocity() * time;
    double expectedX = Math.sin(event.getHeadingRadians()) * expectedDistance + enemyX;
    double expectedY = Math.cos(event.getHeadingRadians()) * expectedDistance + enemyY;
    double absDistance = Math.abs(expectedDistance);

    // calculate the expected bullet distance.
    double bulletDistance =
        RobotHelper.calculateDistance(this.getX(), this.getY(), expectedX, expectedY);

    // calculate the expected angle using the law of cosines.
    double expectedAngle =
        RobotHelper.calculateLawOfCosinesForAngle(event.getDistance(), bulletDistance, absDistance);

    // calculate the angle the gun should turn.
    double gunTurnAngle = headingToEnemy - this.getGunHeading() + expectedAngle;
    gunTurnAngle = RobotHelper.calculateOptimalAngle(gunTurnAngle);

    // allow gun to turn before firing.
    if (Double.isNaN(gunTurnAngle) || Double.isInfinite(gunTurnAngle)) {
      isTargetingPhase = false;
    }
    else {
      this.setTurnGunRight(gunTurnAngle);
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
    if (targetName == null) {
      targetName = event.getName();
    }

    if (this.targetName.equals(event.getName())) {
      this.setAhead(100);
      double bearing = event.getBearing();

      // keep enemy in our sights.
      double turnRadar = this.getHeading() + bearing - this.getRadarHeading();
      turnRadar = RobotHelper.calculateOptimalAngle(turnRadar);
      this.setTurnRadarRight(turnRadar);

      // get angle of this robot.
      if (!this.isWallAvoidPhase) {
        this.setTurnRight(bearing + this.getHeading());
      }

      // calculate our predictive firing.
      if (!isTargetingPhase) {
        isTargetingPhase = true;
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
    if (this.targetName.equals(event.getName())) {
      this.targetName = null;
    }
  }
}