package bki;

/**
 * Tests the {@link Hunter} robot versus Walls robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusWalls extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Walls,bki.Hunter";
  }
}