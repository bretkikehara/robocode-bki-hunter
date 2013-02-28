package bki;

import static org.junit.Assert.assertTrue;
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
      }
    }

    // TODO ensure robot doesn't get stuck on wall.
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