package bki;

import static org.junit.Assert.assertTrue;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Hunter} robot versus SittingDuck.
 * 
 * @author Bret K. Ikehara
 */
public abstract class AbstractTestHunterVersusRobot extends RobotTestBed {

  private static final String myRobotName = "bki.Hunter*";
  private static final double WIN_RATIO = 0.75;

  /**
   * Specifies the robots to battle.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public abstract String getRobotNames();

  /**
   * This test runs for 20 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 10;
  }

  /**
   * Compares the amount of wins.
   * 
   * @param event {@link BattleCompletedEvent}
   */
  @Override
  public void onBattleCompleted(BattleCompletedEvent event) {
    double winRatio = (getNumRounds() * WIN_RATIO);
    for (BattleResults results : event.getIndexedResults()) {
      if (myRobotName.equals(results.getTeamLeaderName())) {
        boolean check = results.getFirsts() >= winRatio;
        assertTrue("Win at least " + (int) (WIN_RATIO * 100) + "%", check);
      }
    }
  }
}