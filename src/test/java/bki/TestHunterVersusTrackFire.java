package bki;

/**
 * Tests the {@link Hunter} robot versus TrackFire robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunterVersusTrackFire extends AbstractTestHunterVersusRobot {

  /**
   * Specifies the robots that will compete.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.TrackFire,bki.Hunter";
  }
}