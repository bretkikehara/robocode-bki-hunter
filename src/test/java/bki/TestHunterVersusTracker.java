package bki;

/**
 * Tests the {@link Hunter} robot versus Tracker robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusTracker extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Tracker,bki.Hunter";
  }
}