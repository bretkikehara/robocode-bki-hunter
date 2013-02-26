package bki;

import static org.junit.Assert.assertTrue;
import robocode.BattleResults;
import robocode.Rules;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.snapshot.ITurnSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link RobotHelper#Hunter} robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunter extends RobotTestBed {

  final String myRobotName = "bki.Hunter*";

  /**
   * Specifies that SittingDuck and DaCruzer are to be matched up in this test case.
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

  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    for (BattleResults results : event.getIndexedResults()) {
      if (myRobotName.equals(results.getTeamLeaderName())) {
        boolean check = results.getFirsts() > 10;
        assertTrue("Won at least 50%", check);
      }
    }
  }

  /*
   * Tests what happens on turn end.
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
        boolean test = testDouble(Rules.MAX_BULLET_POWER, bullet.getPower(), 0.3);
        if (test) {
          assertTrue("Always max fire power: " + bullet.getPower(), test);
        }
        else {
          // previous bullet may have hit gaining us health, so health can be as much as 10.
          test = robot.getEnergy() < 10.0;
          assertTrue("Low life: " + robot.getEnergy(), test);
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