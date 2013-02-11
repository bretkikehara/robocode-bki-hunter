package edu.hawaii.breti.robocode;

import robocode.AdvancedRobot;
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
      // find all enemies.
      this.setTurnRadarRight(360);

      if (this.event != null) {
        this.setAhead(100);
        double turnAngle = this.event.getHeading() - this.getHeading();
        turnAngle = RobotHelper.calculateOptimalAngle(turnAngle);
        this.setTurnRight(turnAngle);
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
    else if (this.event.equals(event.getName()) || event.getDistance() < this.event.getDistance()) {
      this.event = event;
    }
  }
}