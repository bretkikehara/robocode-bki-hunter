package bki;

/**
 * Tests the {@link Hunter} robot versus VelociRobot robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusVelociRobot extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.VelociRobot,bki.Hunter";
  }
}