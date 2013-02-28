package bki;

/**
 * Tests the {@link Hunter} robot versus SpinBot robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusSpinBot extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.SpinBot,bki.Hunter";
  }
}