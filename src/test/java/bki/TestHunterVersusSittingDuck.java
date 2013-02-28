package bki;

/**
 * Tests the {@link Hunter} robot versus SittingDuck.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusSittingDuck extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.SittingDuck,bki.Hunter";
  }
}