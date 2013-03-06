package bki;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import robocode.Rules;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.snapshot.ITurnSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Hunter} robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunter extends RobotTestBed {

  private static final String myRobotName = "bki.Hunter*";

  private double positionX = -1, positionY = -1;
  private int positionCheckCount = 0;

  /**
   * Specifies the robots that will fight.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Tracker,bki.Hunter";
  }

  /**
   * This test runs for 20 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 20;
  }

  /**
   * Tests that a bullet is fire every turn.
   * 
   * @param event {@link TurnEndedEvent}
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    ITurnSnapshot turn = event.getTurnSnapshot();
    IRobotSnapshot[] robots = turn.getRobots();

    // tests the firepower.
    for (IBulletSnapshot bullet : turn.getBullets()) {
      IRobotSnapshot robot = robots[bullet.getOwnerIndex()];
      if (myRobotName.equals(robot.getName())) {
        // fire power is limited when life is low.
        if (robot.getEnergy() > 10.0) {
          boolean test = testDouble(Rules.MAX_BULLET_POWER, bullet.getPower(), 0.3);
          assertTrue("Always max fire power: " + bullet.getPower(), test);
        }
        else {
          boolean test = testDouble(Rules.MAX_BULLET_POWER, bullet.getPower(), 3.0);
          assertTrue("Fire as much as possible: " + bullet.getPower(), test);
        }
      }
    }

    // test to ensure we aren't in the same place.
    for (IRobotSnapshot robot : turn.getRobots()) {
      if (myRobotName.equals(robot.getName())) {
        if (this.positionX == -1) {
          this.positionX = robot.getX();
          this.positionY = robot.getY();
        }
        else {
          boolean checkX = testDouble(this.positionX, robot.getX(), 3.0);
          boolean checkY = testDouble(this.positionY, robot.getY(), 3.0);
          if (checkX && checkY) {
            positionCheckCount += 1;
          }
          else {
            positionCheckCount = 0;
          }

          if (positionCheckCount > 10) {
            fail("Robot got stuck on the wall.");
          }
        }
      }
    }
  }

  /**
   * Checks whether a double is equals with offset.
   * 
   * @param expected double
   * @param value double
   * @param epsilon double
   * @return boolean
   */
  public boolean testDouble(double expected, double value, double epsilon) {
    return (expected - epsilon < value) && (expected + epsilon > value);
  }
}