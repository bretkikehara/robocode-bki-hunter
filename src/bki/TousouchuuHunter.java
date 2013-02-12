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
public class TousouchuuHunter extends AdvancedRobot {

  private ScannedRobotEvent event;

  @Override
  public void run() {
    this.turnRadarRight(360);
    while (true) {
      this.setAdjustRadarForRobotTurn(true);
      this.setAdjustGunForRobotTurn(true);
      this.setAdjustRadarForGunTurn(true);

      // find all enemies.
      this.setTurnRadarRight(360);

      if (this.event != null) {
        this.setAhead(100);

        // get angle of this robot.
        double turnAngle = this.event.getBearing() + this.getHeading();
        turnAngle = RobotHelper.calculateOptimalAngle(turnAngle);
        this.setTurnRight(turnAngle);

        // turn gun towards the enemy.
        double angleToEnemy = this.getHeading() - this.getGunHeading() + event.getBearing();
        angleToEnemy = RobotHelper.calculateOptimalAngle(angleToEnemy);
        this.setTurnGunRight(angleToEnemy);
        this.setFire(Rules.MAX_BULLET_POWER);
        this.event = null;
      }
      this.execute();
    }
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    if (this.event == null) {
      this.event = event;
    }
    else if (event.getDistance() < this.event.getDistance()) {
      this.event = event;
    }
  }
}