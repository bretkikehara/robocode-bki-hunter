package bki;

/**
 * Tests the {@link Hunter} robot versus Target robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusTarget extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Target,bki.Hunter";
  }
}