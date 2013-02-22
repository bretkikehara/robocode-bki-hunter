package bki;

import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.snapshot.ITurnSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link RobotHelper#Hunter} robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunter extends RobotTestBed {

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

  /**
   * Tests what happens on turn end.
   * 
   * @param event {@link TurnEndedEvent}
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {
    ITurnSnapshot turn = event.getTurnSnapshot();

    for (IRobotSnapshot robot : turn.getRobots()) {
      if ("bki".equals(robot.getName())) {
        // TODO fix test.
        // if (robot.getX())
        // robot.getBodyHeading()
      }
    }
  }

}